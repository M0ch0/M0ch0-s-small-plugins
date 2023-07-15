package io.github.mocho.ecapi.repository

import io.github.mocho.ecapi.model.Transaction

interface TransactionRepository {
    fun save(transaction: Transaction)
}