package io.github.mocho.ecapi

import io.github.mocho.ecapi.api.EconomyAPI
import io.github.mocho.ecapi.application.AddBalance
import io.github.mocho.ecapi.application.CreateAccount
import io.github.mocho.ecapi.application.TransferFunds
import io.github.mocho.ecapi.database.SQLiteAccountRepository
import io.github.mocho.ecapi.database.SQLiteTransactionRepository
import org.bukkit.plugin.java.JavaPlugin

class ECAPI : JavaPlugin() {
    override fun onEnable() {

    }

    override fun onDisable() {
    }
}