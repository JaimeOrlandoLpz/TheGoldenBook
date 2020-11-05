package mx.itesm.thegoldenbook.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import kotlinx.android.synthetic.main.activity_crear_cuenta.*
import mx.itesm.thegoldenbook.R

class ActividadLogin : AppCompatActivity() {
    var flag = 0
    private val permissions: ArrayList<String> = ArrayList()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginButton: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.loginButton)

        // Facebook Permissions
        permissions.add("email")
        permissions.add("public_profile")

        callbackManager = CallbackManager.Factory.create()

        loginButton.setReadPermissions(permissions)
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    loginSuccess(loginResult)
                }

                override fun onError(ex: FacebookException) {
                    Log.d("Jaime", "facebook:onError: $ex")
                }

                override fun onCancel() {
                    Log.d("Jaime", "facebook:onCancel")
                }
            })
    }

    private fun loginSuccess(loginResult: LoginResult?) {
        if(loginResult == null) {
            return
        }

        val accessToken = loginResult.accessToken
        val token = accessToken.token
        val credential = FacebookAuthProvider.getCredential(token)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Jaime","Login Success")

                        val firebaseUser = auth.currentUser

                        if (firebaseUser != null) {
                            val firebaseId = firebaseUser.uid
                            val userName = if (firebaseUser.displayName == null) "" else firebaseUser.displayName!!
                            val email = if (firebaseUser.email == null) "" else firebaseUser.email!!
                            val photoUrl = if (firebaseUser.photoUrl == null) "" else firebaseUser.photoUrl.toString() + "?type=large"
                            loginUser(firebaseId, userName, email, photoUrl)


                        } else {
                            //TODO setUserLogged(false)
                        }
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

    fun loginUser(uid: String, displayName: String, email: String, photoUrl: String) {
        Log.d("Jaime", "UID: $uid")
        Log.d("Jaime", "displayName: $displayName")
        Log.d("Jaime", "email: $email")
        Log.d("Jaime", "photoUrl: $photoUrl")
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

    }

    fun signIn(correo: String, password: String){
        auth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                println("signInWithEmail:success")
                val user = auth.currentUser
                updateUI(user)
                flag = 1
            } else {
                // If sign in fails, display a message to the user.
                println("signInWithEmail:failure")
                Toast.makeText(
                    this@ActividadLogin,
                    "Usuario o contraseña incorrectos.",
                    Toast.LENGTH_SHORT
                ).show()
                updateUI(null)
                flag = 2
            }
        }
    }

    fun loginClicked(view: View) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }
}