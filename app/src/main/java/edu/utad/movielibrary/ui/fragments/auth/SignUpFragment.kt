package edu.utad.movielibrary.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import edu.utad.movielibrary.R
import edu.utad.movielibrary.databinding.FragmentSignupBinding
import edu.utad.movielibrary.env.EnvironmentVariables.DB_REF
import edu.utad.movielibrary.model.User

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var authFirebaseAuth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instances()

        binding.registerButton.setOnClickListener {

            if (binding.editMail.text.isNotEmpty()
                && binding.editName.text.isNotEmpty()
                && binding.editPass.text.isNotEmpty()
                && binding.editPass2.text.isNotEmpty()
                && binding.editPass.text == binding.editPass.text
            ) {

                val email = binding.editMail.text.toString()
                val pass = binding.editPass.text.toString()

                authFirebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val currentUser: FirebaseUser = authFirebaseAuth.currentUser!!

                            val user =
                                User(
                                    name = binding.editName.text.toString(),
                                    email = binding.editMail.text.toString(),
                                )

                            database.reference.child("users")
                                .child(currentUser.uid)
                                .setValue(user)


                            val bundle = Bundle()
                            bundle.putString("email", binding.editMail.text.toString())
                            bundle.putString("pass", binding.editPass.text.toString())
                            bundle.putString("uid", currentUser.uid)
                            findNavController().navigate(
                                R.id.action_signUpFragment_to_LoginFragment,
                                bundle
                            )

                            Snackbar.make(binding.root, "Correctly Sing up", Snackbar.LENGTH_SHORT)
                                .show()
                        } else {
                            Snackbar.make(binding.root, "Fail Sing up ${it.exception}", Snackbar.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }
    }

    private fun instances() {
        database =
            FirebaseDatabase.getInstance(DB_REF)

        authFirebaseAuth = FirebaseAuth.getInstance()
    }

}