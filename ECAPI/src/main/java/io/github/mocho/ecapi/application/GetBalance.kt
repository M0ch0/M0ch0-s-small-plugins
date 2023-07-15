package io.github.mocho.ecapi.application

import io.github.mocho.ecapi.model.Account
import io.github.mocho.ecapi.repository.AccountRepository

class GetBalance(private val accountRepository: AccountRepository) {
    fun execute(id: String): Double {
        val account = accountRepository.find(id)
        return account.balance
    }
}