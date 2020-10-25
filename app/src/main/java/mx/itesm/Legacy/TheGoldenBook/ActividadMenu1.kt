package mx.itesm.Legacy.TheGoldenBook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView

class ActividadMenu1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu1)

    }

    fun loginClicked(view: View){
        val intentLog = Intent(this, ActividadLogin::class.java).apply {

        }

        startActivity(intentLog)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_overflow, menu)
        return true
    }

    fun registroClick(view: View) {
        val intentReg = Intent(this, ActividadCrearCuenta::class.java).apply {

        }

        startActivity(intentReg)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.acercaDe) {
            val intentReg = Intent(this, Ayuda::class.java).apply {
            }
            startActivity(intentReg)
            return true
        }
        return super.onContextItemSelected(item)
    }



}