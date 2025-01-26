package com.example.berberrezervasyon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.berberrezervasyon.databinding.FragmentHomePageBinding
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth

class HomePage : Fragment() {
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Argümanları al
        val args: HomePageArgs by navArgs()
        val email = args.email
        val name = args.name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}