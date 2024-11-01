package com.example.sra

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.content.ContentProviderCompat
import androidx.navigation.fragment.findNavController
import com.example.sra.databinding.FragmentLoginBinding


class Login : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private var listener: OnBottomNavVisibilityListener? = null//
    private lateinit var sharedPreferences: SharedPreferences

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNewUser.setOnClickListener {
            findNavController().navigate(R.id.action_Login_to_NewUserScreen)
        }

        val db = DBHelper(requireContext().applicationContext,null)
        db.addStock("martin","APL","10/30/29",10)
        db.addStock("mark","MST","10/30/29",20)

/*        binding.buttonCreateDb.setOnClickListener {
            val context = requireContext().applicationContext
//            context.deleteDatabase(DBHelper.DATABASE_NAME)

            val db = DBHelper(requireContext().applicationContext, null)
            db.addName("john", "123")
            db.addName("nick", "123")
            db.addName("jack", "123")

            val cursor = db.getAllUsers()
            val count = cursor?.getCount()
            Log.i("myapp", "databased and users created : " + count.toString())

            while (cursor != null && cursor.moveToNext()){
                val username = cursor.getString(cursor.getColumnIndex(DBHelper.USERNAME_COl))
                val password = cursor.getString(cursor.getColumnIndex(DBHelper.PASSWORD_COL))
                Log.i("myapp", "\t" + username + ", " + password)
            }

            cursor?.close()
        }*/

        binding.buttonLogin.setOnClickListener {
            var username = _binding?.editTextUsername?.text.toString()
            var password = _binding?.editTextPassword?.text.toString()
            Log.i("myapp", username)
            Log.i("myapp", password)
            //val db = DBHelper(requireContext().applicationContext, null)
            var cursor = db.getUser(username, password)
            val count = cursor?.getCount()

            if (count != null) {
                if (count > 0) {
                    findNavController().navigate(R.id.action_Login_to_Home)
                    listener?.showBottomNavigationView()
                    sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    val UserLogged = username
                    saveUsername(UserLogged)
                }
                else
                    findNavController().navigate(R.id.action_Login_to_Recovery)
            }
            else
                findNavController().navigate(R.id.action_Login_to_Recovery)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBottomNavVisibilityListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnBottomNavVisibilityListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OnBottomNavVisibilityListener {
        fun showBottomNavigationView()
    }

    private fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString("logged_in_username", username)
        editor.apply() // Save the username
    }
}

