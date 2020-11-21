package mx.itesm.thegoldenbook.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.models.Owner
import mx.itesm.thegoldenbook.utils.Utils

class TestRealtime: AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_realtime)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference

        val owner = Owner("test", "Roberto Martinez Roman", "rmroman@tec.mx", "https://www.google.com", System.currentTimeMillis());
        databaseReference.child("Users").setValue(owner).addOnCompleteListener {
            Utils.print("Correcto")
        }.addOnSuccessListener {
            Utils.print("OnSuccess")
        }.addOnFailureListener {
            Utils.print(it.toString())
        }.addOnCanceledListener {
            Utils.print("OnCancel")
        }
    }
}