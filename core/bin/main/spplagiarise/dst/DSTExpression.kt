package spplagiarise.dst

import org.eclipse.jdt.core.dom.*

//
//  Expressions
//

abstract class DSTExpressionOrName : DSTNode()

abstract class DSTExpression : DSTExpressionOrName() {
    abstract var typeBinding: ITypeBinding
}

class DSTMethodCall(
        override var typeBinding: ITypeBinding,
        var name: DSTName,
        var arguments: List<DSTExpressionOrName>,
        var typeArguments: List<DSTType>,
        var expression: DSTExpressionOrName?,
        val binding: IMethodBinding
) : DSTExpression() {
    init {
        name.parent = this
        arguments.assignParent()
        typeArguments.assignParent()
        expression?.parent = this
    }
}

class DSTConstructorCall(
        override var typeBinding: ITypeBinding,
        var methodBinding: IMethodBinding,
        var type: DSTType,
        var arguments: List<DSTExpressionOrName>,
        var typeArguments: List<DSTType>,
        var anonymousClassBody: DSTAnonymousClassBody?,
        var expression: DSTExpressionOrName?
) : DSTExpression() {
    init {
        type.parent = this
        arguments.assignParent()
        typeArguments.assignParent()
        anonymousClassBody?.parent = this
        expression?.parent = this
    }
}

class DSTAnonymousClassBody(
        var type: ITypeBinding,
        var declarations: List<DSTBodyDeclaration>
) : DSTNode() {
    init {
        declarations.assignParent()
    }
}

class DSTAssignment(
        override var typeBinding: ITypeBinding,
        var operator: Assignment.Operator,
        var assignee: DSTExpressionOrName,
        var expression: DSTExpressionOrName
) : DSTExpression() {
    init {
        assignee.parent = this
        expression.parent = this
    }
}

class DSTPrefixExpression(
        override var typeBinding: ITypeBinding,
        var operator: PrefixExpression.Operator,
        var expression: DSTExpressionOrName
) : DSTExpression() {
    init {
        expression.parent = this
    }
}

abstract class DSTInfixExpression() : DSTExpression()

class DSTMultiInfixExpression(
        override var typeBinding: ITypeBinding,
        var operator: InfixExpression.Operator,
        var expressions: List<DSTExpressionOrName>
) : DSTInfixExpression() {
    init {
        expressions.assignParent()
    }
}

class DSTSingleInfixExpression(
        override var typeBinding: ITypeBinding,
        var operator: InfixExpression.Operator,
        var lhs: DSTExpressionOrName,
        var rhs: DSTExpressionOrName
) : DSTInfixExpression() {
    init {
        lhs.parent = this
        rhs.parent = this
    }
}

class DSTPostfixExpression(
        override var typeBinding: ITypeBinding,
        var operator: PostfixExpression.Operator,
        var expression: DSTExpressionOrName
) : DSTExpression() {
    init {
        expression.parent = this
    }
}

class DSTVariableDeclarationExpression(
        override var typeBinding: ITypeBinding,
        var type: DSTType,
        var variables: List<DSTLocalVariableDeclaration>
) : DSTExpression() {
    init {
        type.parent = this
        variables.assignParent()
    }
}

class DSTArrayAccess(
        override var typeBinding: ITypeBinding,
        var array: DSTExpressionOrName,
        var index: DSTExpressionOrName
) : DSTExpression() {
    init {
        array.parent = this
        index.parent = this
    }
}

class DSTParenthesisedExpression(
        override var typeBinding: ITypeBinding,
        var expr: DSTExpressionOrName
) : DSTExpression() {
    init {
        expr.parent = this
    }
}

class DSTCastExpression(
        override var typeBinding: ITypeBinding,
        var expression: DSTNode,
        var type: DSTType
) : DSTExpression() {
    init {
        expression.parent = this
        type.parent = this
    }
}

class DSTArrayCreation(
        override var typeBinding: ITypeBinding,
        var type: DSTArrayType,
        var dimensions: List<DSTExpressionOrName>,
        var initialiser: DSTArrayInitialiser?
) : DSTExpression() {
    init {
        type.parent = this
        dimensions.assignParent()
        initialiser?.parent = this
    }
}

class DSTArrayInitialiser(
        override var typeBinding: ITypeBinding,
        var expressions: List<DSTExpressionOrName>
) : DSTExpression() {
    init {
        expressions.assignParent()
    }
}

class DSTConditionalExpression(
        override var typeBinding: ITypeBinding,
        var condition: DSTExpressionOrName,
        var thenExpression: DSTExpressionOrName,
        var elseExpression: DSTExpressionOrName
) : DSTExpression() {
    init {
        condition.parent = this
        thenExpression.parent = this
        elseExpression.parent = this
    }
}

class DSTFieldAccessExpression(
        override var typeBinding: ITypeBinding,
        var scope: DSTExpressionOrName?,
        var name: DSTSimpleName
) : DSTExpression() {
    init {
        scope?.parent = this
        name.parent = this
    }
}

class DSTSuperFieldAccessExpression(
        override var typeBinding: ITypeBinding,
        var qualifier: DSTName?,
        var name: DSTSimpleName
) : DSTExpression() {
    init {
        qualifier?.parent = this
        name.parent = this
    }
}

class DSTThisExpression(
        override var typeBinding: ITypeBinding,
        var qualifier: DSTName?
) : DSTExpression() {
    init {
        qualifier?.parent = this
    }
}

class DSTInstanceOfExpression(
        override var typeBinding: ITypeBinding,
        var expression: DSTExpressionOrName,
        var type: DSTType
) : DSTExpression() {
    init {
        expression.parent = this
        type.parent = this
    }
}

class DSTLambdaExpression(
        override var typeBinding: ITypeBinding,
        var parameters: List<DSTNode>,
        var body: DSTNode
) : DSTExpression() {
    init {
        parameters.assignParent()
        body.parent = this
    }
}


class DSTCreationRefExpression(
        override var typeBinding: ITypeBinding,
        var type: DSTType,
        var typeParameters: List<DSTType>
) : DSTExpression() {
    init {
        type.parent = this
        typeParameters.assignParent()
    }
}

class DSTTypeMethodRefExpression(
        override var typeBinding: ITypeBinding,
        var type: DSTType,
        var typeParameters: List<DSTType>,
        var name: DSTSimpleName
) : DSTExpression() {
    init {
        type.parent = this
        name.parent = this
    }
}

class DSTSuperMethodRefExpression(
        override var typeBinding: ITypeBinding,
        var qualifier: DSTName?,
        var typeParameters: List<DSTType>,
        var name: DSTSimpleName
) : DSTExpression() {
    init {
        qualifier?.parent = this
        name.parent = this
    }
}

class DSTExpressionMethodRefExpression(
        override var typeBinding: ITypeBinding,
        var expression: DSTExpressionOrName,
        var typeParameters: List<DSTType>,
        var name: DSTSimpleName
) : DSTExpression() {
    init {
        expression.parent = this
        name.parent = this
    }
}

