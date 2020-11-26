package mx.itesm.thegoldenbook.ui.viewholders

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.thegoldenbook.R

class PaginaHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cvContainer: CardView = itemView.findViewById(R.id.cvContainer)
    val ivPagina: ImageView = itemView.findViewById(R.id.ivPagina)
    val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
}