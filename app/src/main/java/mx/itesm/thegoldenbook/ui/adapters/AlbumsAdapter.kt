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
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.ui.viewholders.AlbumHolder

class AlbumsAdapter(
    private val context: Context,
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

        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.getReferenceFromUrl("gs://goldenbook-3ae2f.appspot.com/album.png")
        Glide.with(context).load(gsReference).into(holder.ivAlbum)

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

    fun setList(list: MutableList<Album>) {
        this.list = list
        notifyDataSetChanged()
    }
}