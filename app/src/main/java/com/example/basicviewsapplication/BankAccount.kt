package com.example.basicviewsapplication

import java.lang.IllegalStateException

class BankAccount {
    private var balance: Double = 0.0
    fun deposit(amount: Double) {
        balance += amount
    }

    fun withdraw(amount: Double) {
        if (amount <= balance) {
            balance -= amount
        } else {
            throw IllegalStateException("Entered value is more than current balance!!")
        }
    }
}