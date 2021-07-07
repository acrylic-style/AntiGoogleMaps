package xyz.acrylicstyle.antigooglemaps.plugin.listeners

import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

interface EventListener<T: Event>: Listener {
    fun handle(e: T)

    @EventHandler
    fun doHandle(e: T) = handle(e)
}
