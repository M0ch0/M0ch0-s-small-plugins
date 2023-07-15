package io.github.mocho.ecapi.application

import io.github.mocho.ecapi.model.Account
import io.github.mocho.ecapi.repository.AccountRepository

class CreateAccount(private val accountRepository: AccountRepository) {
    fun execute(id: String): Boolean {
        return try {
            val account = Account(id, 0.0)
            accountRepository.save(account)
            true
        } catch (e: Exception){
            false
        }
    }

}