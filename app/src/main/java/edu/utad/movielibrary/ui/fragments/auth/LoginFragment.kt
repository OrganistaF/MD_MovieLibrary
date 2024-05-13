package edu.utad.movielibrary.ui.fragments.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import edu.utad.movielibrary.R
import edu.utad.movielibrary.databinding.FragmentLoginBinding
import edu.utad.movielibrary.ui.MainActivity


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var authFirebaseAuth: FirebaseAuth
    private var email: String? = null
    private var pass: String? = null



    override fun onAttach(context: Context) {
        super.onAttach(context)
        email = arguments?.getString("email")
        pass = arguments?.getString("pass")
    }
    /*Match graphic and logic*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }


    /*View Logic*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val aas = activity as MainActivity
//        aas.toolBarHide()
        instances()

        Glide.with(this).load("https://png.pngtree.com/element_our/png/20181113/clapperboard-film-logo-icon-design-template-vector-isolated-png_236642.jpg")
            .into(binding.imageLogin)

        if (email!=null && pass != null){
            binding.editEmail.setText(email)
            binding.editPass.setText(pass)
        }

        binding.buttonLogin.setOnClickListener {
            if (binding.editPass.text.toString().isNotEmpty()
                && binding.editEmail.text.isNotEmpty()
            ) {
                authFirebaseAuth.signInWithEmailAndPassword(
                    binding.editEmail.text.toString(),
                    binding.editPass.text.toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("debug", authFirebaseAuth.currentUser!!.uid)
                            val mainActivity = activity as? MainActivity
                            mainActivity?.setUserID(authFirebaseAuth.currentUser!!.uid)
//                            mainActivity?.toolBarShow()

                            val bundle = Bundle()
                            bundle.putString("uid", authFirebaseAuth.currentUser!!.uid)
                            findNavController().navigate(
                                R.id.action_LoginFragment_to_popularMoviesFragment,
                                bundle
                            )
                        } else {
                            Snackbar.make(
                                binding.root,
                                "Account login failed",
                                Snackbar.LENGTH_LONG,
                            ).setAction("Create account?") {
                                findNavController().navigate(
                                    R.id.action_LoginFragment_to_signUpFragment)
                            }.show()
                        }
                    }
            } else {
                Snackbar.make(binding.root, "Fail login. Try again", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.buttonSingUp.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_signUpFragment)
        }
    }

    private fun instances() {
        authFirebaseAuth = FirebaseAuth.getInstance()
    }


}