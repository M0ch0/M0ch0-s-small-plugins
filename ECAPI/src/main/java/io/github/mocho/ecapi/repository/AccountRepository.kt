package io.github.mocho.ecapi.repository

import io.github.mocho.ecapi.model.Account

interface AccountRepository {
    fun save(account: Account)
    fun find(id: String): Account
}