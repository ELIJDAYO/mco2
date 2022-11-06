package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.R
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityRegistrationBinding
import java.text.SimpleDateFormat
import java.util.*

class RegistrationActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding:ActivityRegistrationBinding
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("mm-dd-yyyy",Locale.TAIWAN)
    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth
    private var chosenyear: Int = 0
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialising auth object
        auth = Firebase.auth

        val gender = resources.getStringArray(R.array.Gender)
        val spinner = binding.spGender
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, gender)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    Toast.makeText(this@RegistrationActivity,
                        getString(R.string.selected_item) + " " +
                                "" + gender[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        binding.btnSSigned.setOnClickListener {
            signUpUser()
        }
        // switching from signUp Activity to Login Activity
        binding.tvRedirectLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnYear.setOnClickListener {
            DatePickerDialog(this,this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun signUpUser() {
        val username = binding.etUsername.text.toString()
        val email = binding.etSEmailAddress.text.toString()
        val pass = binding.etSPassword.text.toString()
        val confpass = binding.etSConfPassword.text.toString()
        val birthday = binding.tvAge.text.toString()
        val gender = binding.spGender.selectedItem.toString()
        // check pass
        if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || confpass.isEmpty() || birthday.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Some field/s is/are blank blank", Toast.LENGTH_SHORT).show()
            return
        }

        else if (pass != confpass) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }
        else {
            val progressDialog= ProgressDialog(this@RegistrationActivity)
            progressDialog.setTitle("SignUp")
            progressDialog.setMessage("Please wait...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        saveUserInfo(username, email, pass, formatter.parse(birthday), gender, progressDialog)
                    }
                    else
                    {
                        val message=task.exception!!.toString()
                        Toast.makeText(this,"Error : $message", Toast.LENGTH_LONG).show()
                        auth.signOut()
                        progressDialog.dismiss()
                    }
                }
        }
    }
    private fun saveUserInfo(
        userName: String,
        email: String,
        password: String,
        birthday: Date,
        gender: String,
        progressDialog: ProgressDialog
    ) {
        val currentUserId=FirebaseAuth.getInstance().currentUser!!.uid
        val userRef : DatabaseReference=FirebaseDatabase.getInstance().reference.child("Users")

        val userMap=HashMap<String,Any>()
        userMap["uid"]=currentUserId
        userMap["fullname"]=fullName
        userMap["username"]=userName.toLowerCase()
        userMap["email"]=email
        userMap["bio"]="Hey! I am using InstaApp"
        userMap["image"]="gs://instagram-clone-app-205f9.appspot.com/Default images/profile.png"


        userRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener {task ->
                if(task.isSuccessful)
                {
                    Toast.makeText(this,"Account has been created",Toast.LENGTH_SHORT).show()


                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(currentUserId)
                        .child("Following").child(currentUserId)
                        .setValue(true)


                    val intent=Intent(this@SignUpActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    val message=task.exception!!.toString()
                    Toast.makeText(this,"Error : $message", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }
    }
    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        Log.e("Calendar","$p1 -- $p2 -- $p3")
        calendar.set(p1,p2,p3)
        displayFormattedDate(calendar.timeInMillis)
    }
    private fun displayFormattedDate(timestamp:Long){
        binding.tvAge.text = formatter.format(timestamp)
    }
}