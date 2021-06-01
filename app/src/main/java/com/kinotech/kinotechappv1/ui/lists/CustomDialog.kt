package com.kinotech.kinotechappv1.ui.lists

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
            var fullName = editTextFullName.text.toString()
            if (fullName.isEmpty()) {
                Toast.makeText(
                    this.context,
                    "Введите название", Toast.LENGTH_LONG
                ).show()
                fullName = "Без названия"
            }
            user?.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Lists")
                    .child(it1.toString())
                    .child("UserLists")
                    .child(fullName)
                    .setValue(fullName)
            }
            user?.uid.let { it1 ->
                FirebaseDatabase.getInstance().reference
                    .child("Lists")
                    .child(it1.toString())
                    .child(fullName)
                    .child("IsOpened")
                    .setValue(false)
            }


            dismiss() // Close Dialog
            listener.fullNameEntered(fullName)
        }
    }

//    private fun checkForDuplicates(var fullName: String)  {
//        val duplicateRef = user?.uid.let { it1 ->
//            FirebaseDatabase.getInstance().reference
//                .child("Lists")
//                .child(it1.toString())
//                .child("UserLists")
//                .child(fullName)
//        }
//        duplicateRef.addValueEventListener(object: ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for(snap in snapshot.children){
//                    if (fullName == snap.value){
//
//                    }
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }

}
