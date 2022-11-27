package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.RemoveBookmarkAdapter
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityRemoveBookmarkBinding

class RemoveBookmarkActivity : AppCompatActivity() {

    private var bookmarkList:ArrayList<String> = ArrayList<String>()
    private lateinit var binding: ActivityRemoveBookmarkBinding
    private lateinit var removeBookmarkAdapter: RemoveBookmarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoveBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookmarkList.add("Classroom of the Elite 1")
        bookmarkList.add("Classroom of the Elite 2")
        bookmarkList.add("Classroom of the Elite 3")

        binding.removeBookmarkRV.layoutManager = LinearLayoutManager(applicationContext)
        removeBookmarkAdapter = RemoveBookmarkAdapter(applicationContext, bookmarkList, "view")
        binding.removeBookmarkRV.adapter = removeBookmarkAdapter

    }
}