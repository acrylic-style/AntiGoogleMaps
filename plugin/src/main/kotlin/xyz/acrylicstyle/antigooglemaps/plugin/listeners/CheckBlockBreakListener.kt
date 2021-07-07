package xyz.acrylicstyle.antigooglemaps.plugin.listeners

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.event.block.BlockBreakEvent
import xyz.acrylicstyle.antigooglemaps.plugin.AntiGoogleMaps
import xyz.acrylicstyle.antigooglemaps.plugin.util.BlockUtil.isTrue

object CheckBlockBreakListener: EventListener<BlockBreakEvent> {
    override fun handle(e: BlockBreakEvent) {
        if (e.player.gameMode != GameMode.SURVIVAL) return
        val period = AntiGoogleMaps.instance.config.getInt("blocks.${e.block.type.name}.period", 0)
        val threshold = AntiGoogleMaps.instance.config.getInt("blocks.${e.block.type.name}.threshold", 0)
        if (threshold == 0) return
        if (!e.block.isTrue() && ++AntiGoogleMaps.blocks[e.block.type][e.player.uniqueId] == threshold) {
            Bukkit.getOnlinePlayers().filter { it.hasPermission("antigooglemaps.notification") }.forEach { p ->
                p.sendMessage("${ChatColor.GOLD}[AntiGoogleMaps] ${ChatColor.AQUA}${e.player.name}${ChatColor.GOLD}が${ChatColor.GREEN}${e.block.type.name.lowercase()}${ChatColor.GOLD}を${period}分間に${threshold}個破壊しました。")
            }
        }
    }
}
