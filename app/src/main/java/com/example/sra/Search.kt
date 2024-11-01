package com.example.sra

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sra.databinding.FragmentSearchBinding
import android.view.Menu
import android.view.MenuItem
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.widget.SearchView


class Search : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("logged_in_username", null)?:""

        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val tableLayout = binding.tableLayout


        //val db = DBHelper(requireContext().applicationContext,null)
        //db.addStock("martin","APL","10/30/29",10)
        //db.addStock("mark","MST","10/30/29",20)

        // Set up the search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    tableLayout.removeAllViews()

                    // Search in the database for matching usernames
                    val dbHelper = DBHelper(requireContext(), null)
                    val cursor : Cursor? = dbHelper.searchStock(username,query)


                    if (cursor != null && cursor.moveToFirst()) {
                        // Iterate over the results and display each one
                        do {
                            val stockName = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.STOCK_NAME_COL))
                            val stockAmount = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.STOCK_AMOUNT))

                            val tableRow = TableRow(context)
                            val textView = TextView(context)
                            val stockamounttextView = TextView(context)

                            textView.text = stockName
                            textView.textSize = 18f
                            textView.setPadding(30, 16, 16, 16)
                            tableRow.addView(textView)

                            stockamounttextView.text = stockAmount
                            stockamounttextView.textSize = 18f
                            stockamounttextView.setPadding(30, 16, 16, 16)
                            tableRow.addView(stockamounttextView)

                            tableLayout.addView(tableRow)
                        } while (cursor.moveToNext())

                        cursor.close()
                    } else {
                        // Display "No results found" if no matches
                        val textView = TextView(context)
                        val tableRow = TableRow(context)
                        textView.text = "No results found"
                        textView.textSize = 18f
                        textView.setPadding(16, 16, 16, 16)
                        tableLayout.addView(tableRow)
                        tableLayout.addView(textView)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Clear the layout when the query is changed or empty
                if (newText.isNullOrEmpty()) {
                    //linearLayout.removeAllViews()
                    tableLayout.removeAllViews()
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}