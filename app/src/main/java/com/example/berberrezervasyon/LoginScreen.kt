package com.example.berberrezervasyon

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.example.berberrezervasyon.databinding.FragmentLoginScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginScreen : Fragment() {

    private var _binding: FragmentLoginScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var googleIleGiris: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase Authentication tanımlaması
        auth = Firebase.auth

        // Google Sign-In seçenekleri tanımlama
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Google Sign-In Client tanımlama
        googleIleGiris = GoogleSignIn.getClient(requireContext(), gso)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Giriş yapma işlemi
        binding.girisYapButton.setOnClickListener { girisYap(it) }

        // Şifre sıfırlama işlemi
        binding.sifreniMiUnuttun.setOnClickListener { sifreniMiUnuttun(it) }

        // Kayıt ekranına yönlendirme
        binding.hemenUyeOl.setOnClickListener {
            val action = LoginScreenDirections.actionLoginScreenToRegisterScreen()
            Navigation.findNavController(it).navigate(action)
        }

        // Google ile giriş işlemi
        binding.griButton.setOnClickListener { googleIleGiris() }

        // Kullanıcı oturum kontrolü
        val mevcutKullanici = auth.currentUser
        if (mevcutKullanici != null) {
            val action = LoginScreenDirections.actionLoginScreenToHomePage()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun girisYap(view: View) {
        val mail = binding.mailBoslugu.text.toString()
        val sifre = binding.sifreBoslugu.text.toString()

        if (mail.isNotEmpty() && sifre.isNotEmpty()) {
            auth.signInWithEmailAndPassword(mail, sifre).addOnSuccessListener {
                val action = LoginScreenDirections.actionLoginScreenToHomePage()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun.", Toast.LENGTH_LONG).show()
        }
    }

    private fun sifreniMiUnuttun(view: View) {
        Toast.makeText(requireContext(), "Şifrenizi sıfırlamak için e-posta gönderildi.", Toast.LENGTH_LONG).show()
    }

    private fun googleIleGiris() {
        val signInIntent = googleIleGiris.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResult(task)
        } else {
            Toast.makeText(requireContext(), "Google ile giriş başarısız.", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val action = LoginScreenDirections.actionLoginScreenToHomePage(
                    email = account.email ?: "",
                    name = account.displayName ?: ""
                )
                Navigation.findNavController(requireView()).navigate(action)
            } else {
                Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}