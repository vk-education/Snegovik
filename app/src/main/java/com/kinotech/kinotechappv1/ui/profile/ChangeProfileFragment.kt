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
import android.database.DatabaseErrorHandler
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.databinding.ChangeProfileBinding
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage

class ChangeProfileFragment : Fragment() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var photoAcc: ImageView
    private lateinit var nickName: TextView
    private var checker = ""
    private var userUrl = ""
    private var photoUri: Uri? = null
    private var storagePhoto: StorageReference? = null
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    companion object {
        private const val REQUEST_CODE = 1
        private const val PERMISSION_CODE = 2
    }

    private lateinit var binding: ChangeProfileBinding
    //private lateinit var model: ProfileSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        profileViewModel =
//            ViewModelProvider(this).get(ProfileViewModel::class.java)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storagePhoto = FirebaseStorage.getInstance().reference.child("Profile Photo")
        binding = ChangeProfileBinding.inflate(inflater, container, false)
        binding.apply {
            userInfo(changeName, root, changePhoto)
            }
        binding.changePhotoButton.setOnClickListener{
            checker = "clicked"
            CropImage.activity()
                .setAspectRatio(1,1)
                .start(requireActivity())
        }
//        binding.changePhotoButton.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (context?.let { it1 ->
//                        PermissionChecker.checkSelfPermission(
//                            it1,
//                            Manifest.permission.READ_EXTERNAL_STORAGE
//                        )
//                    } ==
//                    PackageManager.PERMISSION_DENIED) {
//
//                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    requestPermissions(permissions, PERMISSION_CODE)
//                } else {
//                    openGallery()
//                }
//            } else {
//                openGallery()
//            }
//        }

//        model = ViewModelProvider(requireActivity()).get(ProfileSharedViewModel::class.java)
//        model.getPhoto().observe(viewLifecycleOwner, {
//            binding.changePhoto.setImageURI(it)
////            binding.changeName.setText(it.toString())
//        })
//        model.getPhoto().observe(viewLifecycleOwner, {
//            binding.changePhoto.setImageURI(it)
//        })
//        binding.saveButton.setOnClickListener {
//            //model.putPhoto(binding.changePhoto.drawable.toString().toUri()
//            loadfragmentch(binding.changeName.text.toString())
//        }
        binding.saveButton.setOnClickListener {
            if (checker == "clicked"){
                uploadPhotoAndInfo()

            }
            else{
                updateUserInfoOlnly()
            }
            loadfragment()
        }

        binding.backBtnCh.setOnClickListener {
            loadfragment()
        }
        return binding.root
    }

    private fun updateUserInfoOlnly() {
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
        val userMap = HashMap<String, Any?>()
        userMap["fullName"] = binding.changeName.text.toString().toLowerCase()
        usersRef.updateChildren(userMap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            val result = CropImage.getActivityResult(data)
            photoUri = result.uri
            binding.changePhoto.setImageURI(photoUri)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        model = ViewModelProvider(requireActivity()).get(ProfileSharedViewModel::class.java)
//        model.getPhoto().observe(viewLifecycleOwner, {
//            binding.changePhoto.setImageURI(it)
////            binding.changeName.setText(it.toString())
//        })
////        model.getPhoto().observe(viewLifecycleOwner, {
////            binding.changePhoto.setImageURI(it)
////        })
//        binding.saveButton.setOnClickListener {
//            model.putPhoto(binding.changePhoto.drawable.toString().toUri())
//            loadfragment()
//            loadfragmentch(binding.changeName.text.toString())
//        }
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

//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, REQUEST_CODE)
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            PERMISSION_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] ==
//                    PackageManager.PERMISSION_GRANTED
//                ) {
//                    openGallery()
//                } else {
//                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
//            val uri = data?.data
//            binding.changePhoto.setImageURI(uri)
//            if (uri != null) {
//                //model.putPhoto(uri)
//            }
////            prefs?.edit()?.putString("profilePic", uri.toString())?.apply()
//        }
//    }

//    private fun userInfo()
//    {
//        nickName.text = firebaseUser.displayName
//        Glide
//            .with(this)
//            .load(firebaseUser.photoUrl)
//            .error(R.drawable.ic_like_40dp)
//            .into(photoAcc)
//    }

    private fun userInfo(nickName : TextView, v: View, img : ImageView ){
        val usersRef = user?.uid?.let { FirebaseDatabase.getInstance().reference.child("Users").child(it) }
        usersRef?.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot){
                nickName.text = p0.child("fullName").value.toString()
                Glide
                    .with(v.context)
                    .load(p0.child("photo").value.toString())
                    .into(img)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun uploadPhotoAndInfo(){
        val  fileRef = storagePhoto!!.child(firebaseUser!!.uid + "jpg")
        var uploadTask: StorageTask <*>
        uploadTask = fileRef.putFile(photoUri!!)
        uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (task.isSuccessful){
                task.exception?.let{
                    throw it
                }
            }
            return@Continuation fileRef.downloadUrl
        }).addOnCompleteListener{OnCompleteListener<Uri>{task ->
            if (task.isSuccessful){
                val downloadUrl = task.result
                userUrl = downloadUrl.toString()
                val ref = FirebaseDatabase.getInstance().reference.child("Users")
                val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)
                val userMap = HashMap<String, Any?>()
                userMap["fullName"] = binding.changeName.text.toString().toLowerCase()
                userMap["photo"] = userUrl
                ref.child(firebaseUser.uid).updateChildren(userMap)
            }
        }}

    }

//    private fun updateUserInfoOnly(){
//        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.uid)
//        val userMap = HashMap<String, Any>()
//        userMap["username"] = changeName.getText().toString().toLowerCase()
//    }


}
