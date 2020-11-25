package mx.itesm.thegoldenbook.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.interfaces.ItemListener
import mx.itesm.thegoldenbook.models.Pagina
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.ui.adapters.ViewPagerAdapter
import mx.itesm.thegoldenbook.utils.Constants

class VistaPreviaActivity: AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_previa)

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

        val paginaId = bundle.getString(Constants.ParamPaginaId)

        if(paginaId == null) {
            finish()
            return
        }

        val position = bundle.getInt(Constants.ParamPosition)

        if(position == -1) {
            finish()
            return
        }

        val currentUser = Settings.getCurrentUser()

        if(currentUser == null) {
            finish()
            return
        }

        viewPager = findViewById(R.id.viewPager)

        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        viewPager.adapter = adapter

        FirebaseRepository.instance.getPaginasList(currentUser.uid, albumId, object: ItemListener<MutableList<Pagina>> {
            override fun onItemSelected(model: MutableList<Pagina>) {
                adapter.setList(model)
                viewPager.setCurrentItem(position, false)
            }
        })
    }
}