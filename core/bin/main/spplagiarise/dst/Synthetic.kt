package spplagiarise.dst

class DSTSyntheticMethodDeclaration(
        var accessModifier: AccessModifier,
        var modifiers: MethodModifier,
        var returnType: String,
        var typeParameters: List<String>,
        var name: String,
        var parameters: List<DSTParameter>,
        var throws: List<DSTType>,
        var body: DSTBlockStatement
) : DSTBodyDeclaration() {
    init {
        this.body.parent = this
        this.parameters.assignParent()
        this.throws.assignParent()
    }
}

class DSTSyntheticMethodCall(
        val name: String,
        val parameters: List<DSTSimpleName>
) : DSTStatement()