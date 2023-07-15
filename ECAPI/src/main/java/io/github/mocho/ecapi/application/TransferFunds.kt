package io.github.mocho.ecapi.application

import io.github.mocho.ecapi.model.Transaction
import io.github.mocho.ecapi.repository.AccountRepository
import io.github.mocho.ecapi.repository.TransactionRepository
import java.time.Instant

class TransferFunds(private val accountRepository: AccountRepository, private val transactionRepository: TransactionRepository) {
    fun execute(senderId: String, receiverId: String, amount: Double): Transaction {
        val sender = accountRepository.find(senderId)
        val receiver = accountRepository.find(receiverId)

        sender.balance -= amount
        receiver.balance += amount

        val transaction = Transaction(Instant.now(), senderId, receiverId, amount)
        transactionRepository.save(transaction)

        accountRepository.save(sender)
        accountRepository.save(receiver)

        return transaction
    }
}