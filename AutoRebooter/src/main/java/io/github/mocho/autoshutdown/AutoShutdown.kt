package io.github.mocho.autoshutdown

/* (c) Mocho */

import net.minecraft.server.MinecraftServer
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import io.github.mocho.autoshutdown.utils.LocaleConfig
import io.github.mocho.autoshutdown.utils.LocalePath
import io.github.mocho.autoshutdown.utils.PluginConfig

class AutoShutdown : JavaPlugin() {
    private lateinit var pluginConfig: PluginConfig
    private lateinit var localeConfig: LocaleConfig
    private lateinit var tpsChecker: BukkitTask
    private var tpsThreshold: Double = 9.5
    private var tickReference : Int = 1
    private var shutdownDelayTime: Long = 60
    private var recoveryRecover: Boolean = false
    private var recoveryThreshold: Double = 15.5
    private var recoveryRecoverDelay: Boolean = false
    private var recoveryRecoverDelayTime: Long = 60

    override fun onEnable() {
        pluginConfig = PluginConfig(this, "config.yml")
        localeConfig = LocaleConfig(this, pluginConfig.getLanguageFile())
        loadAndSetConfig()
        Bukkit.getLogger().info(localeConfig.getLocaleString(LocalePath.PluginLoadMessage).format(tpsThreshold.toFloat(), shutdownDelayTime.toInt()))
        tpsChecker = Bukkit.getScheduler().runTaskTimer(this,
            Runnable { tpsCheck() },
            20 * pluginConfig.getCheckInterval(), 20 * pluginConfig.getCheckInterval()
        )
    }

    override fun onDisable() {
        tpsChecker.cancel()
    }

    private fun tpsCheck() {
        if(MinecraftServer.getServer().recentTps[tickReference] < tpsThreshold){
            if(recoveryRecoverDelay){
                Bukkit.getLogger().info(localeConfig.getLocaleString(LocalePath.RecoveryRecoverStarted).format(tpsThreshold, recoveryRecoverDelayTime.toInt()))
                Bukkit.getScheduler().runTaskLater(this, Runnable {
                    if(MinecraftServer.getServer().recentTps[tickReference] > recoveryThreshold){
                        Bukkit.getLogger().info(localeConfig.getLocaleString(LocalePath.RecoveryRecoverRecovered).format(MinecraftServer.getServer().recentTps[tickReference].toFloat()))
                        return@Runnable
                    }
                    Bukkit.broadcastMessage(localeConfig.getLocaleString(LocalePath.PreShutdownNoticeMessage).format(tpsThreshold, shutdownDelayTime.toInt()))
                    Bukkit.getScheduler().runTaskLater(this, Runnable { shutdown() }, 20 * shutdownDelayTime)
                    return@Runnable
                }, 20 * recoveryRecoverDelayTime)
                return
            }else{
                Bukkit.broadcastMessage(localeConfig.getLocaleString(LocalePath.PreShutdownNoticeMessage).format(tpsThreshold, shutdownDelayTime.toInt()))
                Bukkit.getScheduler().runTaskLater(this, Runnable { shutdown() }, 20 * shutdownDelayTime)
                return
            }
        }
    }
    private fun shutdown(){
        if(MinecraftServer.getServer().recentTps[tickReference] > recoveryThreshold){
            Bukkit.broadcastMessage(localeConfig.getLocaleString(LocalePath.WhenTPSRecoveredToThreshold))
            return
        }
        val countdownMessage = localeConfig.getLocaleString(LocalePath.InShutdownCountdownMessage)
        var countdown = pluginConfig.getShutdownCountdownTime()
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            countdown--
            Bukkit.broadcastMessage(countdownMessage.format(countdown.toInt()))
            if(countdown <= 0) {
                Bukkit.broadcastMessage(localeConfig.getLocaleString(LocalePath.InShutdownProgressMessage))
                Bukkit.shutdown()
            }
        },0, 20)
        return
    }

    private fun loadAndSetConfig(){
        tpsThreshold = pluginConfig.getTickThreshold()
        tickReference = pluginConfig.getTickReference()
        shutdownDelayTime = pluginConfig.getShutdownDelayTime()
        recoveryRecover = pluginConfig.getRecoveryRecover()
        recoveryThreshold = pluginConfig.getRecoveryThreshold()
        recoveryRecoverDelay = pluginConfig.getRecoveryRecoverDelay()
        recoveryRecoverDelayTime = pluginConfig.getRecoveryRecoverDelayTime()
    }


}