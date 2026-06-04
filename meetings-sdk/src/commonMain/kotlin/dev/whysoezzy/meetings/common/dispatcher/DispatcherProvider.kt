package dev.whysoezzy.meetings.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

expect fun mainDispatcher(): CoroutineDispatcher

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}
