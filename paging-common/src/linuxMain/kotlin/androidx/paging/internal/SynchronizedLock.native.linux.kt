package androidx.paging.internal

import platform.posix.*
import kotlinx.cinterop.*
import kotlin.native.identityHashCode

@OptIn(ExperimentalForeignApi::class)
internal actual class SynchronizedLockNativeImpl actual constructor() {
    // Allocation on the native heap to ensure it survives outside the stack
    @OptIn(ExperimentalForeignApi::class)
    private val mutex = nativeHeap.alloc<pthread_mutex_t>()

    init {
        memScoped {
            val attr = alloc<pthread_mutexattr_t>()
            pthread_mutexattr_init(attr.ptr)
            // Set as RECURSIVE to match JVM/Android synchronized behavior
            pthread_mutexattr_settype(attr.ptr, PTHREAD_MUTEX_RECURSIVE)
            pthread_mutex_init(mutex.ptr, attr.ptr)
            pthread_mutexattr_destroy(attr.ptr)
        }
    }

    internal actual fun lock(): Int {
        return pthread_mutex_lock(mutex.ptr)
    }

    internal actual fun unlock(): Int {
        return pthread_mutex_unlock(mutex.ptr)
    }

    internal actual fun destroy() {
        pthread_mutex_destroy(mutex.ptr)
        nativeHeap.free(mutex) // Free the memory allocated for the mutex
    }
}