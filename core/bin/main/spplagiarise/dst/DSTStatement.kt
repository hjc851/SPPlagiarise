package spplagiarise.dst

import org.eclipse.jdt.core.dom.IMethodBinding

//
//  Statements
//

abstract class DSTStatement : DSTNode()

//  Wrappers

class DSTBlockStatement(
        var statements: List<DSTStatement>
) : DSTStatement() {
    init {
        statements.assignParent()
    }
}

class DSTExpressionStatement(var expression: DSTExpression) : DSTStatement() {
    init {
        expression.parent = this
    }
}

//  Variables

class DSTLocalVariableDeclarationGroup(
        var variables: List<DSTLocalVariableDeclaration>,
        var type: DSTType
) : DSTStatement() {
    init {
        variables.assignParent()
        type.parent = this
    }
}

class DSTLocalVariableDeclaration(
        var name: DSTSimpleName,
        var extraDimensions: Int,
        var initialiser: DSTExpressionOrName?
) : DSTStatement() {
    init {
        name.parent = this
        initialiser?.parent = this
    }
}

class DSTSingleVariableDeclaration(
        var type: DSTType,
        var name: DSTDeferredSimpleName,
        var extraDimensions: Int,
        var initialiser: DSTExpressionOrName?
) : DSTStatement() {
    init {
        type.parent = this
        name.parent = this
        initialiser?.parent = this
    }
}

//  Loops

class DSTWhileStatement(
        var condition: DSTExpressionOrName,
        var body: DSTStatement
) : DSTStatement() {
    init {
        condition.parent = this
        body.parent = this
    }
}

class DSTDoWhileStatement(
        var condition: DSTExpressionOrName,
        var body: DSTStatement
) : DSTStatement() {
    init {
        condition.parent = this
        body.parent = this
    }
}

class DSTForStatement(
        var initialisers: List<DSTExpression>,
        var condition: DSTExpressionOrName?,
        var updaters: List<DSTExpression>,
        var body: DSTStatement
) : DSTStatement() {
    init {
        initialisers.assignParent()
        condition?.parent = this
        updaters.assignParent()
        body.parent = this
    }
}

class DSTForEachStatement(
        var variable: DSTSingleVariableDeclaration,
        var collection: DSTExpressionOrName,
        var body: DSTStatement
) : DSTStatement() {
    init {
        variable.parent = this
        collection.parent = this
        body.parent = this
    }
}

//  Constructors

class DSTSuperMethodCall(
        var binding: IMethodBinding,
        var name: DSTName,
        var qualifier: DSTName?,
        var arguments: List<DSTExpressionOrName>,
        var typeArguments: List<DSTType>
) : DSTStatement() {
    init {
        name?.parent = this
        qualifier?.parent = this
        arguments.assignParent()
        typeArguments.assignParent()
    }
}

class DSTSuperConstructorCall(
        var binding: IMethodBinding,
        var qualifier: DSTExpressionOrName?,
        var arguments: List<DSTExpressionOrName>,
        var typeArguments: List<DSTType>
) : DSTStatement() {
    init {
        qualifier?.parent = this
        arguments.assignParent()
        typeArguments.assignParent()
    }
}

class DSTThisConstructorCall(
        var binding: IMethodBinding,
        var arguments: List<DSTExpressionOrName>,
        var typeArguments: List<DSTType>
) : DSTStatement() {
    init {
        arguments.assignParent()
        typeArguments.assignParent()
    }
}

//  Synchronisation

class DSTSynchronisedStatement(
        var monitor: DSTExpressionOrName,
        var body: DSTStatement
) : DSTStatement() {
    init {
        body.parent = this
    }
}

//  Control Flow

class DSTLabeledStatement(
        var label: String,
        var statement: DSTStatement
) : DSTStatement() {
    init {
        statement.parent = this
    }
}

class DSTBreakStatement(var label: DSTName?) : DSTStatement() {
    init {
        label?.parent = this
    }
}

class DSTContinueStatement(var label: DSTName?) : DSTStatement() {
    init {
        label?.parent = this
    }
}

class DSTReturnStatement(
        var expression: DSTExpressionOrName?
) : DSTStatement() {
    init {
        expression?.parent = this
    }
}

class DSTThrowStatement(
        var exception: DSTExpressionOrName
) : DSTStatement() {
    init {
        exception.parent = this
    }
}

//  Branching

class DSTIfStatement(
        var condition: DSTExpressionOrName,
        var thenStatement: DSTStatement,
        var elseStatement: DSTStatement?
) : DSTStatement() {
    init {
        thenStatement.parent = this
        elseStatement?.parent = this
    }
}

class DSTSwitchStatement(
        var expression: DSTExpressionOrName,
        var statements: List<DSTStatement>
) : DSTStatement() {
    init {
        expression.parent = this
        statements.assignParent()
    }
}

class DSTSwitchCase(
        var expression: DSTExpressionOrName?,
        var isDefault: Boolean
) : DSTStatement() {
    init {
        expression?.parent = this
    }
}

//  Exception Handling

class DSTTryStatement(
        var resources: List<DSTExpression>,
        var body: DSTStatement,
        var catchClauses: List<DSTCatchClause>,
        var finally: DSTStatement?
) : DSTStatement() {
    init {
        resources.assignParent()
        body.parent = this
        catchClauses.assignParent()
        finally?.parent = this
    }
}

class DSTCatchClause(
        var exception: DSTSingleVariableDeclaration,
        var body: DSTStatement
) : DSTNode() {
    init {
        exception.parent = this
        body.parent = this
    }
}

//  Others

class DSTEmptyStatement : DSTStatement()

class DSTTypeDeclarationStatement(
        var type: DSTTypeDeclaration
) : DSTStatement() {
    init {
        type.parent = this
    }
}

class DSTAssertStatement(
        var message: DSTExpressionOrName,
        var expression: DSTExpressionOrName
) : DSTStatement() {
    init {
        message.parent = this
        expression.parent = this
    }
}
