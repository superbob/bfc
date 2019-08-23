package eu.superbob.bfc

/**
 * Unlimited memory space that will span from 0 to [Int.MAX_VALUE] as long as memory is physically available.
 *
 * Memory is lazily allocated through a [MutableList] whose values are added as needed.
 *
 * Memory is represented as signed integers.
 */
class MutableMemory {
    private val storage = mutableListOf<Int>()

    operator fun get(index: Int): Int {
        lazyInitMemory(index)
        return storage[index]
    }

    operator fun set(index: Int, value: Int) {
        lazyInitMemory(index)
        storage[index] = value
    }

    private fun lazyInitMemory(requiredSize: Int) {
        while (storage.size <= requiredSize) storage.add(0)
    }
}
