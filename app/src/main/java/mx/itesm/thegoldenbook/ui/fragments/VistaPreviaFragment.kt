package mx.itesm.thegoldenbook.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mx.itesm.thegoldenbook.R
import mx.itesm.thegoldenbook.models.Pagina
import mx.itesm.thegoldenbook.utils.Constants

class VistaPreviaFragment: Fragment() {
    private lateinit var rootView: View
    private lateinit var ivPaginaImagen: ImageView
    private lateinit var tvDescription: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_vista_previa, container, false)

        ivPaginaImagen = rootView.findViewById(R.id.ivPaginaImagen)
        tvDescription = rootView.findViewById(R.id.tvDescription)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments ?: return

        val json = bundle.getString(Constants.ParamPagina)
        val gson = Gson()
        val type = object : TypeToken<Pagina>() {}.type
        val pagina: Pagina = gson.fromJson(json, type) ?: return

        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.getReferenceFromUrl(Constants.BUCKET + pagina.rutaImagen)
        Glide.with(requireContext()).load(gsReference).into(ivPaginaImagen)

        tvDescription.text = pagina.texto
    }
}