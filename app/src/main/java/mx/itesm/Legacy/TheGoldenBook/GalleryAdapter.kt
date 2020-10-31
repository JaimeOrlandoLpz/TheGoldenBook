package mx.itesm.Legacy.TheGoldenBook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.gallery_box_custom_view.*

class GalleryAdapter :  RecyclerView.Adapter<GalleryAdapter.ViewHolder>(){
    var data = listOf<ViewHolder>()
    set(value){
        field = value
        notifyDataSetChanged()
    }

    // This is the RecyclerView itself
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.gallery_box_custom_view, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        val item = data[position]
        val res = holder.itemView.context.resources
        holder.bookName.text = item.bookName.text
        holder.imageView.setImageResource(R.drawable.icono_libro)
        holder.btn1.setImageResource(R.drawable.botonovalo)
        holder.btn2.setImageResource(R.drawable.botonovalo)
        holder.btn3.setImageResource(R.drawable.botonovalo)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        // Data Binding
        val bookName : TextView = itemView.findViewById(R.id.tvBookId)
        val imageView : ImageView = itemView.findViewById(R.id.ivImagenLibro)
        val btn1 : ImageButton = itemView.findViewById(R.id.btn1)
        val btn2 : ImageButton = itemView.findViewById(R.id.btn2)
        val btn3 : ImageButton = itemView.findViewById(R.id.btn3)

    }
}