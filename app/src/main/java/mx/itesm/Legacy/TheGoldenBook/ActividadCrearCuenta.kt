package mx.itesm.Legacy.TheGoldenBook

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_crear_cuenta.*


class ActividadCrearCuenta : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)
        mAuth = FirebaseAuth.getInstance()

    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            println("Usuario: ${currentUser?.displayName}")
        }else{
            println("No has hecho login")
        }
    }

    fun createAccount(correo: String, password: String){
        mAuth.createUserWithEmailAndPassword(correo, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    println("createUserWithEmail:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    println("createUserWithEmail:failure ${task.exception}")
                    Toast.makeText(
                        this@ActividadCrearCuenta, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }

            }
    }

    fun registroClicked(view: View){
        val intentMenu = Intent(this, ActividadMenu2::class.java).apply {

        }
        val correo = editTextTextMail.text.toString()
        val password = editTextTextPassword.text.toString()
        //val passwordConfirmar = editTextTextPasswordConfirm.text.toString();

        createAccount(correo, password)

        //AGREGAR MENSAJE DE CONFIRMACION

        /*if(passwordConfirmar == password){
            createAccount(correo, password);
        }else{
            println("Las contraseñas no coinciden")
        }*/

        startActivity(intentMenu)
    }
}