package spplagiarise.printing

import spplagiarise.dst.*
import spplagiarise.dst.visitor.DSTNameAndTypeEvaluator
import spplagiarise.dst.visitor.DSTNodeVisitor
import spplagiarise.dst.visitor.accept
import spplagiarise.dst.visitor.evaluate
import java.io.Writer

class DSTPrinter(val out: Writer, val evaluator: DSTNameAndTypeEvaluator<String, Unit>) : DSTNodeVisitor {

    //  Utilities

    fun print(str: String) = out.append(str)
    fun println() = out.appendln()
    fun println(str: String) = out.appendln(str)

    private fun DSTName.evaluate(): String = this.evaluate(evaluator, Unit)
    private fun DSTType.evaluate(): String = this.evaluate(evaluator, Unit)

    //  Compilation Unit

    override fun visitDSTCompilationUnit(node: DSTCompilationUnit) {
        if (node.packageDeclaration != null) {
            visitDSTPackageDeclaration(node.packageDeclaration!!)
            println()
        }

        for (import in node.imports) {
            import.accept(this)
        }

        if (node.imports.isNotEmpty())
            println()

        for (type in node.types) {
            type.accept(this)
            println()
        }
    }

    //  Package Declaration

    override fun visitDSTPackageDeclaration(node: DSTPackageDeclaration) {
        print("package ")
        print(node.name.evaluate())
        println(";")
    }

    //  Imports

    override fun visitDSTSingleImportDeclaration(node: DSTSingleImportDeclaration) {
        print("import ")
        if (node.binding.declaringClass != null)
            Unit
        print(node.name.evaluate())
        println(";")
    }

    override fun visitDSTPackageImportDeclaration(node: DSTPackageImportDeclaration) {
        print("import ")
        print(node.name.evaluate())
        print(".*")
        println(";")
    }

    override fun visitDSTStaticFieldImportDeclaration(node: DSTStaticFieldImportDeclaration) {
        print("import static ")
        print(node.name.evaluate())
        println(";")
    }

    override fun visitDSTStaticMethodImportDeclaration(node: DSTStaticMethodImportDeclaration) {
        print("import static ")
        print(node.name.evaluate())
        println(";")
    }

    override fun visitDSTStaticAsterixImportDeclaration(node: DSTStaticAsterixImportDeclaration) {
        print("import static ")
        print(node.name.evaluate())
        print(".*")
        println(";")
    }

    //  Types

    override fun visitDSTClassOrInterfaceTypeDeclaration(node: DSTClassOrInterfaceTypeDeclaration) {
        print(node.accessModifier.text())

        if (node.isStatic)
            print("static ")

        if (node.isAbstract)
            print("abstract ")

        print(node.declarationType.name.toLowerCase())
        print(" ")

        print(node.name.evaluate())

        if (node.genericTypes.isNotEmpty()) {
            print("<")
            node.genericTypes.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.genericTypes.size - 1) {
                    print(", ")
                }
            }
            print(">")
        }
        print(" ")

        if (node.extends != null) {
            print("extends ")
            print(node.extends!!.evaluate())
            print(" ")
        }

        if (node.implements.isNotEmpty()) {
            if (node.declarationType == TypeDeclarationType.CLASS)
                print("implements ")
            else
                print("extends ")

            node.implements.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.implements.size - 1)
                    print(", ")
            }

            print(" ")
        }


        println("{")

        for (body in node.bodyDeclarations) {
            body.accept(this)
        }

        println("}")
        println()
    }

    override fun visitDSTEnumTypeDeclaration(node: DSTEnumTypeDeclaration) {
        print(node.accessModifier.text())
        print("enum ")
        print(node.name.evaluate())
        print(" ")

        if (node.implements.isNotEmpty()) {
            print("implements ")

            node.implements.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.implements.size - 1)
                    print(", ")
            }

            print(" ")
        }

        println("{")
        node.enumConstants.forEachIndexed { index, dstEnumConstant ->
            dstEnumConstant.accept(this)

            if (index < node.enumConstants.size - 1)
                println(", ")
            else
                println(";")
        }

        if (node.bodyDeclarations.isNotEmpty()) {
            println()
            node.bodyDeclarations.forEach { it.accept(this); println() }
        }

        println("}")
        println()
    }

    override fun visitDSTAnnotationTypeDeclaration(node: DSTAnnotationTypeDeclaration) {
        print(node.accessModifier.text())
        print("@interface ")
        print(node.name.evaluate())
        print(" ")

        println("{")
        node.bodyDeclarations.forEach { it.accept(this) }
        println("}")
        println()
    }

    override fun visitDSTEnumConstant(node: DSTEnumConstant) {
        print(node.name.evaluate())

        if (node.arguments.isNotEmpty()) {
            print("(")
            node.arguments.forEachIndexed { index, dstExpressionOrName ->
                dstExpressionOrName.accept(this)

                if (index < node.arguments.size - 1)
                    print(", ")
            }
            print(")")
        }

        if (node.classBody != null) {
            print(" ")
            node.classBody!!.accept(this)
        }
    }

    override fun visitDSTAnnotationMember(node: DSTAnnotationMember) {
        print(node.accessModifier.text())
        print(node.type.evaluate())
        print(" ")
        print(node.name.evaluate())
        print("() ")

        if (node.defaultValue != null) {
            print("default ")
            node.defaultValue!!.accept(this)
        }

        println(";")
    }

    override fun visitDSTFieldGroup(node: DSTFieldGroup) {
        print(node.accessModifier.text())
        print(node.modifiers.text())

        print(node.type.evaluate())
        print(" ")

        node.fields.forEachIndexed { index, dstFieldDeclaration ->
            dstFieldDeclaration.accept(this)

            if (index < node.fields.size - 1)
                print(", ")
        }

        println(";")
    }

    override fun visitDSTFieldDeclaration(node: DSTFieldDeclaration) {
        print(node.name.evaluate())

        for (i in 0 until node.extraDimensions)
            print("[]")

        if (node.initialiser != null) {
            print(" = ")
            node.initialiser!!.accept(this)
        }
    }

    override fun visitDSTConstructorDeclaration(node: DSTConstructorDeclaration) {
        println()

        print(node.accessModifier.text())
        print(node.modifiers.text())

        if (node.typeParameters.isNotEmpty()) {
            print("<")
            node.typeParameters.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.parameters.size - 1)
                    print(", ")
            }

            print("> ")
        }

        print(node.name.evaluate())
//        node.name.accept(this)

        print("(")
        node.parameters.forEachIndexed { index, dstParameter ->
            dstParameter.accept(this)

            if (index < node.parameters.size - 1)
                print(", ")
        }
        print(")")

        if (node.throws.isNotEmpty()) {
            print("throws ")

            node.throws.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.throws.size - 1)
                    print(", ")
            }
        }

        if (node.body != null) {
            node.body!!.accept(this)
        }
    }

    override fun visitDSTMethodDeclaration(node: DSTMethodDeclaration) {
        println()

        print(node.accessModifier.text())
        print(node.modifiers.text())

        if (node.typeParameters.isNotEmpty()) {
            print("<")
            node.typeParameters.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.parameters.size - 1)
                    print(", ")
            }

            print("> ")
        }

        print(node.returnType.evaluate())
        print(" ")
        print(node.name.evaluate())
//        node.name.accept(this)

        print("(")
        node.parameters.forEachIndexed { index, dstParameter ->
            dstParameter.accept(this)

            if (index < node.parameters.size - 1)
                print(", ")
        }
        print(")")

        if (node.throws.isNotEmpty()) {
            print("throws ")

            node.throws.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.throws.size - 1)
                    print(", ")
            }
        }

        if (node.body != null) {
            node.body!!.accept(this)
        }

        if (node.modifiers.isAbstract)
            println(";")
    }

    override fun visitDSTSyntheticMethodDeclaration(node: DSTSyntheticMethodDeclaration) {
        println()

        print(node.accessModifier.text())
        print(node.modifiers.text())

        if (node.typeParameters.isNotEmpty()) {
            print("<")
            node.typeParameters.forEachIndexed { index, dstType ->
                print(dstType)

                if (index < node.parameters.size - 1)
                    print(", ")
            }

            print("> ")
        }

        print(node.returnType)
        print(" ")
        print(node.name)

        print("(")
        node.parameters.forEachIndexed { index, dstParameter ->
            dstParameter.accept(this)

            if (index < node.parameters.size - 1)
                print(", ")
        }
        print(")")

        if (node.throws.isNotEmpty()) {
            print("throws ")

            node.throws.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.throws.size - 1)
                    print(", ")
            }
        }

        node.body.accept(this)
    }

    override fun visitDSTInitialiser(node: DSTInitialiser) {
        if (node.isStatic)
            print("static ")

        node.body.accept(this)
    }

    override fun visitDSTParameter(node: DSTParameter) {
        print(node.type.evaluate())
        print(" ")

        if (node.isVarArg)
            print("... ")

        print(node.name.evaluate())
//        node.name.accept(this)
    }

    override fun visitDSTDimension(node: DSTDimension) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitDSTModifier(node: DSTModifier) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //  Literals

    override fun visitDSTStringLiteral(node: DSTStringLiteral) {
        print(node.value)
    }

    override fun visitDSTCharLiteral(node: DSTCharLiteral) {
//        print("'")
        print(node.value)
//        print("'")
    }

    override fun visitDSTBooleanLiteral(node: DSTBooleanLiteral) {
        print(node.value.toString())
    }

    override fun visitDSTNumberLiteral(node: DSTNumberLiteral) {
        print(node.value)
    }

    override fun visitDSTTypeLiteral(node: DSTTypeLiteral) {
        print(node.type.evaluate())
        print(".class")
    }

    override fun visitDSTNullLiteral(node: DSTNullLiteral) {
        print("null")
    }

    //

    override fun visitDSTMethodCall(node: DSTMethodCall) {
        if (node.expression != null) {
            node.expression!!.accept(this)
            print(".")
        }

        if (node.typeArguments.isNotEmpty()) {
            print("<")
            node.typeArguments.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.typeArguments.size - 1)
                    print(", ")
            }
            print("> ")
        }

        print(node.name.evaluate())
//        node.name.accept(this)

        print("(")
        node.arguments.forEachIndexed { index, dstExpressionOrName ->
            dstExpressionOrName.accept(this)

            if (index < node.arguments.size - 1)
                print(", ")
        }
        print(")")
    }

    override fun visitDSTSyntheticMethodCall(node: DSTSyntheticMethodCall) {
        print(node.name)
        print("(")
        node.parameters.forEachIndexed { index, name ->
            name.accept(this)

            if (index < node.parameters.size - 1)
                print(", ")
        }
        println(");")
    }

    override fun visitDSTConstructorCall(node: DSTConstructorCall) {
        if (node.expression != null) {
            node.expression!!.accept(this)
            print(".")
        }

        print("new ")

        if (node.typeArguments.isNotEmpty()) {
            print("<")
            node.typeArguments.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.typeArguments.size - 1)
                    print(", ")
            }
            print("> ")
        }

        print(node.type.evaluate())

        print("(")
        node.arguments.forEachIndexed { index, dstExpressionOrName ->
            dstExpressionOrName.accept(this)

            if (index < node.arguments.size - 1)
                print(", ")
        }
        print(")")

        node.anonymousClassBody?.accept(this)
    }

    override fun visitDSTAssignment(node: DSTAssignment) {
        node.assignee.accept(this)
        print(" ")
        print(node.operator.toString())
        print(" ")
        node.expression.accept(this)
    }

    override fun visitDSTPrefixExpression(node: DSTPrefixExpression) {
        print(node.operator.toString())
        node.expression.accept(this)
    }

    override fun visitDSTMultiInfixExpression(node: DSTMultiInfixExpression) {

        node.expressions.first().accept(this)

        for (expr in node.expressions.drop(1)) {
            print(" ")
            print(node.operator.toString())
            print(" ")

            expr.accept(this)
        }
    }

    override fun visitDSTSingleInfixExpression(node: DSTSingleInfixExpression) {
        node.lhs.accept(this)
        print(" ")
        print(node.operator.toString())
        print(" ")
        node.rhs.accept(this)
    }

    override fun visitDSTPostfixExpression(node: DSTPostfixExpression) {
        node.expression.accept(this)
        print(node.operator.toString())
    }

    override fun visitDSTVariableDeclarationExpression(node: DSTVariableDeclarationExpression) {
        print(node.type.evaluate())
        print(" ")
        node.variables.forEachIndexed { index, dstLocalVariableDeclaration ->
            dstLocalVariableDeclaration.accept(this)

            if (index < node.variables.size - 1)
                print(", ")
        }
    }

    override fun visitDSTArrayAccess(node: DSTArrayAccess) {
        node.array.accept(this)
        print("[")
        node.index.accept(this)
        print("]")
    }

    override fun visitDSTParenthesisedExpression(node: DSTParenthesisedExpression) {
        print("(")
        node.expr.accept(this)
        print(")")
    }

    override fun visitDSTCastExpression(node: DSTCastExpression) {
        print("(")
        print(node.type.evaluate())
        print(") ")
        node.expression.accept(this)
    }

    override fun visitDSTArrayCreation(node: DSTArrayCreation) {
        print("new ")

        print(node.type.typeName.evaluate())

        for (dimension in node.dimensions) {
            print("[")
            dimension.accept(this)
            print("]")
        }

        val extraDimensions = node.type.dimensions - node.dimensions.count()
        for (i in 0 until extraDimensions)
            print("[]")

        if (node.initialiser != null) {
            print(" ")
            node.initialiser!!.accept(this)
        }
    }

    override fun visitDSTArrayInitialiser(node: DSTArrayInitialiser) {
        print("{")

        node.expressions.forEachIndexed { index, dstExpressionOrName ->
            dstExpressionOrName.accept(this)

            if (index < node.expressions.size-1)
                print(",")
        }

        print("}")
    }

    override fun visitDSTConditionalExpression(node: DSTConditionalExpression) {
        node.condition.accept(this)
        print(" ? ")
        node.thenExpression.accept(this)
        print(" : ")
        node.elseExpression.accept(this)
    }

    override fun visitDSTFieldAccessExpression(node: DSTFieldAccessExpression) {
        if (node.scope != null) {
            node.scope!!.accept(this)
            print(".")
        }

        (node.name as DSTName).accept(this)
    }

    override fun visitDSTSuperFieldAccessExpression(node: DSTSuperFieldAccessExpression) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitDSTThisExpression(node: DSTThisExpression) {
        if (node.qualifier != null) {
            print(node.qualifier!!.evaluate())
            print(".")
        }

        print("this")
    }

    override fun visitDSTInstanceOfExpression(node: DSTInstanceOfExpression) {
        node.expression.accept(this)
        print(" instanceof ")
        print(node.type.evaluate())
    }

    override fun visitDSTLambdaExpression(node: DSTLambdaExpression) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitDSTCreationRefExpression(node: DSTCreationRefExpression) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitDSTTypeMethodRefExpression(node: DSTTypeMethodRefExpression) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitDSTSuperMethodRefExpression(node: DSTSuperMethodRefExpression) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitDSTExpressionMethodRefExpression(node: DSTExpressionMethodRefExpression) {

        node.expression.accept(this)
        print("::")
        print(node.name.evaluate())

        if (node.typeParameters.isNotEmpty()) {
            print("<")
            node.typeParameters.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.typeParameters.size-1) {
                    print(", ")
                }
            }
            print(">")
        }
    }

    //

    override fun visitDSTAnonymousClassBody(node: DSTAnonymousClassBody) {
        println("{")
        node.declarations.forEach { it.accept(this) }
        println("}")
    }

    override fun visitDSTBlockStatement(node: DSTBlockStatement) {
        println("{")
        node.statements.forEach { it.accept(this) }
        println("}")
    }

    override fun visitDSTExpressionStatement(node: DSTExpressionStatement) {
        node.expression.accept(this)
        println(";")
    }

    override fun visitDSTLocalVariableDeclarationGroup(node: DSTLocalVariableDeclarationGroup) {
        print(node.type.evaluate())
        print(" ")
        node.variables.forEachIndexed { index, dstLocalVariableDeclaration ->
            dstLocalVariableDeclaration.accept(this)

            if (index < node.variables.size - 1)
                print(", ")
        }

        println(";")
    }

    override fun visitDSTLocalVariableDeclaration(node: DSTLocalVariableDeclaration) {
        print(node.name.evaluate())
        for (i in 0 until node.extraDimensions)
            print("[]")

        if (node.initialiser != null) {
            print(" = ")
            node.initialiser!!.accept(this)
        }
    }

    override fun visitDSTSingleVariableDeclaration(node: DSTSingleVariableDeclaration) {
        print(node.type.evaluate())
        print(" ")
        print(node.name.evaluate())
        for (i in 0 until node.extraDimensions)
            print("[]")

        if (node.initialiser != null) {
            print(" = ")
            node.initialiser!!.accept(this)
        }
    }

    override fun visitDSTWhileStatement(node: DSTWhileStatement) {
        println()
        print("while (")
        node.condition.accept(this)
        print(") ")
        node.body.accept(this)
    }

    override fun visitDSTDoWhileStatement(node: DSTDoWhileStatement) {
        println()
        print("do ")
        node.body.accept(this)
        print(" while (")
        node.condition.accept(this)
        println(");")
    }

    override fun visitDSTForStatement(node: DSTForStatement) {
        println()
        print("for (")
        node.initialisers.forEachIndexed { index, dstExpression ->
            dstExpression.accept(this)

            if (index < node.initialisers.size - 1)
                print(", ")
        }
        print(";")
        node.condition?.accept(this)
        print(";")
        node.updaters.forEachIndexed { index, dstExpression ->
            dstExpression.accept(this)

            if (index < node.updaters.size - 1)
                print(", ")
        }
        print(") ")
        node.body.accept(this)
    }

    override fun visitDSTForEachStatement(node: DSTForEachStatement) {
        print("for (")
        node.variable.accept(this)
        print(" : ")
        node.collection.accept(this)
        print(")")
        node.body.accept(this)
    }

    override fun visitDSTSuperMethodCall(node: DSTSuperMethodCall) {
        if (node.qualifier != null) {
            node.qualifier!!.accept(this)
            print(".")
        }
        print("super.")
        if (node.typeArguments.isNotEmpty()) {
            print("<")
            node.typeArguments.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.typeArguments.size - 1)
                    print(", ")
            }
            print(">")
        }
        print(node.name.evaluate())
//        node.name.accept(this)
        print("(")
        node.arguments.forEachIndexed { index, dstExpressionOrName ->
            dstExpressionOrName.accept(this)

            if (index < node.arguments.size - 1)
                print(", ")
        }
        print(")")
        println(";")
    }

    override fun visitDSTSuperConstructorCall(node: DSTSuperConstructorCall) {
        if (node.qualifier != null) {
            node.qualifier!!.accept(this)
            print(".")
        }
        if (node.typeArguments.isNotEmpty()) {
            print("<")
            node.typeArguments.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.typeArguments.size - 1)
                    print(", ")
            }
            print(">")
        }
        print("super(")
        node.arguments.forEachIndexed { index, dstExpressionOrName ->
            dstExpressionOrName.accept(this)

            if (index < node.arguments.size - 1)
                print(", ")
        }
        print(")")
        println(";")
    }

    override fun visitDSTThisConstructorCall(node: DSTThisConstructorCall) {
        if (node.typeArguments.isNotEmpty()) {
            print("<")
            node.typeArguments.forEachIndexed { index, dstType ->
                print(dstType.evaluate())

                if (index < node.typeArguments.size - 1)
                    print(", ")
            }
            print(">")
        }
        print("this(")
        node.arguments.forEachIndexed { index, dstExpressionOrName ->
            dstExpressionOrName.accept(this)

            if (index < node.arguments.size - 1)
                print(", ")
        }
        print(")")
        println(";")
    }

    override fun visitDSTSynchronisedStatement(node: DSTSynchronisedStatement) {
        println()
        print("synchronized (")
        node.monitor.accept(this)
        print(") ")
        node.body.accept(this)
    }

    override fun visitDSTLabeledStatement(node: DSTLabeledStatement) {
        print(node.label)
        print(": ")
        node.statement.accept(this)
    }

    override fun visitDSTBreakStatement(node: DSTBreakStatement) {
        print("break")
        if (node.label != null) {
            print(" ")
            node.label!!.accept(this)
        }
        println(";")
    }

    override fun visitDSTContinueStatement(node: DSTContinueStatement) {
        print("continue")
        if (node.label != null) {
            print(" ")
            node.label!!.accept(this)
        }
        println(";")
    }

    override fun visitDSTReturnStatement(node: DSTReturnStatement) {
        print("return")
        if (node.expression != null) {
            print(" ")
            node.expression!!.accept(this)
        }
        println(";")
    }

    override fun visitDSTThrowStatement(node: DSTThrowStatement) {
        print("throw ")
        node.exception.accept(this)
        println(";")
    }

    override fun visitDSTIfStatement(node: DSTIfStatement) {
        println()
        print("if (")
        node.condition.accept(this)
        print(") ")
        node.thenStatement.accept(this)

        if (node.elseStatement != null) {
            print("else ")
            node.elseStatement!!.accept(this)
        } else {
            println()
        }
    }

    override fun visitDSTSwitchStatement(node: DSTSwitchStatement) {
        println()
        print("switch (")
        node.expression.accept(this)
        println(") {")
        node.statements.forEach { it.accept(this) }
        println("}")
    }

    override fun visitDSTSwitchCase(node: DSTSwitchCase) {
        if (node.isDefault) {
            println("default:")
        } else {
            print("case ")
            node.expression!!.accept(this)
            println(":")
        }
    }

    override fun visitDSTTryStatement(node: DSTTryStatement) {
        println()
        print("try ")

        if (node.resources.isNotEmpty()) {
            println("(")

            node.resources.forEachIndexed { index, dstExpression ->
                dstExpression.accept(this)
                println(";")
            }

            print(") ")
        }

        node.body.accept(this)

        for (catch in node.catchClauses) {
            catch.accept(this)
        }

        if (node.finally != null) {
            print("finally ")
            node.finally!!.accept(this)
        }
    }

    override fun visitDSTCatchClause(node: DSTCatchClause) {
        print("catch (")
        node.exception.accept(this)
        print(") ")
        node.body.accept(this)
    }

    override fun visitDSTEmptyStatement(node: DSTEmptyStatement) {
        println(";")
    }

    override fun visitDSTTypeDeclarationStatement(node: DSTTypeDeclarationStatement) {
        node.type.accept(this)
        println()
    }

    override fun visitDSTAssertStatement(node: DSTAssertStatement) {
        print("assert(")
        node.expression.accept(this)
        print(", ")
        node.message.accept(this)
        println(");")
    }

    //  Names -> Should only be used for expressions

    override fun visitDSTKnownSimpleName(node: DSTKnownSimpleName) {
        print(node.evaluate())
    }

    override fun visitDSTDeferredSimpleName(node: DSTDeferredSimpleName) {
        val binding = node.binding
//        if (binding is IVariableBinding && binding.isEnumConstant) {
//
//            print(inject<DSTTypeFactory>().getTypeNameMapping(binding.declaringClass).evaluate())
//            print(".")
//        }

        print(node.evaluate())
    }

    override fun visitDSTQualifiedName(node: DSTQualifiedName) {
        print(node.evaluate())
    }

    //  Types

    override fun visitDSTSimpleType(node: DSTSimpleType) {
        throw NotImplementedError("types cannot be evaluated by visiting")
    }

    override fun visitDSTArrayType(node: DSTArrayType) {
        throw NotImplementedError("types cannot be evaluated by visiting")
    }

    override fun visitDSTParameterisedType(node: DSTParameterisedType) {
        throw NotImplementedError("types cannot be evaluated by visiting")
    }

    override fun visitDSTQualifiedType(node: DSTQualifiedType) {
        throw NotImplementedError("types cannot be evaluated by visiting")
    }

    override fun visitDSTNameQualifiedType(node: DSTNameQualifiedType) {
        throw NotImplementedError("types cannot be evaluated by visiting")
    }

    override fun visitDSTWildcardType(node: DSTWildcardType) {
        throw NotImplementedError("types cannot be evaluated by visiting")
    }

    override fun visitDSTUnionType(node: DSTUnionType) {
        throw NotImplementedError("types cannot be evaluated by visiting")
    }

    override fun visitDSTIntersectionType(node: DSTIntersectionType) {
        throw NotImplementedError("types cannot be evaluated by visiting")
    }

    override fun visitDSTTypeParameter(node: DSTTypeParameter) {
        throw NotImplementedError("types cannot be evaluated by visiting")
    }

    override fun visitDSTSyntheticField(dstSyntheticField: DSTSyntheticField) {
        print("private static final ")
        print(dstSyntheticField.type.evaluate())
        print(" ")
        dstSyntheticField.name.accept(this)
        print(" = ")
        dstSyntheticField.value.accept(this)
        print(";")
    }

    override fun visitDSTSyntheticName(dstSyntheticName: DSTSyntheticName) {
        print(dstSyntheticName.name)
    }
}