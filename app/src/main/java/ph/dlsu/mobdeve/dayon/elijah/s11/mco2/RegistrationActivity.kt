package ph.dlsu.mobdeve.dayon.elijah.s11.mco2

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityRegistrationBinding
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

class RegistrationActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding:ActivityRegistrationBinding
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("mm/dd/yyyy",Locale.JAPAN)
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
        val email = binding.etSEmailAddress.text.toString()
        val pass = binding.etSPassword.text.toString()
        val confpass = binding.etSConfPassword.text.toString()
        // check pass
        if (email.isEmpty() || pass.isEmpty() || confpass.isEmpty()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confpass) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }
        // If all credential are correct
        // We call createUserWithEmailAndPassword
        // using auth object and pass the
        // email and pass in it.
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully Singed Up", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Log.w(TAG, "signInWithCustomToken:failure", it.exception)
                Toast.makeText(this, "Singed Up Failed!", Toast.LENGTH_SHORT).show()
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