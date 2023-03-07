package com.example.catsbankaccount.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class Account(
    val order: Int,
    val id: String,
    val holder: String,
    val role: Int,
    @JsonProperty("contract_number")
    val contractNumber: String,
    val label: String,
    @JsonProperty("product_code")
    val productCode: String?,
    val balance: Double,
    val operations: List<Operation>
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        mutableListOf<Operation>().apply {
            parcel.readList(this, Operation::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(order)
        parcel.writeString(id)
        parcel.writeString(holder)
        parcel.writeInt(role)
        parcel.writeString(contractNumber)
        parcel.writeString(label)
        parcel.writeString(productCode)
        parcel.writeDouble(balance)
        parcel.writeList(operations)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Account> {
        override fun createFromParcel(parcel: Parcel): Account {
            return Account(parcel)
        }

        override fun newArray(size: Int): Array<Account?> {
            return arrayOfNulls(size)
        }
    }
}
