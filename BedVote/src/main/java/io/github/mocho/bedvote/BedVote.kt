package io.github.mocho.bedvote

import io.github.mocho.bedvote.command.Executor
import io.github.mocho.bedvote.config.ConfigBase
import io.github.mocho.bedvote.config.LocaleConfig
import io.github.mocho.bedvote.config.PluginConfig
import io.github.mocho.bedvote.events.EventListener
import io.github.mocho.bedvote.localization.LocalePath
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class BedVote : JavaPlugin() {

    internal lateinit var voteManager: VoteManager
    private val pluginConfig: ConfigBase
    private val eventListener: EventListener
    private val localeConfig: LocaleConfig

    init {
        instance = this
        pluginConfig = PluginConfig(this, "config.yml")
        localeConfig = LocaleConfig(this, PluginConfig.getLanguageFile())
        eventListener = EventListener(this)

    }

    override fun onEnable() {
        Bukkit.getScheduler().runTaskLater(this,
            Runnable {
                voteManager = VoteManager(this)
                logger.info("BedVote Enabled")
                server.pluginManager.registerEvents(eventListener, this)
                this.getCommand("bedvote")?.setExecutor(Executor())
            },
        1)
        // BukkitRunnableの動作はWorldの初期化が終わった後からなので、onEnableの処理をrunTaskLaterで囲めばたとえそれが1tickの遅延でもWorldの初期化後に行われる
        // 今回の場合voteManagerがWorld依存の動作をするため、Worldの初期化後にこれを行うためこうなっている
    }
    companion object {
        lateinit var instance: BedVote
            private set

    }
}