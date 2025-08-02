package com.example.niaganow.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niaganow.R
import com.example.niaganow.model.Transaction

class TransactionAdapter(private val transactionList: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTransactionId: TextView = itemView.findViewById(R.id.tvTransactionId)
        val tvTransactionDate: TextView = itemView.findViewById(R.id.tvTransactionDate)
        val tvTransactionDescription: TextView = itemView.findViewById(R.id.tvTransactionDescription)
        val tvTransactionAmount: TextView = itemView.findViewById(R.id.tvTransactionAmount)
        val tvTransactionStatus: TextView = itemView.findViewById(R.id.tvTransactionStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.tvTransactionId.text = transaction.id
        holder.tvTransactionDate.text = transaction.date
        holder.tvTransactionDescription.text = transaction.description
        holder.tvTransactionAmount.text = transaction.amount
        holder.tvTransactionStatus.text = transaction.status
    }

    override fun getItemCount(): Int = transactionList.size
}
