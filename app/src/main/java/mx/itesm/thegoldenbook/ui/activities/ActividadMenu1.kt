package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings

class ActividadMenu1 : AppCompatActivity() {
    private lateinit var btnCrearCuenta: MaterialButton
    private lateinit var btnLogin: MaterialButton
    private lateinit var tvContacto: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu1)

        btnCrearCuenta = findViewById(R.id.btnCrearCuenta)
        btnLogin = findViewById(R.id.btnLogin)
        tvContacto = findViewById(R.id.tvContacto)

        val currentUser = Settings.getCurrentUser()

        if(currentUser != null) {
            startActivity(Intent(this, ActividadMenu2::class.java))
            finish()
        }

        btnCrearCuenta.setOnClickListener {
            val intent = Intent(this, ActividadCrearCuenta::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, ActividadLogin::class.java)
            startActivity(intent)
            finish()
        }

        tvContacto.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            intent.putExtra(Intent.EXTRA_EMAIL, "thegoldenbookproject@gmail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
            intent.putExtra(Intent.EXTRA_TEXT, "Mensaje")

            startActivity(Intent.createChooser(intent, "Enviar"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_overflow, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.acercaDe) {
            val intent = Intent(this, Ayuda::class.java)
            startActivity(intent)
            return true
        }

        return super.onContextItemSelected(item)
    }
}