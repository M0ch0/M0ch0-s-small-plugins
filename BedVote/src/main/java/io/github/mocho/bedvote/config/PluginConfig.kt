package io.github.mocho.bedvote.config

import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin

class PluginConfig : ConfigBase {
    constructor(plugin: Plugin, file: String) : super(plugin, file){
        super.saveDefaultConfig()
        systemConfig = super.getConfig()!!
    }

    fun reloadConfig(){
        systemConfig = super.getConfig()!!
    }

    companion object{
        private lateinit var systemConfig: FileConfiguration
        fun getLanguageFile() : String {
            return when(val langRaw = systemConfig.getString("lang", "en")){
                "ja_JP" -> "ja_JP.yml"
                "en_GB" -> "en_GB.yml"
                else -> {
                    Bukkit.getLogger().warning("The language: %s is not supported. Uses English instead.".format(langRaw))
                    "en_GB.yml"
                }
            }
        }

        fun getAutoStartEnabled() : Boolean {
            return systemConfig.getBoolean("vote.autostart", false)
        }

        fun getBossbarShow() : Boolean {
            return systemConfig.getBoolean("boss-bar.show", false)
        }

        fun getBossbarColor() : BarColor {
            return when(systemConfig.getString("boss-bar.color")){
                "BLUE" -> BarColor.BLUE
                "GREEN" -> BarColor.GREEN
                "PINK" -> BarColor.PINK
                "PURPLE" -> BarColor.PURPLE
                "WHITE" -> BarColor.WHITE
                "YELLOW" -> BarColor.YELLOW
                else -> BarColor.BLUE
            }
        }

        fun getBossbarStyle() : BarStyle {
            return when(systemConfig.getString("boss-bar.style")){
                "SEGMENTED_6" -> BarStyle.SEGMENTED_6
                "SEGMENTED_10" -> BarStyle.SEGMENTED_10
                "SEGMENTED_12" -> BarStyle.SEGMENTED_12
                "SEGMENTED_20" -> BarStyle.SEGMENTED_20
                else -> BarStyle.SEGMENTED_10
            }
        }

        fun getVoteTimeLimit() : Long {
            return systemConfig.getLong("vote.time-limit", 60L)
        }

        fun getRequiredAgreeRatio() : Int {
            return systemConfig.getInt("vote.required-agree-ratio", 2)
        }

        fun getRequiredDeclineRatio() : Int {
            return systemConfig.getInt("vote.required-decline-ratio", 2)
        }


        fun getPhantomSpawnTimerResetTarget() : String {
            return when(systemConfig.getString("vote.phantom-spawn-timer-reset-target", "agreed")){
                "agreed" -> "agreed"
                "all" -> "all"
                "none" -> "none"
                else -> "agreed"
            }
        }

    }
}