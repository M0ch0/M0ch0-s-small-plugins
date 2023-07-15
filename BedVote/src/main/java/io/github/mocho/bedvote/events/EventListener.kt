package io.github.mocho.bedvote.events

import io.github.mocho.bedvote.BedVote
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent

class EventListener(private val instance: BedVote) : Listener {

    @EventHandler
    fun onPlayerBedEnterEvent(event: PlayerBedEnterEvent){
        if(event.bedEnterResult != PlayerBedEnterEvent.BedEnterResult.OK) { return }
        instance.voteManager.startNewVote(event.player.world)
        event.isCancelled = true
    }

}