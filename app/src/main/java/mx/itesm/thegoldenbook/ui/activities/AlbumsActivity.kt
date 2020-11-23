package mx.itesm.thegoldenbook.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Album
import mx.itesm.thegoldenbook.ui.adapters.AlbumsAdapter

class AlbumsActivity: AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)

        recyclerView = findViewById(R.id.recyclerView)

        val albumList = ArrayList<Album>()
        /*
        albumList.add(Album("Roberto Martinez Roman", R.drawable.icono_libro))
        albumList.add(Album("Roberto Martinez Roman", R.drawable.icono_libro))
        albumList.add(Album("Roberto Martinez Roman", R.drawable.icono_libro))
        albumList.add(Album("Roberto Martinez Roman", R.drawable.icono_libro))
        albumList.add(Album("Roberto Martinez Roman", R.drawable.icono_libro))
        albumList.add(Album("Roberto Martinez Roman", R.drawable.icono_libro))
        albumList.add(Album("Roberto Martinez Roman", R.drawable.icono_libro))
        albumList.add(Album("Roberto Martinez Roman", R.drawable.icono_libro))
        albumList.add(Album("Roberto Martinez Roman", R.drawable.icono_libro))
        albumList.add(Album("Roberto Martinez Roman", R.drawable.icono_libro))
        */

        val adapter = AlbumsAdapter(this, object: ItemListener<Album> {
            override fun onItemSelected(view: View, model: Album) {
                if(view.id == R.id.btnVer || view.id == R.id.cvContainer) {
                    //Toast.makeText(applicationContext, "Ver ${model.title}", Toast.LENGTH_SHORT).show()
                } else if(view.id == R.id.btnEditar) {
                    //Toast.makeText(applicationContext, "Editar ${model.title}", Toast.LENGTH_SHORT).show()
                } else if(view.id == R.id.btnBorrar) {
                    //Toast.makeText(applicationContext, "Borrar ${model.title}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
    }
}