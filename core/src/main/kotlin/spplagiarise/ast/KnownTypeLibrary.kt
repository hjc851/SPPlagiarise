package spplagiarise.ast

import org.eclipse.jdt.core.dom.ITypeBinding
import javax.inject.Singleton

@Singleton
class KnownTypeLibrary {
    lateinit var stringType: ITypeBinding
    lateinit var charType: ITypeBinding
    lateinit var intType: ITypeBinding
    lateinit var doubleType: ITypeBinding
    lateinit var voidType: ITypeBinding
    lateinit var booleanType: ITypeBinding
}