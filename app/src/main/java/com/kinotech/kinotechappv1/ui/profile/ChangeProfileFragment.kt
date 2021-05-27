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

    companion object {
        private const val REQUEST_CODE = 1
        private const val PERMISSION_CODE = 2
    }

    private lateinit var binding: ChangeProfileBinding
    private lateinit var model: ProfileSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        profileViewModel =
//            ViewModelProvider(this).get(ProfileViewModel::class.java)
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

        model = ViewModelProvider(requireActivity()).get(ProfileSharedViewModel::class.java)
        model.getPhoto().observe(viewLifecycleOwner, {
            binding.changePhoto.setImageURI(it)
//            binding.changeName.setText(it.toString())
        })
//        model.getPhoto().observe(viewLifecycleOwner, {
//            binding.changePhoto.setImageURI(it)
//        })
        binding.saveButton.setOnClickListener {
            model.putPhoto(binding.changePhoto.drawable.toString().toUri())
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
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
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
//    }

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
            if (uri != null) {
                model.putPhoto(uri)
            }
//            prefs?.edit()?.putString("profilePic", uri.toString())?.apply()
        }
    }
}
