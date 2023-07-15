package io.github.mocho.bedvote.command

import io.github.mocho.bedvote.BedVote.Companion.instance
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class Executor : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(!(command.name.equals("bedvote", ignoreCase = true))){ return false }
        when (args[0]) {
            "vote" -> {
                if(args.size != 2) { return false }
                val player = sender as Player
                when (args[1]) {
                    "agree" ->{
                        instance.voteManager.voteAgree(player, player.world)
                        return true
                    }
                    "decline" -> {
                        instance.voteManager.voteDecline(player, player.world)
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String> ): MutableList<String> {
        val completions: MutableList<String> = mutableListOf()
        if(!(command.name.equals("bedvote", ignoreCase = true))){ return completions }
        if(args.size == 1){
            completions.add("vote")
        } else if (args.size == 2 && args[0].equals("vote", ignoreCase = true)) {
            completions.add("agree")
            completions.add("decline")
        }
        return completions
    }
}