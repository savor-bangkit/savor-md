import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salya.savorcapstone.R
import com.salya.savorcapstone.data.response.FridgeItemResponse

class FridgeItemAdapter(
    private var items: List<FridgeItemResponse>,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<FridgeItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryTextView: TextView = view.findViewById(R.id.textViewCategory)
        val daysCountTextView: TextView = view.findViewById(R.id.textViewDaysCountExpire)
        val deleteButton: Button = view.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fridge, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.categoryTextView.text = item.category
        holder.daysCountTextView.text = "Days to expire: ${item.daysCountExpire}"
        holder.deleteButton.setOnClickListener {
            onDelete(item.id)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<FridgeItemResponse>) {
        items = newItems
        notifyDataSetChanged()
    }
}
