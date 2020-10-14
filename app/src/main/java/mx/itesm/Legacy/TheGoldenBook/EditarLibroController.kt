package mx.itesm.Legacy.TheGoldenBook

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditarLibroController {

     fun getArrLibrosBD(idUsuario : String) : Array<Libro>{

         val baseDatos = FirebaseDatabase.getInstance()
         val referencia = baseDatos.getReference("/Libro")
         referencia.keepSynced(true)
         var lsLibros = mutableListOf<Libro>()

         referencia.addListenerForSingleValueEvent(object : ValueEventListener {

             override fun onDataChange(snapshot : DataSnapshot) {
                 for (registro in snapshot.children){
                    val libro = registro.getValue(Libro :: class.java)

                    if(libro != null){
                        lsLibros.add(libro)
                    }
                 }
             }

             override fun onCancelled(error: DatabaseError) {

             }
         })
         println(lsLibros)

         return lsLibros.toTypedArray()
    }
}