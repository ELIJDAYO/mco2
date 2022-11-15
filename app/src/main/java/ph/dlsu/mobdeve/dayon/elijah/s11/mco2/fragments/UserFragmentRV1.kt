package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentUserRV1Binding

class UserFragmentRV1 : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<NovelItemAdapter.ViewHolder>? = null
    private var _binding: FragmentUserRV1Binding? = null
    private val binding get() = _binding!!

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserRV1Binding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        setupData()
    }
    private fun setupData() {
        binding.rv.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = NovelItemAdapter()
        }
//        binding.label.text = getString(R.string.app_name)
//        val color = arguments?.getString("color")
//        binding.root.setBackgroundColor(Color.parseColor(color))
//        val position = arguments?.getString("label")
//        binding.label.text = position
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}