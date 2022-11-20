package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.SearchHistoryAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.SearchResultAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.SearchResultNavAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentSearchBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentSearchResultBinding

class SearchResultFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<SearchResultNavAdapter.ViewHolder>? = null
    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        binding.searchResultRV.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = SearchResultNavAdapter()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}