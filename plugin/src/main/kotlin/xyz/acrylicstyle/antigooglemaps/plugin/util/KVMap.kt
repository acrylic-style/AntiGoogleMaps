package xyz.acrylicstyle.antigooglemaps.plugin.util

import org.jetbrains.annotations.Contract
import java.util.function.Function

class KVMap<K, V>(private val constructor: Function<K, V>) : HashMap<K, V>() {
    @Contract("_ -> fail")
    override operator fun get(key: K): V {
        if (key == null) throw NullPointerException("key cannot be null")
        if (!this.containsKey(key)) super.put(key, constructor.apply(key))
        return super.get(key)!!
    }
}
