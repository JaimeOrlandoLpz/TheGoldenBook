package mx.itesm.thegoldenbook.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.gson.Gson
import mx.itesm.thegoldenbook.models.Pagina
import mx.itesm.thegoldenbook.ui.fragments.VistaPreviaFragment
import mx.itesm.thegoldenbook.utils.Constants

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {
    private var list: MutableList<Pagina> = ArrayList()
    private val gson = Gson()

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val pagina = list[position]

        val json = gson.toJson(pagina)

        val bundle = Bundle()
        bundle.putString(Constants.ParamPagina, json)

        val fragment = VistaPreviaFragment()
        fragment.arguments = bundle

        return fragment
    }

    fun setList(list: MutableList<Pagina>) {
        this.list = list
        notifyDataSetChanged()
    }
}