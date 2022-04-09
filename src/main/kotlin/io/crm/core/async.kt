package io.crm.core

import io.vertx.core.Future
import kotlinx.coroutines.*

class Reader1Kt<K, V, A> {
    suspend fun read(key: K, arg: A): V? {
        delay(100L)
        return null
    }

}

fun <K, V, A> readAsync(reader1: Reader1Kt<K, V, A>, key: K, arg: A): Future<V> {
    return Future.future { p ->
        val res = CoroutineScope(Dispatchers.Default).async {
            reader1.read(key, arg)
        }
        res.invokeOnCompletion {
            if (it != null) {
                p.fail(it)
            } else {
                @Suppress("EXPERIMENTAL_API_USAGE")
                p.complete(res.getCompleted())
            }
        }
    }
}