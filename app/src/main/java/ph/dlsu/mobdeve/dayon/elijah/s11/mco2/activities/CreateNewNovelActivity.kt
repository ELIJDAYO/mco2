package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.annotation.SuppressLint
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityCreateNewNovelBinding
import java.util.*
import kotlin.collections.HashMap

class CreateNewNovelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewNovelBinding
    private var tagList= arrayListOf<String>()
    private lateinit var profileId: String
    private var imageUri:String = ""

    private var storageProfileRef: StorageReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewNovelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageProfileRef = FirebaseStorage.getInstance().reference.child("Pictures")
        this.profileId = FirebaseAuth.getInstance().currentUser!!.uid

        binding.ibBack.setOnClickListener {
            val intent= Intent(this@CreateNewNovelActivity,EditNovelActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnCreateEpisode.setOnClickListener {
            addNovel()
        }
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
        binding.addTags.setOnClickListener (object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val tagDialog = AlertDialog.Builder(this@CreateNewNovelActivity)
                tagDialog.setTitle("Input Novel Tag")

                val etTag = EditText(this@CreateNewNovelActivity)
                etTag.inputType = InputType.TYPE_CLASS_TEXT
                tagDialog.setView(etTag)

                tagDialog.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val tag = etTag.text.toString()
                        tagList.add(tag)
                    }
                })
                tagDialog.setNegativeButton("Cancel",object: DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        if (p0 != null) {
                            p0.cancel()
                        }
                    }

                })
                tagDialog.show()
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
            val novelId = novelRef.push().key.toString()
            val novelMap=HashMap<String,Any>()

            novelMap["novelId"] = novelId
            novelMap["uid"] = profileId
            novelMap["title"] = title
            novelMap["synopsis"] = synopsis
            novelMap["imageUri"] = imageUri
            novelMap["numEpisodes"] = 0

            novelRef.child(novelId).setValue(novelMap)
//            novelRef.child(novelId).updateChildren(novelMap)

            val tagRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Tags")
            val tagMap=HashMap<String, Any>()
            for (tag in tagList){
                tagMap["novelId"] = novelId
                tagMap["tagName"] = tag
                tagRef.child(novelId).setValue(tagMap)
            }


            val intent = Intent(this, CreateNewEpisodeActivity::class.java)
            intent.putExtra("novelId", novelId)
            intent.putExtra("novelTitle",title)
            startActivity(intent)
            finish()
        }

    }
}