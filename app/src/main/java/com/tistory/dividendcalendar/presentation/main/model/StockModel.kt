package com.tistory.dividendcalendar.presentation.main.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StockModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val companyName: String = "",
    val companyLogo: String = "",
    val ticker: String = "",
    var amount: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(companyName)
        parcel.writeString(companyLogo)
        parcel.writeString(ticker)
        parcel.writeString(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StockModel> {
        override fun createFromParcel(parcel: Parcel): StockModel {
            return StockModel(parcel)
        }

        override fun newArray(size: Int): Array<StockModel?> {
            return arrayOfNulls(size)
        }
    }
}