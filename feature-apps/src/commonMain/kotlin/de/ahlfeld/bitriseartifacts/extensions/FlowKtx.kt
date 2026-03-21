package de.ahlfeld.bitriseartifacts.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

private const val ANR_TIMEOUT = 5_000L

fun <T> Flow<T>.stateInWhileSubscribed(
    scope : CoroutineScope,
    initialValue : T,
    stopTimeoutMillis: Long = ANR_TIMEOUT
) : StateFlow<T> {
    return stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis),
        initialValue = initialValue,
    )
}