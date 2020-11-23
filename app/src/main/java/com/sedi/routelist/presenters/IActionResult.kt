package com.sedi.routelist.presenters

import java.lang.Exception

interface IActionResult {
    fun result(result: Any?, exception: Exception?)
}