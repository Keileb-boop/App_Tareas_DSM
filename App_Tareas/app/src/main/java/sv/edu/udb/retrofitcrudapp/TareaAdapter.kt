package sv.edu.udb.retrofitcrudapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class TareaAdapter(private val tareas: List<Tarea>) : RecyclerView.Adapter<TareaAdapter.ViewHolder>() {

    private var onItemClick: OnItemClickListener? = null
    private var tareaSeleccionada: Tarea? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloTextView: TextView = view.findViewById(R.id.tvTitulo)
        val descripcionTextView: TextView = view.findViewById(R.id.tvDescripcion)
        val estadoTextView: TextView = view.findViewById(R.id.tvEstado)
        val cardView: CardView = view.findViewById(R.id.cardViewItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tarea_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tarea = tareas[position]
        holder.tituloTextView.text = tarea.titulo
        holder.descripcionTextView.text = tarea.descripcion
        holder.estadoTextView.text = "Estado: ${tarea.estado}"

        if (tarea == tareaSeleccionada)
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFF3E0"))
        else
            holder.cardView.setCardBackgroundColor(Color.WHITE)

        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(tarea)
            notifyDataSetChanged()
        }

        holder.itemView.setOnLongClickListener {
            onItemClick?.onItemLongClick(tarea)
            true
        }
    }

    override fun getItemCount(): Int = tareas.size

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClick = listener
    }

    fun setTareaSeleccionada(tarea: Tarea) {
        tareaSeleccionada = tarea
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(tarea: Tarea)
        fun onItemLongClick(tarea: Tarea)
    }
}
