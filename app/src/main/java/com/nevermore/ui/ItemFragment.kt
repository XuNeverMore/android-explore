package com.nevermore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nevermore.R
import com.nevermore.ui.dummy.DummyContent
import com.nevermore.ui.dummy.DummyContent.DummyItem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [DummyContent.OnListFragmentInteractionListener] interface.
 */
class ItemFragment : Fragment(),
    DummyContent.OnListFragmentInteractionListener {


    private var columnCount = 1

//    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyItemRecyclerViewAdapter(getItemList(), this@ItemFragment)
            }
        }
        return view
    }

    private fun getItemList(): MutableList<DummyItem> {
        val list: MutableList<DummyItem> = ArrayList()
        val listOf = mutableListOf(
            ItemFragmentDirections.actionItemFragmentToBitmapFragment()
        )
        for (i in 0 until listOf.size) {
            list.add(DummyItem("$i", "Bitmap", listOf[i]))
        }

        return list
    }

    override fun onListFragmentInteraction(item: DummyItem?) {
        item?.let {
            findNavController().navigate(it.action)
        }
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
