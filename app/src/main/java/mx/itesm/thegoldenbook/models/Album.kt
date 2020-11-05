package mx.itesm.thegoldenbook.models

data class Album(
    val albumId: Int,
    val ownerId: Int,
    val titulo: String,
    val rutaPortada: String,
    val fechaCreacion: Long
)