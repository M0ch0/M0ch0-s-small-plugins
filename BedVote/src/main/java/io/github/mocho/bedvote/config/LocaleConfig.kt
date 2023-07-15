package io.github.mocho.bedvote.config

import io.github.mocho.bedvote.localization.LocalePath
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin

class LocaleConfig : ConfigBase {
    constructor(plugin: Plugin, file: String) : super(plugin, file){
        super.saveDefaultConfig()
        localeConfig = super.getConfig()!!
    }
    fun reloadConfig(){
        localeConfig = super.getConfig()!!
    }

    companion object {
        private lateinit var localeConfig: FileConfiguration
        fun getLocaleString(context: LocalePath): String {
            return localeConfig.getString(context.path, context.path).toString()
        }
    }
}