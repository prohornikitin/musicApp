package com.example.musicapp.logic.pure.logic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val f = flow {
        for (i in 1..100) {
            println("emit $i")
            emit(i)
            delay(100)
        }
    }
    val scope = CoroutineScope(Dispatchers.Default)
    runBlocking {
        val l = f
        scope.launch {
            l.collect {
                println(it)
            }
        }
        delay(300)
        scope.cancel()
        delay(10000)
    }
}