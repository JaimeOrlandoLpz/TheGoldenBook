package mx.itesm.thegoldenbook.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Pagina
import mx.itesm.thegoldenbook.ui.viewholders.PaginaHolder
import mx.itesm.thegoldenbook.utils.Constants

class PaginaAdapter(
    private val context: Context,
    private val listener: ItemListener<Pagina>
): RecyclerView.Adapter<PaginaHolder>() {
    private var list: MutableList<Pagina> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaginaHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_content_pagina, parent, false)
        return PaginaHolder(view)
    }

    override fun onBindViewHolder(holder: PaginaHolder, position: Int) {
        val item: Pagina = list[position]

        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.getReferenceFromUrl(Constants.BUCKET + item.rutaImagen)
        Glide.with(context)
            .load(gsReference)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.ivPagina)

        holder.cvContainer.setOnClickListener {
            listener.onItemSelected(it, holder.adapterPosition, item)
        }

        holder.ivEdit.setOnClickListener {
            listener.onItemSelected(it, holder.adapterPosition, item)
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