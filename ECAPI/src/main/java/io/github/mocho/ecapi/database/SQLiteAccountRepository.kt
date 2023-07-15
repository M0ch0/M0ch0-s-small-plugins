package io.github.mocho.ecapi.database

import io.github.mocho.ecapi.model.Account
import io.github.mocho.ecapi.repository.AccountRepository
import java.sql.DriverManager

class SQLiteAccountRepository(private val url: String) : AccountRepository {

    init {
        DriverManager.getConnection(url).use { connection ->
            connection.createStatement().use { statement ->
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS Accounts (id TEXT PRIMARY KEY, balance REAL)")
            }
        }
    }

    override fun save(account: Account) {
        DriverManager.getConnection(url).use { connection ->
            connection.prepareStatement("INSERT OR REPLACE INTO Accounts (id, balance) VALUES (?, ?)").use { preparedStatement ->
                preparedStatement.setString(1, account.id)
                preparedStatement.setDouble(2, account.balance)
                preparedStatement.executeUpdate()
            }
        }
    }

    override fun find(id: String): Account {
        DriverManager.getConnection(url).use { connection ->
            connection.prepareStatement("SELECT * FROM Accounts WHERE id = ?").use { preparedStatement ->
                preparedStatement.setString(1, id)
                val resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    return Account(resultSet.getString("id"), resultSet.getDouble("balance"))
                } else {
                    throw Exception("Account not found")
                }
            }
        }
    }
}