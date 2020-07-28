package spplagiarise

import org.eclipse.jdt.core.dom.AST
import spplagiarise.ast.IdentifierVisitor
import spplagiarise.ast.KnownTypeLibrary
import spplagiarise.cdi.ObfuscatorContext
import spplagiarise.config.Configuration
import spplagiarise.document.ConfigDocument
import spplagiarise.document.ISerializer
import spplagiarise.dst.DSTProducer
import spplagiarise.dst.clone
import spplagiarise.naming.DeferredNameContext
import spplagiarise.obfuscation.DSTObfuscatorFilterFactory
import spplagiarise.obfuscation.Obfuscator
import spplagiarise.parser.BindingFinder
import spplagiarise.parser.Parser
import spplagiarise.printing.JavaOutputWriter
import spplagiarise.util.IFileUtils
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import java.util.stream.IntStream
import javax.enterprise.inject.spi.BeanManager
import javax.enterprise.inject.spi.CDI
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.streams.toList

@Singleton
class Application {

    @Inject
    lateinit var configuration: Configuration

    @Inject
    lateinit var serializer: ISerializer

    @Inject
    lateinit var fileUtils: IFileUtils

    @Inject
    lateinit var nameContext: DeferredNameContext

    @Inject
    lateinit var knownTypes: KnownTypeLibrary

    @Inject
    lateinit var idVisitor: IdentifierVisitor

    @Inject
    lateinit var filterFactory: DSTObfuscatorFilterFactory

    @Inject
    lateinit var javaWriter: JavaOutputWriter

    @Inject
    lateinit var obfContext: ObfuscatorContext

    @Inject
    lateinit var bm: BeanManager

    fun run(args: Array<String>) {
        if (args.count() != 1)
            throw IllegalArgumentException("Usage: JObfuscate <path to config file>")

        val arg = args[0]
        val configPath = Paths.get(arg)
        loadConfiguration(configPath)

        val sources = fileUtils.listFiles(configuration.inputRoot, ".java")
        val directories = fileUtils.listDirectories(configuration.inputRoot)

        val cus = Parser.parse(sources, configuration.libs, directories)
        val bindings = BindingFinder().find(cus.map { it.cu })
        cus.forEach { it.cu.accept(idVisitor) }
        loadKnownTypes(cus.first().cu.ast)

        val producer = DSTProducer(bindings, nameContext)
        val _dstcus = cus.parallelStream()
                .map { producer.evaluateCompilationUnit(it.cu) }
                .toList()

        IntStream.range(0, configuration.copies).forEach { index ->
            obfContext.use {
                val dstcus = _dstcus.map { it.clone() }

                val filters = filterFactory.produceObfuscators()
                val obfuscator = Obfuscator(dstcus, filters)
                obfuscator.run()

                javaWriter.write(dstcus, index.toString())
                println("Generated clone ${index+1} of ${configuration.copies}")
            }
        }

        println("Done.")
    }

    private fun loadConfiguration(configPath: Path) {
        if (!Files.exists(configPath))
            throw IllegalArgumentException("Config file '${configPath}' does not exist")

        val configDocument = serializer.read(configPath, ConfigDocument::class)

        val root = configPath.parent

        val inputRoot = root.resolve(configDocument.input)
        val outputRoot = root.resolve(configDocument.output)

        val libs = fileUtils.listFiles(root.resolve(configDocument.libs), ".jar")

        if (!Files.exists(inputRoot) || !Files.isDirectory(inputRoot))
            throw IllegalArgumentException("The input root must be folder")

        if (Files.exists(outputRoot))
            Files.walk(outputRoot)
                    .sorted(Comparator.reverseOrder())
                    .forEach { Files.delete(it) }

        if (!Files.exists(outputRoot))
            Files.createDirectory(outputRoot)

        configuration.apply {
            this.root = root

            this.inputRoot = inputRoot
            this.outputRoot = outputRoot
            this.libs = libs

            this.randomSeed = configDocument.seed ?: Instant.now().epochSecond
            this.copies = configDocument.copies
            this.extreme = configDocument.extreme

            this.l1 = configDocument.l1
            this.l2 = configDocument.l2
            this.l3 = configDocument.l3
            this.l4 = configDocument.l4
            this.l5 = configDocument.l5
        }
    }

    private fun loadKnownTypes(ast: AST) {
        knownTypes.charType = ast.resolveWellKnownType("char")
        knownTypes.doubleType = ast.resolveWellKnownType("double")
        knownTypes.intType = ast.resolveWellKnownType("int")
        knownTypes.stringType = ast.resolveWellKnownType("java.lang.String")
        knownTypes.voidType = ast.resolveWellKnownType("void")
        knownTypes.booleanType = ast.resolveWellKnownType("boolean")
    }
}