package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Pagina
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.ui.adapters.PaginaAdapter
import mx.itesm.thegoldenbook.utils.Constants
import mx.itesm.thegoldenbook.utils.Utils

class VisualizarEsteLibroActivity : AppCompatActivity() {
    private lateinit var tvTituloLibro: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: ExtendedFloatingActionButton

    private lateinit var adapter: PaginaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_este_libro)

        val intent = intent
        val bundle = intent.extras

        if(bundle == null) {
            finish()
            return
        }

        val albumId = bundle.getString(Constants.ParamAlbumId)

        if(albumId == null) {
            finish()
            return
        }

        val currentUser = Settings.getCurrentUser()

        if(currentUser == null) {
            finish()
            return
        }

        tvTituloLibro = findViewById(R.id.tvTituloLibro)
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)

        adapter = PaginaAdapter(this, object: ItemListener<Pagina> {
            override fun onItemSelected(model: Pagina) {
                Utils.print("Pagina: ${model.paginaId}")
            }
        })

        FirebaseRepository.instance.getPaginasList(currentUser.uid, albumId, object: ItemListener<MutableList<Pagina>> {
            override fun onItemSelected(model: MutableList<Pagina>) {
                adapter.setList(model)
            }
        })

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            startActivity(Intent(this, AgregarPaginaActivity::class.java))
        }
    }
}