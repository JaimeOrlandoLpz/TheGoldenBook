package mx.itesm.thegoldenbook.models

import com.google.firebase.database.Exclude

data class Owner(
    val uid: String,
    val nombre: String,
    val email: String,
    val fotoPerfil: String,
    val fechaNacimiento: Long
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        0L
    )

    @Exclude
    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["uid"] = uid
        result["nombre"] = nombre
        result["email"] = email
        result["fotoPerfil"] = fotoPerfil
        result["fechaNacimiento"] = fechaNacimiento
        return result
    }
}