@file:Suppress("DEPRECATION")

package mx.itesm.thegoldenbook.ui.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.utils.DatePickerFragment
import mx.itesm.thegoldenbook.utils.Utils
import java.util.*

class InfoUsuario : AppCompatActivity() {
    private lateinit var edtNombre: EditText
    private lateinit var edtEmail: EditText
    private lateinit var btnFechaNacimiento: Button
    private lateinit var tvFechaNacimiento: TextView
    private lateinit var btnActualizar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_usuario)

        edtNombre = findViewById(R.id.edtNombre)
        edtEmail = findViewById(R.id.edtEmail)
        btnFechaNacimiento = findViewById(R.id.btnFechaNacimiento)
        tvFechaNacimiento = findViewById(R.id.tvFechaNacimiento)
        btnActualizar = findViewById(R.id.btnActualizar)

        val account = Settings.getCurrentUser()

        edtNombre.setText(account?.nombre)
        edtEmail.setText(account?.email)
        tvFechaNacimiento.text = Utils.getFormattedDate(account?.fechaNacimiento!!)

        btnFechaNacimiento.setOnClickListener {
            getDatePicker()
        }

        btnActualizar.setOnClickListener {
            val nombre = edtNombre.text.toString()
            val email = edtEmail.text.toString()
            val fechaNacimiento = tvFechaNacimiento.text.toString()

            if(nombre.isEmpty() || email.isEmpty()) {
                if(nombre.isEmpty()) {
                    edtNombre.error = "El campo no debe estar vacío"
                }

                if(email.isEmpty()) {
                    edtEmail.error = "El campo no debe estar vacío"
                }
            } else {
                val date = Date(fechaNacimiento)
                val owner = Owner(account.uid, nombre, email, account.fotoPerfil, date.time)
                Utils.print(owner.fechaNacimiento.toString())
                FirebaseRepository.instance.update(owner)
            }
        }
    }

    private fun getDatePicker() {
        val newFragment = DatePickerFragment(object: DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                val selectedDate = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                tvFechaNacimiento.text = selectedDate
            }
        })

        newFragment.show(supportFragmentManager, "datePicker")
    }
}