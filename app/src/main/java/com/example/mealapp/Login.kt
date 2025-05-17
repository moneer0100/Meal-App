package com.example.mealapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.IntentSenderRequest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.android.gms.auth.api.identity.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    var isPasswordVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        auth = FirebaseAuth.getInstance()
        oneTapClient = Identity.getSignInClient(this)

        val emailEditText = findViewById<EditText>(R.id.editTextName1)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.button2)
        val registerLink = findViewById<TextView>(R.id.textView7)
        val googleSignInBtn = findViewById<ImageView>(R.id.imageView13)
        val showpass=findViewById<ImageView>(R.id.imageView12)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        showpass.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                showpass.setImageResource(R.drawable.p)
            } else {
                passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                showpass.setImageResource(R.drawable.p)
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }
        // إنتقال إلى شاشة التسجيل
        registerLink.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        // تسجيل دخول بالإيميل والباسورد
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty()) {
                emailEditText.error = "Email is required"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Enter a valid email"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Password is required"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordEditText.error = "Password must be at least 6 characters"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            // Firebase sign-in
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_LONG).show()
                    }
                }
            progressBar.visibility = View.VISIBLE


            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()


            }, 2000)
        }

        // إعداد Google Sign-In
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id)) // تأكد من إنك ضايف دا صح من google-services.json
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        // الضغط على زر تسجيل الدخول بجوجل
        googleSignInBtn.setOnClickListener {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener { result ->
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    googleSignInLauncher.launch(intentSenderRequest)

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Firebase Auth failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "No ID Token!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}

