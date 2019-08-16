package spplagiarise.printing

import com.google.googlejavaformat.java.Formatter
import com.google.googlejavaformat.java.FormatterException
import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.document.Serializer
import spplagiarise.dst.AccessModifier
import spplagiarise.dst.DSTCompilationUnit
import spplagiarise.dst.visitor.accept
import spplagiarise.naming.DeferredNameMappingContext
import spplagiarise.naming.NameAndTypeEvaluator
import spplagiarise.util.ReaderCharSource
import spplagiarise.util.WriterCharSink
import org.apache.commons.io.input.CharSequenceReader
import java.io.File
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JavaOutputWriter {

    @Inject
    private lateinit var configuration: Configuration

    @Inject
    private lateinit var deferredNameMappingContext: DeferredNameMappingContext

    @Inject
    private lateinit var nameEvaluator: NameAndTypeEvaluator

    @Inject
    private lateinit var analytics: IAnalyticContext

    @Inject
    private lateinit var serializer: Serializer

    val outRoot: Path get() = configuration.outputRoot

    val formatter = Formatter()

    fun write(dcus: List<DSTCompilationUnit>, folderPrefix: String) {
        val outPath = outRoot.resolve(folderPrefix)

        if (!Files.exists(outPath))
            Files.createDirectory(outPath)

        val analyticsFile = outPath.resolve("analytics.json")
        serializer.write(analyticsFile, analytics.toDocument())

        dcus.forEach { dcu ->
            val filePath = makeFilePath(outPath, dcu)

            if (!Files.exists(filePath))
                Files.createFile(filePath)

            val tempWriter = StringWriter()
            val printer = DSTPrinter(tempWriter, nameEvaluator)
            dcu.accept(printer)
            tempWriter.flush()

            val bufferReader = CharSequenceReader(tempWriter.buffer)
            val bufferSource = ReaderCharSource(bufferReader)

            val fileWriter = Files.newBufferedWriter(filePath)
            val fileSink = WriterCharSink(fileWriter)

            try {
                formatter.formatSource(bufferSource, fileSink)
            } catch (e: FormatterException) {
                fileWriter.write(tempWriter.buffer.toString())

                println("Error in $filePath")
                for (diagnostic in e.diagnostics()) {
                    print("${diagnostic.line()}:${diagnostic.column()} - ${diagnostic.message()}")
                }
            } finally {
                fileWriter.close()
                tempWriter.close()
            }
        }
    }

    fun makeFilePath(outPath: Path, dcu: DSTCompilationUnit): Path {
        var filePath = outPath

        val packagePrefix = dcu.packageDeclaration
                ?.name
                ?.let { deferredNameMappingContext.getMappedName(it.id, false) }
                ?.replace(".", File.separator)

        if (packagePrefix != null) {
            filePath = filePath.resolve(packagePrefix)
            if (!Files.exists(filePath))
                Files.createDirectories(filePath)
        }

        val publicClassName = dcu.types
                .first { it.accessModifier == AccessModifier.PUBLIC }
                .name
                .let { deferredNameMappingContext.getMappedName(it.id, true) }

        val filename = "$publicClassName.java"
        filePath = filePath.resolve(filename)

        return filePath
    }
}

