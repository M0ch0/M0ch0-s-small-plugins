package io.github.mocho.ecapi.model

import java.time.Instant

data class Transaction(val timestamp: Instant, val senderId: String, val receiverId: String, val amount: Double)