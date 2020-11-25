package mx.itesm.thegoldenbook.interfaces

import android.view.View

interface ItemListener<T> {
    fun onItemSelected(model: T) {}
    fun onItemSelected(position: Int, model: T) {}
    fun onItemSelected(view: View, model: T) {}
}