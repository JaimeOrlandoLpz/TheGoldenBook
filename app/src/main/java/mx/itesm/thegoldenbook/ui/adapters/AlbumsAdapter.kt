package mx.itesm.thegoldenbook.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.ui.viewholders.AlbumHolder
import mx.itesm.thegoldenbook.utils.Utils

class AlbumsAdapter(
    private val listener: ItemListener<Album>
): RecyclerView.Adapter<AlbumHolder>() {
    private var list: MutableList<Album> = ArrayList()

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

    fun add(album: Album) {
        //list.add(album)
        //notifyDataSetChanged()
    }

    fun remove(albumId: String) {
        /*
        Utils.print("onChildRemoved 3 $albumId")
        var itemToRemove: Album? = null

        for(item in list) {
            if(item.albumId == albumId) {
                itemToRemove = item
            }
        }

        if(itemToRemove != null) {
            Utils.print("SuccessRemove $albumId")
            listener.onItemSelected(itemToRemove)
        }

        notifyDataSetChanged()
        */
    }

    fun changed(album: Album) {
        /*
        for(item in list) {
            if(item.albumId == album.albumId) {
                item.albumId = album.albumId
                item.ownerId = album.ownerId
                item.titulo = album.titulo
                item.rutaPortada = album.rutaPortada
                item.fechaCreacion = album.fechaCreacion
            }
        }

        notifyDataSetChanged()
        */
    }

    fun setList(list: MutableList<Album>) {
        this.list = list
        notifyDataSetChanged()
    }
}