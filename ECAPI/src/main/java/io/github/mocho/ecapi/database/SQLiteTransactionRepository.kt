package io.github.mocho.ecapi.database

import io.github.mocho.ecapi.model.Transaction
import io.github.mocho.ecapi.repository.TransactionRepository
import java.sql.DriverManager
import java.sql.Timestamp

class SQLiteTransactionRepository(private val url: String) : TransactionRepository {

    init {
        DriverManager.getConnection(url).use { connection ->
            connection.createStatement().use { statement ->
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS Transactions (timestamp TIMESTAMP, senderId TEXT, receiverId TEXT, amount REAL)")
            }
        }
    }

    override fun save(transaction: Transaction) {
        DriverManager.getConnection(url).use { connection ->
            connection.prepareStatement("INSERT INTO Transactions (timestamp, senderId, receiverId, amount) VALUES (?, ?, ?, ?)").use { preparedStatement ->
                preparedStatement.setTimestamp(1, Timestamp.from(transaction.timestamp))
                preparedStatement.setString(2, transaction.senderId)
                preparedStatement.setString(3, transaction.receiverId)
                preparedStatement.setDouble(4, transaction.amount)
                preparedStatement.executeUpdate()
            }
        }
    }
}