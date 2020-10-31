package mx.itesm.thegoldenbook.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.renglon_libro.view.*
import mx.itesm.thegoldenbook.models.Libro
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.interfaces.ClickListener

class AdaptadorListaLibros(private val arrLibros: Array<Libro>) :
    RecyclerView.Adapter<AdaptadorListaLibros.VistaRenglon>() {

    var listener : ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaRenglon {
        val vista = LayoutInflater.from(parent.context)
                    .inflate(R.layout.renglon_libro, parent, false)

        return VistaRenglon(vista)
    }

    override fun onBindViewHolder(holder: VistaRenglon, position: Int) {
        val libro = arrLibros[position]
        holder.set(libro)

        holder.vistaRenglon.setOnClickListener {
            listener?.clicked(position)
        }
    }

    override fun getItemCount(): Int {
        return arrLibros.size
    }

    class VistaRenglon (val vistaRenglon: View) : RecyclerView.ViewHolder(vistaRenglon) {

        fun set(libro: Libro){
            vistaRenglon.tvTituloLibro.text = libro.titulo
            vistaRenglon.tvAutorLibro.text = libro.autor
        }
    }
}