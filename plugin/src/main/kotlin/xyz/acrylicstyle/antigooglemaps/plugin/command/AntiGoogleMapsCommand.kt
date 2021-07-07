package xyz.acrylicstyle.antigooglemaps.plugin.command

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import xyz.acrylicstyle.antigooglemaps.plugin.AntiGoogleMaps
import xyz.acrylicstyle.antigooglemaps.plugin.util.Util
import java.util.Collections
import kotlin.math.round

object AntiGoogleMapsCommand: TabExecutor {
    private val commands = listOf("set", "reset", "info")

    private fun CommandSender.sendHelp(label: String) {
        this.sendMessage("${ChatColor.AQUA}/$label set <Material> <period (in minutes): Int> <threshold: Int> ${ChatColor.GRAY}- ${ChatColor.YELLOW}特定のブロックに対する設定を変更")
        this.sendMessage("${ChatColor.YELLOW} -> <Material>に一致したブロックを<period in minutes>分で<threshold>個以上掘った場合は通知")
        this.sendMessage("${ChatColor.AQUA}/$label reset ${ChatColor.GRAY}- ${ChatColor.YELLOW}設定をすべてリセット")
        this.sendMessage("${ChatColor.YELLOW} -> すべての通知設定を削除")
        this.sendMessage("${ChatColor.AQUA}/$label info ${ChatColor.GRAY}- ${ChatColor.YELLOW}現在の設定を表示")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sender.sendHelp(label)
            return true
        }
        try {
            when (args[0]) {
                "set" -> {
                    if (args.size <= 3) throw IllegalArgumentException("Material/Period/Thresholdが指定されていません")
                    val type = Material.valueOf(args[1].uppercase())
                    val period = args[2].toInt()
                    val threshold = args[3].toInt()
                    AntiGoogleMaps.instance.config.set("blocks.${type.name}.period", period)
                    AntiGoogleMaps.instance.config.set("blocks.${type.name}.threshold", threshold)
                    sender.sendMessage("${ChatColor.YELLOW}${ChatColor.AQUA}$type${ChatColor.YELLOW}: ${ChatColor.AQUA}Period: ${ChatColor.GOLD}$period${ChatColor.AQUA}, Threshold: ${ChatColor.GOLD}$threshold${ChatColor.AQUA} (だいたい${round(threshold.toDouble() / period.toDouble() * 10) / 10.0}/分)")
                    AntiGoogleMaps.instance.startTask()
                }
                "reset" -> {
                    if (args.size >= 2) {
                        val type = Material.valueOf(args[1].uppercase())
                        AntiGoogleMaps.instance.config.set("blocks.${type.name}", null)
                        AntiGoogleMaps.instance.startTask()
                        sender.sendMessage("${ChatColor.YELLOW}${type.name.lowercase()}の設定をリセットしました。")
                        return true
                    }
                    AntiGoogleMaps.instance.config.set("blocks", null)
                    AntiGoogleMaps.instance.startTask()
                    sender.sendMessage("${ChatColor.YELLOW}設定をすべてリセットしました。")
                }
                "info" -> {
                    val map = Util.getConfigSectionValue(AntiGoogleMaps.instance.config.get("blocks"), true)
                    map?.forEach { (key, _) ->
                        val period = map["$key.period"]
                        val threshold = map["$key.threshold"]
                        if (period == null || threshold == null) return@forEach
                        sender.sendMessage("${ChatColor.YELLOW}- ${ChatColor.AQUA}$key${ChatColor.YELLOW}: ${ChatColor.AQUA}Period: ${ChatColor.GOLD}$period${ChatColor.AQUA}, Threshold: ${ChatColor.GOLD}$threshold")
                    }
                }
                else -> sender.sendHelp(label)
            }
        } catch (e: IllegalArgumentException) {
            sender.sendMessage("${ChatColor.RED}${e.message}")
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()
        try {
            commands.filter(args[0], true).let { if (args.size == 1) return it }
            when (args[0]) {
                "reset",
                "set" -> {
                    if (args.size == 2) {
                        return if (args[1].isEmpty()) {
                            Material.values().map { it.name }
                        } else {
                            Material.values().map { it.name }.filter(args[1], true)
                        }
                    }
                }
            }
            return emptyList()
        } catch (e: IllegalArgumentException) {
            return Collections.singletonList("${ChatColor.RED}${e.message}")
        }
    }

    private fun List<String>.filter(s: String, noError: Boolean = false): List<String> {
        val list = this.filter { s1 -> s1.lowercase().startsWith(s.lowercase()) }
        if (!noError && list.isEmpty()) throw IllegalArgumentException("Unknown action '$s'")
        return list
    }
}
