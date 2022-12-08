package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.adapter.*
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityCreateNewNovelBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityFrontEndNovelBinding
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Episode
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Novel
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.Tag
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.model.User
import kotlin.system.measureTimeMillis


class EditExistingNovelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNewNovelBinding
    private var tagNameList= arrayListOf<String>()
    private lateinit var profileId: String
    private var imageUri:String = ""
    private lateinit var tagAdapter: TagRemoveAdapter
    private lateinit var tagRef:DatabaseReference
    lateinit var novelRef: DatabaseReference
    private var storageProfileRef: StorageReference?=null
    private lateinit var novelId:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageProfileRef = FirebaseStorage.getInstance().reference.child("Pictures")
        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid
        fetchNovel()
        CoroutineScope(IO).launch{
            readData1()
        }
        tagAdapter = TagRemoveAdapter(this@EditExistingNovelActivity, tagNameList,novelId)
        binding.rvTags.adapter = tagAdapter

        binding.rvTags.layoutManager = LinearLayoutManager(applicationContext)
        binding.btnCreateEpisode.setText("Update Novel Detail")
        binding.tvAppBarTitle.setText("Edit Novel")
        binding.ibBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnCreateEpisode.setOnClickListener {
            addNovel()
        }
        Log.e(TAG,"EditExistingNovelActivity uri $imageUri")
        binding.tvThumbnail.setOnClickListener{
            if(!imageUri.isNullOrBlank()) {
                deleteThumbnail()
            }
            val galleryIntent = Intent(Intent.ACTION_PICK)
//             here item is type of image
            galleryIntent.type = "image/*"
//             ActivityResultLauncher callback
            imagePickerActivityResult.launch(galleryIntent)
        }
        binding.addTags.setOnClickListener {
            val tagDialog = AlertDialog.Builder(this@EditExistingNovelActivity)
            tagDialog.setTitle("Input Novel Tag")

            val etTag = EditText(this@EditExistingNovelActivity)
            etTag.inputType = InputType.TYPE_CLASS_TEXT
            tagDialog.setView(etTag)

            tagDialog.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val tag = etTag.text.toString()
                    tagNameList.add(tag)
                    tagAdapter.notifyDataSetChanged()
                    Log.e(TAG,"Whas in tagNameList? $tagNameList")
                }
            })
            tagDialog.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    if (p0 != null) {
                        p0.cancel()
                    }
                }
            })
            tagDialog.show()
        }
    }
    private suspend fun readData1(){
        withContext(IO){
            val executionTime = measureTimeMillis {
                async{
                    println("debug: launching 1st job: ${Thread.currentThread().name}")
                    setupTags()
                }.await()
            }
            Log.e(ContentValues.TAG,"debug: job1 and job2 are complete. It took ${executionTime} ms")

        }
    }
    private suspend fun readData2(){
        withContext(Main){
            val executionTime = measureTimeMillis {
                async{
                    println("debug: launching 1st job: ${Thread.currentThread().name}")
                    deleteFromTags()
                }.await()
            }
            Log.e(ContentValues.TAG,"debug: job1 and job2 are complete. It took ${executionTime} ms")

        }
    }
    private suspend fun setupTags(){
        withContext(Main){
            novelId = intent.getStringExtra("novelId").toString()
            tagRef = FirebaseDatabase.getInstance().getReference("Tags")
            var query = tagRef.orderByChild("novelId")
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    tagNameList.clear()
                    for (element in snapshot.children) {
                        val tag = element.getValue(Tag::class.java)
                        if(tag!!.getNovelId()==novelId && tag.getTagName() !in tagNameList) {
                            tagNameList.add(tag.getTagName())
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            delay(300)
            tagAdapter = TagRemoveAdapter(this@EditExistingNovelActivity, tagNameList,novelId)
            binding.rvTags.adapter = tagAdapter
        }
    }
    private suspend fun deleteFromTags(){
        withContext(Main){
            novelId = intent.getStringExtra("novelId").toString()
            tagRef = FirebaseDatabase.getInstance().getReference("Tags")
            var query = tagRef.orderByChild("novelId")
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (element in snapshot.children) {
                        val tag = element.getValue(Tag::class.java)
                        if((tag!!.getNovelId()==novelId) && tag.getTagName() !in tagNameList) {
                            element.ref.removeValue()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            delay(300)
        }
    }
    private fun fetchNovel(){
        novelId = intent.getStringExtra("novelId").toString()
        novelRef = FirebaseDatabase.getInstance().getReference("Novels")
        var query = novelRef.orderByChild("novelId").equalTo(novelId)
        query.addValueEventListener(object:ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()){
                for (element in snapshot.children){
                    val novel = element.getValue(Novel::class.java)
                    binding.etTitle.setText(novel!!.getTitle())
                    binding.etSynopsis.setText(novel.getSynopsis())
                    imageUri = novel.getImageUri()
                    binding.tvThumbnail.text = imageUri
                    return
                }
            }
        }
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })


}
    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                // getting URI of selected Image
                val imageUri: Uri? = result.data?.data

                // val fileName = imageUri?.pathSegments?.last()

                // extract the file name with extension
                val sd = getFileName(applicationContext, imageUri!!)
                // Upload Task with upload to directory 'file'
                // and name of the file remains same
                val uploadTask = storageProfileRef?.child("$profileId/$sd")?.putFile(imageUri!!)
                // On success, download the file URL and display it
                uploadTask?.addOnSuccessListener {
                    val downloadUrl = storageProfileRef?.child("$profileId/$sd")?.downloadUrl
                    // using glide library to display the image
                    downloadUrl?.addOnSuccessListener {
                        this.imageUri = it.toString()

                        Log.e("Firebase", "download passed")
                    }?.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                }?.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }
            }
        }
    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }
    private fun deleteThumbnail(){
        val ref = FirebaseStorage.getInstance().getReferenceFromUrl(imageUri)
        ref.delete()
        imageUri = ""
    }
    private fun addNovel(){
        val title = binding.etTitle.text.toString()
        val synopsis = binding.etSynopsis.text.toString()
        if (title.isBlank() || synopsis.isBlank()){
            Toast.makeText(this,"Either field is blank", Toast.LENGTH_SHORT).show()
            return
        }else{
            this.profileId = FirebaseAuth.getInstance().currentUser!!.uid
            val novelRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Novels")
            val novelMap=HashMap<String,Any>()

            novelMap["title"] = title
            novelMap["synopsis"] = synopsis
            novelMap["imageUri"] = imageUri

            novelRef.child(novelId).updateChildren(novelMap)

            val tagRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Tags")
            val tagMap=HashMap<String, Any>()

            CoroutineScope(Main).launch {
                readData2()
            }
            for (tag in tagNameList){
                tagMap["novelId"] = novelId
                tagMap["tagName"] = tag
                tagRef.setValue(tagMap)
            }


            val intent = Intent(this, FrontEndEditNovelActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}