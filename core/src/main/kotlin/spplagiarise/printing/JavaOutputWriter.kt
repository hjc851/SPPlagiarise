package spplagiarise.printing

import spplagiarise.analytics.IAnalyticContext
import spplagiarise.config.Configuration
import spplagiarise.document.Serializer
import spplagiarise.dst.DSTCompilationUnit
import spplagiarise.dst.visitor.accept
import spplagiarise.naming.DeferredNameMappingContext
import spplagiarise.naming.NameAndTypeEvaluator
import java.io.File
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Path
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.ToolFactory
import org.eclipse.jdt.core.formatter.CodeFormatter
import org.eclipse.jface.text.Document

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

    val formatter: CodeFormatter = ToolFactory.createCodeFormatter(
        mutableMapOf(
            JavaCore.COMPILER_COMPLIANCE to "25",
            JavaCore.COMPILER_SOURCE to "25",
            JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM to "25"
        )
    )

    fun write(dcus: List<DSTCompilationUnit>, folderPrefix: String) {
        val outPath = outRoot.resolve(folderPrefix)

        if (!Files.exists(outPath))
            Files.createDirectory(outPath)

        dcus.forEach { dcu ->
            val filePath = makeFilePath(outPath, dcu)

            if (!Files.exists(filePath))
                Files.createFile(filePath)

            val tempWriter = StringWriter()
            val printer = DSTPrinter(tempWriter, nameEvaluator)
            dcu.accept(printer)
            tempWriter.flush()

            try {
                val source = tempWriter.buffer.toString()

                val edits = this.formatter.format(
                    CodeFormatter.K_COMPILATION_UNIT,
                    source,
                    0,
                    source.length,
                    0,
                    null
                )

                val document = Document(source)
                edits.apply(document)

                Files.writeString(filePath, document.get())

            } catch (e: Exception) {
                println("Unexpected error while writing document")
                e.printStackTrace()
            }
        }

        analytics.setIdentifierMappingCount(deferredNameMappingContext.getMappingCount())
        val analyticsFile = outPath.resolve("analytics.json")
        serializer.write(analyticsFile, analytics.toDocument())
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
                .first()
                .name
                .let { deferredNameMappingContext.getMappedName(it.id, true) }

        val filename = "$publicClassName.java"
        filePath = filePath.resolve(filename)

        return filePath
    }
}

