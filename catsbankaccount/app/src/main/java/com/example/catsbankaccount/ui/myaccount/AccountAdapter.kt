package com.example.catsbankaccount.ui.myaccount

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catsbankaccount.databinding.ItemAccountRowBinding
import com.example.catsbankaccount.model.Operation
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class AccountAdapter(private val items: List<Operation>): RecyclerView.Adapter<AccountAdapter.ViewHolder>() {


    class ViewHolder(binding: ItemAccountRowBinding): RecyclerView.ViewHolder(binding.root){
        val tvOperationTitle = binding.tvOperation
        val tvOperationDate = binding.tvOperationDate
        val tvOperationBalance = binding.tvOperationBalance
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAccountRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val operation = items.get(position)
        holder.tvOperationBalance.text = "${operation.amount} €"
        val timestamp = 1644611558L // la date en timestamp Unix
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // le format souhaité
        val formattedDate = sdf.format(Date(timestamp * 1000)) // la date formatée
        holder.tvOperationDate.text = formattedDate
        holder.tvOperationTitle.text = operation.title
    }
}