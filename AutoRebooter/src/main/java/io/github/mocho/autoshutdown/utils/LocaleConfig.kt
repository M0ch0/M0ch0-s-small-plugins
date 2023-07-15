package io.github.mocho.autoshutdown.utils

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin

class LocaleConfig : ConfigBase {
    constructor(plugin: Plugin, file: String): super(plugin, file){
        super.saveDefaultConfig()
        this.localeConfig = super.getConfig()!!
    }
    private var localeConfig: FileConfiguration

    fun getLocaleString(context: LocalePath): String {
        return localeConfig.getString(context.path, context.path).toString()
    }

}