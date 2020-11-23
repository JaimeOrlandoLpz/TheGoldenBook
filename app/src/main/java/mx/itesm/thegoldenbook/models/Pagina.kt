package mx.itesm.thegoldenbook.models

import com.google.firebase.database.Exclude

data class Pagina(
    var paginaId: String,
    var albumId: String,
    var texto: String,
    var rutaImagen: String,
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
        result["paginaId"] = paginaId
        result["albumId"] = albumId
        result["texto"] = texto
        result["rutaImagen"] = rutaImagen
        result["fechaCreacion"] = fechaCreacion
        return result
    }
}