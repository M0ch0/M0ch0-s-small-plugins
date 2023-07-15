package io.github.mocho.bedvote

import io.github.mocho.bedvote.config.PluginConfig
import io.github.mocho.bedvote.events.VoteEvent
import org.bukkit.Bukkit
import io.github.mocho.bedvote.config.LocaleConfig as LOCALE
import io.github.mocho.bedvote.localization.LocalePath as PATH
import org.bukkit.World
import org.bukkit.World.Environment
import org.bukkit.WorldType
import org.bukkit.entity.Player

class VoteManager(private val instance: BedVote) {

    private val worlds: MutableMap<World, VoteEvent> = mutableMapOf()
    init {
        instance.server.worlds.forEach { world : World ->
            worlds[world] = VoteEvent(world, State.CLOSED)
            if (PluginConfig.getAutoStartEnabled() && world.environment == Environment.NORMAL){
                /*
                Bukkit.getScheduler().runTaskTimer(instance,
                    Runnable {
                        if(world.time >= 12000){
                            startNewVote(world)
                        }
                    },
                    0, 20 * 60)

                 */
            }
        }
    }

    internal fun startNewVote(world: World){
        worlds[world]?.let { event : VoteEvent ->
            if(event.state == State.ACTIVE) { return }
            worlds[world] = VoteEvent(world)
            return
        }
        return
    }

    internal fun voteAgree(player: Player, world: World) : Boolean {
        worlds[world]?.let { event : VoteEvent ->
            if(event.state == State.CLOSED) {
                player.sendMessage(LOCALE.getLocaleString(PATH.VoteIsNotHeldMessage))
                return false
            }
            event.addAgreePlayer(player).let {
                if(it) {
                    player.sendMessage(LOCALE.getLocaleString(PATH.VoteToAgreeMessage))
                } else {
                    player.sendMessage(LOCALE.getLocaleString(PATH.VoteAlreadyDoneMessage))
                }
            }
        }
        return true
    }

    internal fun voteDecline(player: Player, world: World) : Boolean {
        worlds[world]?.let { event : VoteEvent ->
            if(event.state == State.CLOSED) {
                player.sendMessage(LOCALE.getLocaleString(PATH.VoteIsNotHeldMessage))
                return false
            }
            event.addDeclinePlayer(player).let {
                if(it) {
                    player.sendMessage(LOCALE.getLocaleString(PATH.VoteToDeclineMessage))
                } else {
                    player.sendMessage(LOCALE.getLocaleString(PATH.VoteAlreadyDoneMessage))
                }
            }

        }
        return true
    }


}
enum class State{
    ACTIVE,CLOSED,DUMMY
}