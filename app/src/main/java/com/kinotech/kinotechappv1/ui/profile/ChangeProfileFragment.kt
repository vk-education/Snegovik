package com.kinotech.kinotechappv1.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.ChangeProfileBinding
import java.util.Locale
import kotlin.collections.HashMap

class ChangeProfileFragment : Fragment() {

    private lateinit var firebaseUser: FirebaseUser
    private var storagePhoto: StorageReference? = null
    private var checker = ""
    private var userUrl = ""
    private var photoUri: Uri? = null
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

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
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storagePhoto = FirebaseStorage.getInstance().reference.child("Profile Photo")
        binding = ChangeProfileBinding.inflate(inflater, container, false)

        binding.apply {
            userInfo(changeName, root, changePhoto)
        }
        binding.changePhotoButton.setOnClickListener {
            checker = "clicked"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context?.let { it1 ->
                    PermissionChecker.checkSelfPermission(
                        it1,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } == PackageManager.PERMISSION_DENIED) {
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
            if (checker == "clicked") {
                Log.d("photoUri", "onCreateView:$photoUri ")
                uploadPhotoAndInfo()
            } else {
                updateUserInfoOnly()
            }
            loadFragment()
        }

        binding.backBtnCh.setOnClickListener {
            loadFragment()
        }
        return binding.root
    }

    private fun cErr(v: Context) {
        PermissionChecker.checkSelfPermission(
            v,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun userInfo(nickName: TextView, v: View, img: ImageView) {
        val usersRef = user?.uid?.let {
            FirebaseDatabase.getInstance().reference
                .child("Users").child(it)
        }
        usersRef?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    nickName.text = p0.child("fullName").value.toString()
                    Glide
                        .with(v.context)
                        .load(p0.child("photo").value.toString())
                        .into(img)
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }

    private fun updateUserInfoOnly() {
        val userMap = HashMap<String, Any?>()
        userMap["fullName"] = binding.changeName.text.toString().toLowerCase(Locale.getDefault())
        user?.let {
            FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(it.uid)
                .updateChildren(userMap)
        }
    }

    private fun uploadPhotoAndInfo() {
        val userMap = HashMap<String, Any?>()
        val fileRef = storagePhoto!!.child(firebaseUser.uid + "jpg")
        fileRef.putFile(photoUri!!).addOnCompleteListener {
            fileRef.downloadUrl.addOnCompleteListener {
                userUrl = it.result.toString()
                Log.d("photoUri", "onCreateViewURL:$userUrl")
                userMap["fullName"] = binding.changeName.text.toString()
                    .toLowerCase(Locale.getDefault())
                userMap["photo"] = userUrl
                user?.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Users")
                        .child(it1.uid)
                        .updateChildren(userMap)
                }
            }
        }
        Log.d("photoUri", "onCreateView:$fileRef ")
        Log.d("photoUri", "onCreateView:$userUrl ")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            photoUri = data?.data
            binding.changePhoto.setImageURI(photoUri)
        }
    }

    private fun loadFragmentch(editTextInput: String) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            val bun = Bundle()
            val profileFragment = ProfileFragment()
            bun.putString("message", editTextInput)
            profileFragment.arguments = bun
            transaction.replace(R.id.container, profileFragment)
            transaction.disallowAddToBackStack()
            transaction.commit()
        }
    }

    private fun loadFragment() {
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
}
