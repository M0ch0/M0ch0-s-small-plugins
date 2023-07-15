package io.github.mocho.ecapi.api

import io.github.mocho.ecapi.application.AddBalance
import io.github.mocho.ecapi.application.CreateAccount
import io.github.mocho.ecapi.application.GetBalance
import io.github.mocho.ecapi.application.TransferFunds
import io.github.mocho.ecapi.database.SQLiteAccountRepository
import io.github.mocho.ecapi.database.SQLiteTransactionRepository
import io.github.mocho.ecapi.model.Account
import io.github.mocho.ecapi.model.Transaction
import io.github.mocho.ecapi.repository.AccountRepository
import io.github.mocho.ecapi.repository.TransactionRepository

class EconomyAPI private constructor() {
    private val accountRepository: AccountRepository = SQLiteAccountRepository("./account.sqlite")
    private val transactionRepository: TransactionRepository = SQLiteTransactionRepository("./transaction.sqlite")

    private val createAccount = CreateAccount(accountRepository)
    private val transferFunds = TransferFunds(accountRepository, transactionRepository)
    private val addBalance = AddBalance(accountRepository)
    private val getBalance = GetBalance(accountRepository)

    fun createAccount(id: String): Boolean = createAccount.execute(id)
    fun transferFunds(senderId: String, receiverId: String, amount: Double): Transaction = transferFunds.execute(senderId, receiverId, amount)
    fun addBalance(id: String, amount: Double): Boolean = addBalance.execute(id, amount)
    fun getBalance(id: String): Double = getBalance.execute(id)

    companion object {
        private var INSTANCE: EconomyAPI? = null

        fun getInstance(): EconomyAPI {
            if (INSTANCE == null) {
                INSTANCE = EconomyAPI()
            }
            return INSTANCE!!
        }
    }
}







