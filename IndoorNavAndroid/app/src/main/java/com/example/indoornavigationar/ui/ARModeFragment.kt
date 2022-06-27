package com.example.indoornavigationar.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.indoornavigationar.databinding.ArmodeFragmentBinding
import com.unity3d.player.UnityPlayerActivity

class ARModeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ArmodeFragmentBinding.inflate(inflater, container, false)

//        val intent = Intent(activity, UnityPlayerActivity::class.java)
//        startActivity(intent)

        return binding.root
    }
}