package com.example.catsbankaccount.model

import com.google.gson.annotations.SerializedName

data class Bank(
    val name: String,
    val isCA: Int,
    val accounts: List<Account>
)
