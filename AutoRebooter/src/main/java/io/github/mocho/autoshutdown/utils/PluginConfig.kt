package io.github.mocho.autoshutdown.utils

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin

class PluginConfig : ConfigBase {

    private var pluginConfig: FileConfiguration
    constructor(plugin: Plugin, file: String): super(plugin, file){
        super.saveDefaultConfig()
        pluginConfig = super.getConfig()!!
    }
    fun getLanguageFile() : String {
        return when(val langRaw = pluginConfig.getString("lang", "en")){
            "ja_JP" -> "ja_JP.yml"
            "en_GB" -> "en_GB.yml"
            else -> {
                Bukkit.getLogger().warning("The language: %s is not supported. Uses English instead.".format(langRaw))
                "en_GB.yml"
            }
        }
    }
    fun getTickThreshold(): Double {
        return pluginConfig.getDouble("tick-threshold", 9.5)
    }
    fun getShutdownDelayTime(): Long {
        return pluginConfig.getLong("shutdown-delay-time", 60)
    }
    fun getShutdownCountdownTime() : Int {
        return pluginConfig.getInt("shutdown-countdown-time", 10)
    }
    fun getCheckInterval(): Long {
        return pluginConfig.getLong("check-interval", 180)
    }
    fun getTickReference(): Int {
        if(pluginConfig.getInt("tick-reference", 1) in 0..2){
            return pluginConfig.getInt("tick-reference")
        }
        return 1
    }
    fun getRecoveryRecover(): Boolean{
        return pluginConfig.getBoolean("tick-recovery-recover", true)
    }
    fun getRecoveryThreshold(): Double{
        return pluginConfig.getDouble("tick-recovery-threshold", 15.5)
    }
    fun getRecoveryRecoverDelay(): Boolean{
        return pluginConfig.getBoolean("tick-recovery-recover-delay", false)
    }
    fun getRecoveryRecoverDelayTime(): Long{
        return pluginConfig.getLong("recovery-recover-delay-time", 60)
    }

}