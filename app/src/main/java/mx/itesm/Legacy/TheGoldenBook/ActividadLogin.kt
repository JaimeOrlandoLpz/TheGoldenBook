package mx.itesm.Legacy.TheGoldenBook

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_crear_cuenta.*


class ActividadLogin : AppCompatActivity() {
    var flag = 0
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance();
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

    }

    fun signIn(correo: String, password: String){

        mAuth.signInWithEmailAndPassword(correo, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    println("signInWithEmail:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                    flag = 1
                } else {
                    // If sign in fails, display a message to the user.
                    println("signInWithEmail:failure")
                    Toast.makeText(
                        this@ActividadLogin, "Usuario o contraseña incorrectos.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                    flag = 2
                }

                // ...
            }
    }

    fun loginClicked(view: View){
        val intentMenu = Intent(this, ActividadMenu2::class.java).apply {

        }
        val correo = editTextTextMail.text.toString()
        val password = editTextTextPassword.text.toString()
        //val passwordConfirmar = editTextTextPasswordConfirm.text.toString();

        signIn(correo, password)
        if(flag == 1){
            startActivity(intentMenu)
            flag= 0
        }else{
            println("Error al ingresar")
            flag = 0
        }


    }
}