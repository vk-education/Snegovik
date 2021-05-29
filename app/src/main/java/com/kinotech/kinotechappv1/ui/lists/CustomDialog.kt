package com.kinotech.kinotechappv1.ui.lists

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.kinotech.kinotechappv1.R
import kotlin.random.Random

class CustomDialog(context: Context, private var listener: FullNameListener) : Dialog(context) {

    interface FullNameListener {
        fun fullNameEntered(fullName: String)
    }
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var editTextFullName: EditText
    private lateinit var buttonCreate: Button
    private lateinit var buttonCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)

        this.editTextFullName = findViewById(R.id.editTextList)
        this.buttonCancel = findViewById(R.id.cancel_list)
        this.buttonCreate = findViewById(R.id.create_list)
        this.buttonCancel.setOnClickListener {
            this.dismiss()
        }

        this.buttonCreate.setOnClickListener {
            val fullName = editTextFullName.text.toString()
            val listId = (0..1e12.toInt()).random()
            Log.d("db", "click $listId")
            user?.uid.let{it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Lists")
                    .child(it1.toString())
                    .child("UserLists")
                    //.child(listId.toString())
                    .child(fullName)
                    .setValue(true)
            }
            if (fullName.isEmpty()) {
                Toast.makeText(
                    this.context,
                    "Введите название", Toast.LENGTH_LONG
                ).show()
            }
            dismiss() // Close Dialog

            listener.fullNameEntered(fullName)
        }
    }
}
