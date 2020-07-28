package spplagiarise.dst

import org.eclipse.jdt.core.dom.ITypeBinding

//
//  Literals
//

abstract class DSTLiteral : DSTExpression()

class DSTStringLiteral(
        override var typeBinding: ITypeBinding,
        var value: String
) : DSTLiteral()

class DSTCharLiteral(
        override var typeBinding: ITypeBinding,
        var value: String
) : DSTLiteral()

class DSTBooleanLiteral(
        override var typeBinding: ITypeBinding,
        var value: Boolean
) : DSTLiteral()

class DSTNumberLiteral(
        override var typeBinding: ITypeBinding,
        var value: String
) : DSTLiteral()

class DSTTypeLiteral(
        override var typeBinding: ITypeBinding,
        var type: DSTType
) : DSTLiteral()

class DSTNullLiteral(override var typeBinding: ITypeBinding) : DSTLiteral()