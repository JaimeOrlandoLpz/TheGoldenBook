package mx.itesm.thegoldenbook.models

import com.google.firebase.database.Exclude

data class Album(
    val albumId: Long,
    val ownerId: String,
    val titulo: String,
    val rutaPortada: String,
    val fechaCreacion: Long
) {
    constructor() : this(
        0,
        "",
        "",
        "",
        0L
    )

    @Exclude
    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["albumId"] = albumId
        result["ownerId"] = ownerId
        result["titulo"] = titulo
        result["rutaPortada"] = rutaPortada
        result["fechaCreacion"] = fechaCreacion
        return result
    }
}