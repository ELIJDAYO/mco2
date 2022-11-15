package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities.OptionActivity
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.NovelItemAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.ViewPagerAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentHomeBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.FragmentUserBinding


class UserFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<NovelItemAdapter.ViewHolder>? = null
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val titleNovel = arrayOf(
        "Goblin Slayer",
        "灰まみれの騎士",
        "13番目の転生者",
        "High School of the Elites")
    private val numEpisodes = arrayOf(
        "21", "33", "19", "100",
    )
    private val date = arrayOf(
        "Jan 1", "Feb 1", "March 1", "April 1")
    private var tabLayout: TabLayout? = null
    private var rv: RecyclerView? = null
    val animalsArray = arrayOf(
        "Recent Works",
        "Bookmark",
        "Followed"
    )
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentUserBinding.inflate(inflater, container, false)

        binding.ivSeting.setOnClickListener{
            startActivity(Intent(context, OptionActivity::class.java))
        }
        return binding.root
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabs
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = animalsArray[position]
        }.attach()
//        binding.rv.apply{
//            layoutManager = LinearLayoutManager(activity)
//            adapter = NovelItemAdapter()
//        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}