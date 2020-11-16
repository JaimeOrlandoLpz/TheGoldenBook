package mx.itesm.thegoldenbook.models

data class Album(
    val albumId: Long,
    val ownerId: String,
    val titulo: String,
    val rutaPortada: String,
    val fechaCreacion: Long
)