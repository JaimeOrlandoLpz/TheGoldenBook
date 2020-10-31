package mx.itesm.thegoldenbook.ui.viewholders

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.thegoldenbook.R

class AlbumHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cvContainer: CardView = itemView.findViewById(R.id.cvContainer)
    val ivAlbum: ImageView = itemView.findViewById(R.id.ivAlbum)
    val tvAlbumTitle: TextView = itemView.findViewById(R.id.tvAlbumTitle)
    val btnVer: Button = itemView.findViewById(R.id.btnVer)
    val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
    val btnBorrar: Button = itemView.findViewById(R.id.btnBorrar)
}