package mx.itesm.thegoldenbook.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.ui.viewholders.AlbumHolder

class AlbumsAdapter(
    private val list: ArrayList<Album>,
    private val listener: ItemListener<Album>
): RecyclerView.Adapter<AlbumHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_content_album, parent, false)
        return AlbumHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        val item: Album = list[position]

        holder.tvAlbumTitle.text = item.titulo

        // TODO holder.ivAlbum.setImageResource(item.rutaPortada)

        holder.cvContainer.setOnClickListener {
            listener.onItemSelected(it, item)
        }

        holder.btnVer.setOnClickListener {
            listener.onItemSelected(it, item)
        }

        holder.btnEditar.setOnClickListener {
            listener.onItemSelected(it, item)
        }

        holder.btnBorrar.setOnClickListener {
            listener.onItemSelected(it, item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}