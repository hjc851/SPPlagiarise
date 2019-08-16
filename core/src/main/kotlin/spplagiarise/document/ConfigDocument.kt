package spplagiarise.document

class ConfigDocument(
        val input: String,
        val output: String,
        val libs: String,
        val copies: Int,
        val extreme: Boolean,
        val seed: Long?
)

