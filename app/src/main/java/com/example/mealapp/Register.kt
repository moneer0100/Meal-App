package com.example.mealapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    var isPasswordVisible = false
    var isConfirmPasswordVisible = false
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        auth = FirebaseAuth.getInstance()
        oneTapClient = Identity.getSignInClient(this)
        val googleSignInBtn = findViewById<ImageView>(R.id.imageView8)
        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmpass)
        val registerButton = findViewById<Button>(R.id.button)
        val login =findViewById<TextView>(R.id.textView9)
        val showpass=findViewById<ImageView>(R.id.imageView6)
        val showpass2=findViewById<ImageView>(R.id.imageView7)
        val progressBar = findViewById<ProgressBar>(R.id.progresbarid)
        showpass.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordEditText.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                showpass.setImageResource(R.drawable.p) // Replace with open-eye icon
            } else {
                passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                showpass.setImageResource(R.drawable.p) // Replace with closed-eye icon
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        showpass2.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            if (isConfirmPasswordVisible) {
                confirmPasswordEditText.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                showpass2.setImageResource(R.drawable.p) // Replace with open-eye icon
            } else {
                confirmPasswordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                showpass2.setImageResource(R.drawable.p) // Replace with closed-eye icon
            }
            confirmPasswordEditText.setSelection(confirmPasswordEditText.text.length)
        }
        login.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirm = confirmPasswordEditText.text.toString().trim()
            if (name.isEmpty()) {
                nameEditText.error = "Full name is required"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                emailEditText.error = "Email is required"
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Enter a valid email"
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordEditText.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            if (confirm != password) {
                confirmPasswordEditText.error = "Passwords do not match"
                return@setOnClickListener
            }

            // Firebase registration
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Registration failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            progressBar.visibility = View.VISIBLE

            // Simulate login process (replace with actual login logic)
            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                // Navigate to another activity if needed
                // startActivity(Intent(this, HomeActivity::class.java))
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
        }}
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