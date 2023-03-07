package com.example.catsbankaccount.ui.bankaccounts

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.content.UnusedAppRestrictionsConstants.DISABLED
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catsbankaccount.model.*
import com.example.catsbankaccount.repository.BankAccountService
import com.example.catsbankaccount.utils.Constants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.Gson
import com.squareup.okhttp.ResponseBody
import org.json.JSONObject
import retrofit.*

class BankAccountsViewModel: ViewModel() {
    private val _data = MutableLiveData<Pair<List<Bank>,List<Bank>>>()
    val data: LiveData<Pair<List<Bank>, List<Bank>>> get() = _data

    init {
        initNetworkRequest()
    }

    fun parseError(response: Response<*>): String {
        val errorBody = response.errorBody()?.string()
        return errorBody ?: "Unknown error"
    }

    fun getBankNames(banks: List<Bank>): List<String> {
        return banks.map { it.name }
    }

    fun getAllAccountsFromBanks(banks: List<Bank>): List<Account> {
        val accounts = mutableListOf<Account>()
        for (bank in banks) {
            accounts.addAll(bank.accounts)
        }
        return accounts
    }

    fun parseBanksJson(jsonString: String): List<Bank> {
        val mapper = ObjectMapper().registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, true)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
        val bankData = mapper.readValue<BankData>(jsonString)
        return bankData.banks
    }

    fun getBankBalances(accountPreviews: HashMap<String, List<DataPreview>>): List<DataPreview> {
        val bankBalances = arrayListOf<DataPreview>()

        for ((bankName, accountPreviewList) in accountPreviews) {
            var balanceSum = 0.0
            for (accountPreview in accountPreviewList) {
                balanceSum += accountPreview.balance.toDouble()
            }
            val bankBalance = DataPreview(bankName, String.format("%.2f" ,balanceSum))
            bankBalances.add(bankBalance)
        }

        return bankBalances
    }


    fun getAccountPreviewsByBank(banks: List<Bank>): HashMap<String, List<DataPreview>> {
        val previewsByBank = HashMap<String, List<DataPreview>>()

        for (bank in banks) {
            val previews = mutableListOf<DataPreview>()
            for (account in bank.accounts) {
                previews.add(DataPreview(account.label,  account.balance.toString()))
            }
            previewsByBank[bank.name] = previews
        }

        return previewsByBank
    }



    fun sortBanksAndSplitByCreditAgricole(banks: List<Bank>): Pair<List<Bank>, List<Bank>> {
        val sortedBanks = banks.sortedBy { it.name }

        val creditAgricoleBank = sortedBanks.filter  { it.isCA != 0 }
        val otherBanks = sortedBanks.filter { it.isCA == 0 }

        return Pair(creditAgricoleBank, otherBanks)
    }

    fun initNetworkRequest(){
        val retrofit: Retrofit = Retrofit.Builder()
            // API base URL.
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: BankAccountService = retrofit.create<BankAccountService>(BankAccountService::class.java)

        val bankCall: Call<Bank> = service.getAccounts()
        bankCall.enqueue(object : Callback<Bank> {
            override fun onResponse(
                response: Response<Bank>,
                retrofit: Retrofit
            ) {


                if (response.isSuccess) {
                    // On ne recoit pas la trame dans le ody mais dans le erreur body je ne sais pas pourquoi
                    val bankList: Bank = response.body()

                    Log.i("Response Result", "$bankList")


                    val BankJsonString = Gson().toJson(bankList)
                    Log.d("BankJsonString", "onResponse: BankJsonString "+BankJsonString)
//                setupUI()
                } else {
                    val errorbody = parseError(response)
                    val bankList = parseBanksJson(errorbody)
                    _data.postValue(sortBanksAndSplitByCreditAgricole(bankList))
                    val sc = response.code()
                    when (sc) {
                        400 -> {
                            Log.e("Error 400", "Bad Request")
                        }
                        404 -> {
                            Log.e("Error 404", "Not Found")
                        }
                        else -> {
                            Log.e("Error", "Generic Error")
                        }
                    }
                }
            }

            override fun onFailure(t: Throwable) {

                Log.e("Errorrrrr", t.message.toString())
            }
        })

    }


}