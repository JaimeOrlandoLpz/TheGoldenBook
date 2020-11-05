package mx.itesm.thegoldenbook.models

data class Owner(
    val ownerId: Int,
    val nombre: String,
    val apellidoP: String,
    val email: String,
    val fotoPerfil: String,
    val fechaNacimiento: Long
)