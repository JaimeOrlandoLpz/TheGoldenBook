package mx.itesm.thegoldenbook.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.ui.viewholders.AlbumHolder

class AlbumsAdapter: RecyclerView.Adapter<AlbumHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_content_album, parent, false)
        return AlbumHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        holder.tvAlbumTitle.text = "Album posición $position"

        holder.ivAlbum.setImageResource(R.drawable.icono_libro)
    }

    override fun getItemCount(): Int {
        return 10
    }
}