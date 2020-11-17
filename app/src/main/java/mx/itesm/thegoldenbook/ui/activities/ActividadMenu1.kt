package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import mx.itesm.thegoldenbook.R

class ActividadMenu1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu1)
    }

    fun loginClicked(view: View) {
        val intent = Intent(this, ActividadLogin::class.java)
        startActivity(intent)
        finish()
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

    fun mailOnClick(v: View){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/html"
        intent.putExtra(Intent.EXTRA_EMAIL, "thegoldenbookproject@gmail.com")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
        intent.putExtra(Intent.EXTRA_TEXT, "Mensaje")

        startActivity(Intent.createChooser(intent, "Enviar"))
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