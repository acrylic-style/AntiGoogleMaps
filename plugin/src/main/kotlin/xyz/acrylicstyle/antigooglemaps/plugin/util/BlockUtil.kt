package xyz.acrylicstyle.antigooglemaps.plugin.util

import com.gmail.nossr50.mcMMO
import org.bukkit.block.Block

object BlockUtil {
    fun Block.isTrue(): Boolean {
        return try {
            mcMMO.getPlaceStore().isTrue(this)
        } catch (e: NoClassDefFoundError) {
            false
        }
    }
}
