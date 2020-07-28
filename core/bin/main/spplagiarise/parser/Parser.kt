package spplagiarise.parser

import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.FileASTRequestor
import org.eclipse.jface.text.Document
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object Parser {
    val fileEncoding = "UTF-8"

    data class CompilationUnitDescriptor(val sourcePath: Path, val cu: CompilationUnit, val doc: Document)

    fun parse(sources: List<Path>, libraries: List<Path>, sourceDirectories: List<Path>): List<CompilationUnitDescriptor> {

        val parser = ASTParser.newParser(AST.JLS10)
        parser.setCompilerOptions(mutableMapOf(JavaCore.COMPILER_SOURCE to "1.8"))
        parser.setKind(ASTParser.K_COMPILATION_UNIT)
        parser.setResolveBindings(true)
        parser.setBindingsRecovery(true)
        parser.setEnvironment(
                libraries.map { it.toAbsolutePath().toString() }.toTypedArray(),
                sourceDirectories.map { it.toAbsolutePath().toString() }.toTypedArray(),
                Array(sourceDirectories.size) { fileEncoding },
                false
        )

        val requestor = AstRequestor()
        parser.createASTs(sources.map { it.toAbsolutePath().toString() }.toTypedArray(), Array(sources.size) { fileEncoding }, arrayOf(), requestor, null)

        return requestor.compilationUnits
    }

    private class AstRequestor : FileASTRequestor() {
        private val _compilationUnits = mutableListOf<CompilationUnitDescriptor>()
        val compilationUnits get() = _compilationUnits

        override fun acceptAST(sourceFilePath: String, ast: CompilationUnit) {
            val sourcePath = Paths.get(sourceFilePath)
            val file = Files.readAllLines(sourcePath).joinToString("\n")
            val doc = Document(file)

            _compilationUnits.add(CompilationUnitDescriptor(sourcePath, ast, doc))
        }
    }
}