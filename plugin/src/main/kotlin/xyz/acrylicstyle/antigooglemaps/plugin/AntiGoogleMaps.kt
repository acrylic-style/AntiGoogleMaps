package xyz.acrylicstyle.antigooglemaps.plugin

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import xyz.acrylicstyle.antigooglemaps.plugin.command.AntiGoogleMapsCommand
import xyz.acrylicstyle.antigooglemaps.plugin.listeners.CheckBlockBreakListener
import xyz.acrylicstyle.antigooglemaps.plugin.util.KVMap
import xyz.acrylicstyle.antigooglemaps.plugin.util.Util
import java.io.File
import java.util.UUID

class AntiGoogleMaps: JavaPlugin() {
    companion object {
        lateinit var instance: AntiGoogleMaps
        val blocks: KVMap<Material, KVMap<UUID, Int>> = KVMap { KVMap { 0 } }
    }

    private val currentTasks = ArrayList<BukkitTask>()

    init {
        instance = this
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(CheckBlockBreakListener, this)
        getCommand("antigooglemaps")?.let {
            it.tabCompleter = AntiGoogleMapsCommand
            it.setExecutor(AntiGoogleMapsCommand)
        }
        startTask()
    }

    override fun onDisable() {
        config.save(File("./plugins/AntiGoogleMaps/config.yml"))
    }

    fun startTask() {
        currentTasks.forEach { if (!it.isCancelled) it.cancel() }
        currentTasks.clear()
        val map = Util.getConfigSectionValue(config.get("blocks"), true)
        map?.forEach { (key, _) ->
            val period = map["$key.period"]
            if (period !is Number) return
            val threshold = map["$key.threshold"]
            logger.info("${ChatColor.YELLOW}- ${ChatColor.AQUA}$key${ChatColor.YELLOW}: ${ChatColor.AQUA}Period: $period, Threshold: $threshold")
            Material.values().find { it.name == key.uppercase() }?.let { material ->
                val task = server.scheduler.runTaskTimer(this, Runnable {
                    blocks[material].clear()
                }, 0L, 20L * 60L * period.toLong())
                currentTasks.add(task)
            }
        }
    }
}
