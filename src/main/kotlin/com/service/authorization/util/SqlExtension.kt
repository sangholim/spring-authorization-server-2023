package com.service.authorization.util

fun String.sqlValue(): String = "'$this'"

fun Collection<String>.sqlValues(): String = this.joinToString(separator = ",") { it.sqlValue() }