package com.example.berberrezervasyon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.berberrezervasyon.databinding.FragmentLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginScreen : Fragment() {

    private var _binding: FragmentLoginScreenBinding? = null

    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.girisYapButton.setOnClickListener { girisYap(it) }
        binding.sifreniMiUnuttun.setOnClickListener { sifreniMiUnuttun(it) }
        binding.hemenUyeOl.setOnClickListener { goToRegister(it) } // Burada doğru şekilde çağırılıyor
        binding.griButton.setOnClickListener{googleIleGiris(it)} // google auth

        val mevcutKullanici = auth.currentUser
        if (mevcutKullanici!=null){
            //kullanıcı daha önceden giriş yapmış
            val action = LoginScreenDirections.actionLoginScreenToHomePage()
            Navigation.findNavController(view).navigate(action)

        }

    }

    fun goToRegister(view: View) {

        binding.hemenUyeOl.setOnClickListener {
            val action = LoginScreenDirections.actionLoginScreenToRegisterScreen()
            Navigation.findNavController(it).navigate(action)


        }

    }

    fun girisYap(view: View) {
        val mail = binding.mailBoslugu.text.toString()
        val sifre = binding.sifreBoslugu.text.toString()

        if (mail.isNotEmpty()&&sifre.isNotEmpty()){
            auth.signInWithEmailAndPassword(mail,sifre).addOnSuccessListener{task->
                    val action = LoginScreenDirections.actionLoginScreenToHomePage()
                    Navigation.findNavController(view).navigate(action)

            }.addOnFailureListener{exception->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

        println("Giriş Yap Tıklandı")

    }

    fun sifreniMiUnuttun (view: View) {
        println("Şifreni mi unuttun tıklandı")
    }

    fun googleIleGiris(view: View){

        //AD:45:0C:23:30:52:B6:B5:3F:86:99:62:B1:92:7C:AB:6E:39:E0:5E <- sha kod

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}