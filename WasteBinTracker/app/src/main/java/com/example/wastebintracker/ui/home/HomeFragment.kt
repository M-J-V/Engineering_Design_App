package com.example.wastebintracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wastebintracker.R
import com.example.wastebintracker.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var home: View
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        home = inflater.inflate(R.layout.fragment_home, container, false)
        var autoDate = home.findViewById<TextView>(R.id.autoDate)
        val dateNow = Calendar.getInstance().time
        autoDate.setText(dateNow.toString().dropLast(23))

        return home
    }

    override fun onStart() {
        super.onStart()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}