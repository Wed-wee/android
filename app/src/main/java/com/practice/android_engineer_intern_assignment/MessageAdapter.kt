import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.practice.android_engineer_intern_assignment.R

class MessagesAdapter(private val messages: List<Message>) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.senderTextView.text = if (message.agent_id != null) "Agent: ${message.agent_id}" else "User: ${message.user_id}"
        holder.bodyTextView.text = message.body
        holder.timestampTextView.text = message.timestamp
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return messages.size
    }

    // Provide a reference to the views for each data item
    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderTextView: TextView = view.findViewById(R.id.senderTextView)
        val bodyTextView: TextView = view.findViewById(R.id.bodyTextView)
        val timestampTextView: TextView = view.findViewById(R.id.timestampTextView)
    }
}
