package mx.itesm.Legacy.TheGoldenBook

class EditarLibroController {

     fun getArrLibrosBD(idUsuario : String) : Array<Libro>{

        return arrayOf(
            Libro("Prueba", "Prueba","01/01/2020")
        )
    }
}