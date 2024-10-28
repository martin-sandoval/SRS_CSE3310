package com.example.sra

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sra.databinding.NewUserBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NewUserScreen : Fragment() {

    private var _binding: NewUserBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

            override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
            _binding = NewUserBinding.inflate(inflater, container, false)
    return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ButtonCreateUser.setOnClickListener {
            var usernameNew = _binding?.editNewName?.text.toString()
            var passwordNew = _binding?.editNewPassword?.text.toString()

            val db = DBHelper(requireContext().applicationContext, null)
            db.addName(usernameNew,passwordNew)
            db.getUser(usernameNew,passwordNew)
            findNavController().navigate(R.id.action_NewUserScreen_to_Login)
        }

//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
