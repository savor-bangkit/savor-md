import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.salya.savorcapstone.R
import com.salya.savorcapstone.view.MainActivity

class Onboarding3Fragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private companion object {
        private const val TAG = "Onboarding3Fragment"
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_onboarding3, container, false)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        view.findViewById<Button>(R.id.loginGoogle).setOnClickListener {
            signIn()
        }

        return view
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        idToken ?: return // Return early if idToken is null

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    user?.getIdToken(true)?.addOnSuccessListener { result ->
                        val token = result.token ?: ""
                        saveAuthToken(token) // Save the token securely
                        updateUI(user)
                    }?.addOnFailureListener { e ->
                        Log.e(TAG, "Failed to get ID token.", e)
                        updateUI(null)
                    }
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // User is signed in, navigate to MainActivity
            saveOnboardingCompleted() // Store flag that onboarding is completed
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish() // Optionally finish the current activity
        } else {
            // User is signed out or login failed, show a message
            Toast.makeText(activity, "Please try to log in again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAuthToken(token: String) {
        val sharedPreferences = context?.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("auth_token", token) // Store the token securely
        editor?.apply()
    }

    private fun saveOnboardingCompleted() {
        val sharedPreferences = context?.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putBoolean("onboarding_completed", true) // Store flag that onboarding is completed
        editor?.apply()
    }

}


