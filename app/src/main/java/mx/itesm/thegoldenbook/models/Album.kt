package mx.itesm.thegoldenbook.models

import com.google.firebase.database.Exclude

data class Album(
    var albumId: String,
    var ownerId: String,
    var titulo: String,
    var rutaPortada: String,
    var fechaCreacion: Long
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
        result["albumId"] = albumId
        result["ownerId"] = ownerId
        result["titulo"] = titulo
        result["rutaPortada"] = rutaPortada
        result["fechaCreacion"] = fechaCreacion
        return result
    }
}