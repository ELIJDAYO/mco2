package ph.dlsu.mobdeve.dayon.elijah.s11.mco2.activities

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ph.dlsu.mobdeve.dayon.elijah.s11.mco2.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // initialising Firebase auth object

        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.tvRedirectSignUp.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            // using finish() to end the activity
            finish()
        }
    }
    private fun login() {
        var email = binding.etEmailAddress.text.toString()
        var pass = binding.etPassword.text.toString()

        if (email.isBlank() || pass.isBlank() ) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }
        val progressDialog= ProgressDialog(this@LoginActivity)
        progressDialog.setTitle("Login")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {

            if (it.isSuccessful) {
//                val editor = prefs.edit()
//                editor.putString("profileId",)
//                editor.putString("email", email)
//                editor.putString("password", pass)
//                editor.apply()

                progressDialog.dismiss()

                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                val intent=Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
//                Log.w(ContentValues.TAG, "signInWithCustomToken:failure", it.exception)
                Toast.makeText(this,"Error : $it.exception", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
                auth.signOut()
                progressDialog.dismiss()
            }
        }
    }
}