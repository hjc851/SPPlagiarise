package spplagiarise.obfuscation

import spplagiarise.dst.DSTCompilationUnit
import spplagiarise.dst.visitor.walk

class Obfuscator(
        val dstcus: List<DSTCompilationUnit>,
        val filters: List<DSTObfuscatorFilter>
) {
    fun run() {
        // TODO: Need to do some sort of analytics
        for (filter in filters) {
            for (cu in dstcus) {
                cu.walk(filter)
            }
        }
    }
}