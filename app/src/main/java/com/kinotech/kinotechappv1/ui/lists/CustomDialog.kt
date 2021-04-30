package com.kinotech.kinotechappv1.ui.lists

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.kinotech.kinotechappv1.R

class CustomDialog(context: Context, private var listener: FullNameListener) : Dialog(context) {

    interface FullNameListener {
        fun fullNameEntered(fullName: String)

    }

    private lateinit var editTextFullName: EditText
    private lateinit var buttonCreate: Button
    private lateinit var buttonCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        this.editTextFullName = findViewById(R.id.editTextList)
        this.buttonCancel = findViewById<Button>(R.id.cancel_list)
        this.buttonCreate = findViewById<Button>(R.id.create_list)
        this.buttonCancel.setOnClickListener(View.OnClickListener() {
            this.dismiss()
        });

        this.buttonCreate.setOnClickListener(View.OnClickListener() {
            val fullName = editTextFullName.text.toString()

            if (fullName.isEmpty()) {
                Toast.makeText(this.context,
                    "Введите название", Toast.LENGTH_LONG).show()
            }
            dismiss() // Close Dialog

            listener.fullNameEntered(fullName)
        });

    }

}

