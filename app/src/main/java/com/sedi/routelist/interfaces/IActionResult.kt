package com.sedi.routelist.interfaces

import java.lang.Exception

interface IActionResult {
    fun result(result: Any?, exception: Exception?)
}