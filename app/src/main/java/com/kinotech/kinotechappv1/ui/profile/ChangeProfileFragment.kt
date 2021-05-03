package com.kinotech.kinotechappv1.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kinotech.kinotechappv1.R
import android.app.Activity;
import android.view.View.OnClickListener;
import android.widget.Button;

class ChangeProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        val binding = inflater.inflate(R.layout.change_profile, container, false)
        /*val textView = root.findViewById<TextView>(R.id.text_profile)
        profileViewModel.text.observe(
            viewLifecycleOwner,
            Observer {
                textView.text = it
            }
        )*/
        return binding
    }
}
