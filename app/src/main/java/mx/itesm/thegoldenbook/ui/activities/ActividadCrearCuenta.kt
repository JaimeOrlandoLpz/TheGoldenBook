package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.application.Settings
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.repositories.FirebaseRepository
import mx.itesm.thegoldenbook.utils.Utils

class ActividadCrearCuenta: AppCompatActivity() {
    private val permissions: ArrayList<String> = ArrayList()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var callbackManager: CallbackManager
    private lateinit var loginButton: LoginButton

    private lateinit var edtNombre: EditText
    private lateinit var edtApellido: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmarPassword: EditText
    private lateinit var btnRegistrarse: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)

        loginButton = findViewById(R.id.loginButton)
        edtNombre = findViewById(R.id.edtNombre)
        edtApellido = findViewById(R.id.edtApellido)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        edtConfirmarPassword = findViewById(R.id.edtConfirmarPassword)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)

        // Facebook Permissions
        permissions.add("email")
        permissions.add("public_profile")

        callbackManager = CallbackManager.Factory.create()

        loginButton.setReadPermissions(permissions)
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                if(loginResult != null) {
                    createAccount(loginResult)
                }
            }

            override fun onError(ex: FacebookException) {
                Utils.print("Error de Facebook $ex")
            }

            override fun onCancel() {
                Utils.print("Cancelado")
            }
        })

        btnRegistrarse.setOnClickListener {
            registrar()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun updateUI(currentUser: FirebaseUser?, isForm: Boolean) {
        if(currentUser != null) {
            val owner: Owner = if(isForm) {
                val firebaseId = currentUser.uid
                val nombre = edtNombre.text.toString()
                val apellido = edtApellido.text.toString()
                val email = if (currentUser.email == null) "" else currentUser.email!!

                val owner = Owner(firebaseId, nombre, apellido, email, "", 0)

                owner
            } else {
                val firebaseId = currentUser.uid
                val userName = if (currentUser.displayName == null) "" else currentUser.displayName!!
                val email = if (currentUser.email == null) "" else currentUser.email!!
                val photoUrl = if (currentUser.photoUrl == null) "" else currentUser.photoUrl.toString() + "?type=large"

                val owner = Owner(firebaseId, userName, "", email, photoUrl, 0)

                owner
            }

            Settings.setCurrentUser(owner)
            FirebaseRepository.instance.registrar(this, owner)

            Settings.setLogged(true)
            Settings.setCurrentUser(owner)

            val intent = Intent(this, ActividadMenu2::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this@ActividadCrearCuenta, "Ha ocurrido un error, intentelo nuevamente", Toast.LENGTH_SHORT).show()
            Settings.setLogged(false)
        }
    }

    private fun registrar() {
        val nombre = edtNombre.text.toString()
        val apellido = edtApellido.text.toString()
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        val passwordConfirmar = edtConfirmarPassword.text.toString()

        if(nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || password.isEmpty()) {
            if(nombre.isEmpty()) {
                edtNombre.error = "El campo no debe estar vacío"
            }

            if(apellido.isEmpty()) {
                edtApellido.error = "El campo no debe estar vacío"
            }

            if(email.isEmpty()) {
                edtEmail.error = "El campo no debe estar vacío"
            }

            if(password.isEmpty()) {
                edtPassword.error = "El campo no debe estar vacío"
            }

            if(passwordConfirmar.isEmpty()) {
                edtConfirmarPassword.error = "El campo no debe estar vacío"
            }
        } else {
            if(password != passwordConfirmar) {
                edtPassword.error = "Las contraseñas no coinciden"
                edtConfirmarPassword.error = "Las contraseñas no coinciden"
            } else {
                createAccount(email, password)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val currentUser = auth.currentUser
                updateUI(currentUser, true)
            } else {
                Utils.print(task.exception.toString())
                Toast.makeText(this@ActividadCrearCuenta, "El correo '$email' ha sido usado actualmente para otra cuenta.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createAccount(loginResult: LoginResult) {
        val accessToken = loginResult.accessToken
        val token = accessToken.token
        val credential = FacebookAuthProvider.getCredential(token)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val currentUser = auth.currentUser
                        updateUI(currentUser, false)
                    } else {
                        // If sign in fails, display a message to the user.
                        if (task.exception == null) {
                            return
                        }

                        Log.d("Jaime", "Login Exception: " + task.exception.toString())
                        // TODO setUserLogged(false)
                    }
                }
            })
    }
}