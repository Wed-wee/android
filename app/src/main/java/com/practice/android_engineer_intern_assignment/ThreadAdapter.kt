package com.practice.android_engineer_intern_assignment

import Threads
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ThreadsAdapter(
    private val threads: List<Threads?>,  // Use Threads type instead of Thread
    private val onItemClick: (Threads) -> Unit  // Change to Threads type
) : RecyclerView.Adapter<ThreadsAdapter.ThreadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return ThreadViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int) {
        val thread = threads[position]
        if (thread != null) {
            holder.senderTextView.text = "Sender: ${thread.id}"  // Assuming 'userId' is the sender's identifier
            holder.latestMessageTextView.text = thread.body  // Assuming 'body' is the latest message body
        }
        holder.itemView.setOnClickListener {
            thread?.let { onItemClick(it) }  // Make sure thread is non-null before passing
        }
    }

    override fun getItemCount(): Int = threads.size

    class ThreadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderTextView: TextView = view.findViewById(android.R.id.text1)
        val latestMessageTextView: TextView = view.findViewById(android.R.id.text2)
    }
}
