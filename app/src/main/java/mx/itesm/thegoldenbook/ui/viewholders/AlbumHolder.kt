package mx.itesm.thegoldenbook.ui.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.thegoldenbook.R

class AlbumHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var ivAlbum: ImageView = itemView.findViewById(R.id.ivAlbum)
    var tvAlbumTitle: TextView = itemView.findViewById(R.id.tvAlbumTitle)
}