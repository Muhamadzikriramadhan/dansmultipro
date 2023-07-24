package com.example.dansmultipro.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dansmultipro.R
import com.example.dansmultipro.auth.LoginActivity
import com.example.dansmultipro.databinding.FragmentAccountBinding
import com.example.dansmultipro.sharedpreferences.SharedPreferences
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient

class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var signInClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountBinding.bind(view)
        sharedPreferences = SharedPreferences(view.context)

        signInClient =  Identity.getSignInClient(view.context)
        binding.name.text = sharedPreferences.getName()
        binding.logout.setOnClickListener {
            signInClient.signOut()
            var intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            sharedPreferences.removeData()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            AccountFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}