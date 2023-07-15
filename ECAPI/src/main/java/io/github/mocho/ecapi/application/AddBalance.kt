package io.github.mocho.ecapi.application

import io.github.mocho.ecapi.model.Account
import io.github.mocho.ecapi.repository.AccountRepository

class AddBalance(private val accountRepository: AccountRepository) {
    fun execute(id: String, amount: Double): Boolean {
        return try {
            val account = accountRepository.find(id)
            account.balance += amount
            accountRepository.save(account)
            true
        } catch (e: Exception){
            false
        }

    }
}