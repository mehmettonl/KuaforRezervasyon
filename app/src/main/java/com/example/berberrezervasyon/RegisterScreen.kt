package com.example.berberrezervasyon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.berberrezervasyon.databinding.FragmentHomePageBinding
import com.example.berberrezervasyon.databinding.FragmentLoginScreenBinding
import com.example.berberrezervasyon.databinding.FragmentRegisterScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.core.OrderBy.Direction
import com.google.firebase.ktx.Firebase


class RegisterScreen : Fragment() {
    private var _binding: FragmentRegisterScreenBinding? = null
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
        _binding = FragmentRegisterScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.kayitButton.setOnClickListener { kayitOl(it) }
    }

    fun kayitOl(view: View) {

        val email = binding.editTextEmail.text.toString()
        val sifre = binding.editTextSifre.text.toString()

        if (email.isNotEmpty()&& sifre.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener{task->
                if (task.isSuccessful){
                    val action = RegisterScreenDirections.actionRegisterScreenToHomePage()
                    Navigation.findNavController(view).navigate(action)
                }
            }.addOnFailureListener{ exception->

                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
           }

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}