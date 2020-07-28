package spplagiarise.obfuscation.util

import spplagiarise.dst.*
import spplagiarise.dst.visitor.DSTNameEvaluator
import spplagiarise.dst.visitor.DSTTypeEvaluator
import spplagiarise.dst.visitor.evaluate
import spplagiarise.naming.DeferredNameContext

enum class TypeRewriteStrategyType(val strategyFactory: (DeferredNameContext) -> TypeRewriteStrategy) {

    QUALIFY({ nameContext -> QualifyTypeRewriteStrategy(nameContext) }),
    SIMPLIFY({ nameContext -> SimplifyTypeRewriteStrategy(nameContext) }),
    NONE({ nameContext -> NoneTypeRewriteStrategy(nameContext) });

    object Visitor : DSTTypeEvaluator<TypeRewriteStrategyType, Unit>, DSTNameEvaluator<TypeRewriteStrategyType, Unit> {

        //  DSTTypeEvaluator

        override fun evaluateDSTSimpleType(node: DSTSimpleType, context: Unit): TypeRewriteStrategyType {
            return node.name.evaluate(this)
        }

        override fun evaluateDSTQualifiedType(node: DSTQualifiedType, context: Unit): TypeRewriteStrategyType {
            return SIMPLIFY
        }

        override fun evaluateDSTNameQualifiedType(node: DSTNameQualifiedType, context: Unit): TypeRewriteStrategyType {
            return SIMPLIFY
        }

        override fun evaluateDSTArrayType(node: DSTArrayType, context: Unit): TypeRewriteStrategyType {
            return node.typeName.evaluate(this)
        }

        override fun evaluateDSTParameterisedType(node: DSTParameterisedType, context: Unit): TypeRewriteStrategyType {
            return node.baseType.evaluate(this)
        }

        override fun evaluateDSTWildcardType(node: DSTWildcardType, context: Unit): TypeRewriteStrategyType {
            return NONE
        }

        override fun evaluateDSTUnionType(node: DSTUnionType, context: Unit): TypeRewriteStrategyType {
            return node.types.first().evaluate(this)
        }

        override fun evaluateDSTIntersectionType(node: DSTIntersectionType, context: Unit): TypeRewriteStrategyType {
            return node.types.first().evaluate(this)
        }

        override fun evaluateDSTTypeParameter(node: DSTTypeParameter, context: Unit): TypeRewriteStrategyType {
            return NONE
        }

        //  DSTNameEvaluator

        override fun evaluateDSTKnownSimpleName(node: DSTKnownSimpleName, context: Unit): TypeRewriteStrategyType {
            return QUALIFY
        }

        override fun evaluateDSTDeferredSimpleName(node: DSTDeferredSimpleName, context: Unit): TypeRewriteStrategyType {
            return QUALIFY
        }

        override fun evaluateDSTQualifiedName(node: DSTQualifiedName, context: Unit): TypeRewriteStrategyType {
            return SIMPLIFY
        }
    }
}

