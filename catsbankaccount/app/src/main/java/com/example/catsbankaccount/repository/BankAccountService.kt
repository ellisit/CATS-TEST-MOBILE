package com.example.catsbankaccount.repository

import com.example.catsbankaccount.model.Bank
import retrofit.Call
import retrofit.http.GET

interface BankAccountService {

    @GET("accounts")
    fun getAccounts(): Call<Bank>
}