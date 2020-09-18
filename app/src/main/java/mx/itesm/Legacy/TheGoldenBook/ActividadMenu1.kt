package mx.itesm.Legacy.TheGoldenBook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

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

    fun registroClick(view: View) {
        val intentReg = Intent(this, ActividadCrearCuenta::class.java).apply {

        }

        startActivity(intentReg)
    }
}