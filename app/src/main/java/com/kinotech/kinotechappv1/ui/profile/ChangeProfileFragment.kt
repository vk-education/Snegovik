package com.kinotech.kinotechappv1.ui.profile

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.ChangeProfileBinding

class ChangeProfileFragment : Fragment() {

    //    private lateinit var firebaseUser: FirebaseUser
    private var checker = ""

    companion object {
        private const val REQUEST_CODE = 1
        private const val PERMISSION_CODE = 2
    }

    private lateinit var binding: ChangeProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        binding = ChangeProfileBinding.inflate(inflater, container, false)
        binding.changePhotoButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context?.let { it1 ->
                        PermissionChecker.checkSelfPermission(
                            it1,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    } ==
                    PackageManager.PERMISSION_DENIED) {

                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    openGallery()
                }
            } else {
                openGallery()
            }
        }

        binding.saveButton.setOnClickListener {
            loadfragment()
            loadfragmentch(binding.changeName.text.toString())
        }

        binding.saveButton.setOnClickListener {
            loadfragment()
        }

        binding.saveButton.setOnClickListener {
            loadfragmentch(binding.changeName.text.toString())
        }

        binding.backBtnCh.setOnClickListener {
            loadfragment()
        }
//        userInfo()
//        binding.saveButton.setOnClickListener {
//            if (checker == "clicked"){
//
//            }
//            else{
//              updateUserInfoOnly()
//            }
//        }
        return binding.root
    }

private fun loadfragmentch(editTextInput: String) {
    val transaction = activity?.supportFragmentManager?.beginTransaction()
    if (transaction != null) {
        val bun = Bundle()
        val profilefragment = ProfileFragment()
        bun.putString("message", editTextInput)
        profilefragment.arguments = bun
        transaction.replace(R.id.container, profilefragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}

private fun loadfragment() {
    val transaction = activity?.supportFragmentManager?.beginTransaction()
    if (transaction != null) {
        transaction.replace(R.id.container, ProfileFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}

private fun openGallery() {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    startActivityForResult(intent, REQUEST_CODE)
}

override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    when (requestCode) {
        PERMISSION_CODE -> {
            if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
        val uri = data?.data
        binding.changePhoto.setImageURI(uri)
    }
}

//    private fun updateUserInfoOnly(){
//        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.uid)
//        val userMap = HashMap<String, Any>()
//        userMap["username"] = changeName.getText().toString().toLowerCase()
//    }
//
//    private fun userInfo()
//    {
//        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.uid)
//        usersRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(p0: DataSnapshot) {
//                if (p0.exists()) {
//                    val user = p0.getValue<User>(User::class.java)
//                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(changePhoto)
//                    changeName.setText(user!!.getUsername())
//                }
//            }
//
//            override fun onCancelled(p0: DatabaseErrorHandler) {
//            }
//        })
//    }
}
