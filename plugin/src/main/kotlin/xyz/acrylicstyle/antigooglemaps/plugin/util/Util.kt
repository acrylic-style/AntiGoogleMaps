package xyz.acrylicstyle.antigooglemaps.plugin.util

import org.bukkit.configuration.ConfigurationSection

object Util {
    fun getConfigSectionValue(o: Any?, deep: Boolean): Map<String, Any?>? {
        if (o == null) return null
        if (o is ConfigurationSection) {
            return o.getValues(deep)
        } else if (o is Map<*, *>) {
            @Suppress("UNCHECKED_CAST")
            return o as Map<String, Any?>
        }
        return null
    }
}
