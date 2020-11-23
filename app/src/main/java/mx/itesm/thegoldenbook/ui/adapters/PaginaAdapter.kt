package mx.itesm.thegoldenbook.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Pagina
import mx.itesm.thegoldenbook.ui.viewholders.PaginaHolder

class PaginaAdapter(
    private val context: Context,
    private val listener: ItemListener<Pagina>
): RecyclerView.Adapter<PaginaHolder>() {
    private var list: MutableList<Pagina> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaginaHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_content_album, parent, false)
        return PaginaHolder(view)
    }

    override fun onBindViewHolder(holder: PaginaHolder, position: Int) {
        val item: Pagina = list[position]

        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.getReferenceFromUrl("gs://goldenbook-3ae2f.appspot.com/photo_album.png")
        Glide.with(context).load(gsReference).into(holder.ivPagina)

        holder.cvContainer.setOnClickListener {
            listener.onItemSelected(it, item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: MutableList<Pagina>) {
        this.list = list
        notifyDataSetChanged()
    }
}