package io.github.mocho.bedvote.events

import io.github.mocho.bedvote.BedVote.Companion.instance
import io.github.mocho.bedvote.State
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor
import io.github.mocho.bedvote.config.LocaleConfig as LOCALE
import io.github.mocho.bedvote.localization.LocalePath as PATH
import io.github.mocho.bedvote.config.PluginConfig as CONFIG
import org.bukkit.Bukkit
import org.bukkit.Statistic
import org.bukkit.World
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitTask

class VoteEvent(private val world: World, internal var state: State = State.ACTIVE) : Listener {


    private val agreedPlayers: MutableList<Player> = mutableListOf()
    private val declinedPlayers: MutableList<Player> = mutableListOf()
    private val notifyMsg: TextComponent = Component.text()
        .append(Component.text(LOCALE.getLocaleString(PATH.VoteSuggestsMessage))).append(Component.newline())
        .append(Component.text(LOCALE.getLocaleString(PATH.VoteAgreeButtonText))
            .color(TextColor.color(0,255,0))
            .clickEvent(ClickEvent.runCommand("/bedvote vote agree")))
        .append(Component.text(LOCALE.getLocaleString(PATH.VoteDeclineButtonText))
            .color(TextColor.color(255,0,0))
            .clickEvent(ClickEvent.runCommand("/bedvote vote decline")))
        .build()


    private val progressBar: BossBar =
        Bukkit.getServer().createBossBar(String.format("!VOTE SKIP[%1\$d]!  « AGREE:0 VS DECLINE:0 »", world.players.size),
            CONFIG.getBossbarColor(), CONFIG.getBossbarStyle())

    private val requireAgreeRatio: Int = CONFIG.getRequiredAgreeRatio()
    private val requireDeclineRatio: Int = CONFIG.getRequiredDeclineRatio()

    private val closeTimer: BukkitTask = Bukkit.getScheduler().runTaskLater(instance,
        Runnable {
            world.players.forEach {
                    player: Player ->  player.sendMessage(LOCALE.getLocaleString(PATH.VoteBelowStipulationMessage))
            }
            close()
        },
        CONFIG.getVoteTimeLimit() * 20)

    init {
        if(state == State.CLOSED) { close() } else {

            if(CONFIG.getBossbarShow()) { world.players.forEach { player: Player ->
                    progressBar.addPlayer(player)
                    player.sendMessage(notifyMsg)
                }
            }
            progressBar.isVisible = true
            progressBar.progress = 1.0 / world.players.size.toDouble()
            instance.server.pluginManager.registerEvents(this, instance)
        }


    }

    private fun updateProgressBar(){
        progressBar.progress = (agreedPlayers.size / world.players.size).toDouble()
        var remainingPlayerSize: Int = (world.players.size - (agreedPlayers.size + declinedPlayers.size))
        progressBar.setTitle(LOCALE.getLocaleString(PATH.VoteBossBarTitle)
            .format(
                remainingPlayerSize,
                agreedPlayers.size,
                declinedPlayers.size
        ))
    }

    private fun check(){
        if((world.players.size / requireAgreeRatio) < agreedPlayers.size){
            pass()
            return
        }else if((world.players.size / requireDeclineRatio) < declinedPlayers.size){
            reject()
            return
        } else if(world.players.size == (agreedPlayers.size + declinedPlayers.size)){
            world.players.forEach { player: Player -> player.sendMessage(LOCALE.getLocaleString(PATH.VoteUndecidedMessage)) }
            close()
        }
    }

    internal fun addAgreePlayer(player: Player) : Boolean{
        if(!isPlayerNotVoted(player)) { return false }
        agreedPlayers.add(player)
        updateProgressBar()
        check()
        return true
    }

    internal fun addDeclinePlayer(player: Player) : Boolean{
        if(!isPlayerNotVoted(player)) { return false}
        declinedPlayers.add(player)
        updateProgressBar()
        check()
        return true
    }

    private fun isPlayerNotVoted(player: Player) : Boolean{
        if(agreedPlayers.contains(player)) { return false}
        if(declinedPlayers.contains(player)) { return false}
        return true
    }

    private fun pass(){
        when(CONFIG.getPhantomSpawnTimerResetTarget()) {
            "agreed" -> {
                agreedPlayers.forEach { player: Player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0) }
            }
            "all" -> {
                world.players.forEach { player: Player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0) }
            }
            else -> {
                // NOTHING DO
            }
        }
        agreedPlayers.forEach { player: Player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0) }
        world.players.forEach { player: Player ->  player.sendMessage(LOCALE.getLocaleString(PATH.VotePassedMessage)) }
        world.weatherDuration = 0
        world.isThundering = false
        world.setStorm(false)
        world.time = 0L
        close()
    }

    private fun reject(){
        world.players.forEach { player: Player -> player.sendMessage(LOCALE.getLocaleString(PATH.VoteRejectMessage)) }
        close()
    }

    private fun close(){
        progressBar.removeAll()
        progressBar.isVisible = false
        closeTimer.cancel()
        HandlerList.unregisterAll(this)
        state = State.CLOSED
    }

    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent){
        if(event.player.world != world) { return }
        declinedPlayers.remove(event.player)
        agreedPlayers.remove(event.player)
    }

    @EventHandler
    fun onPlayerChangedWorld(event: PlayerChangedWorldEvent){
        if(event.from != world) { return }
        declinedPlayers.remove(event.player)
        agreedPlayers.remove(event.player)
    }

}