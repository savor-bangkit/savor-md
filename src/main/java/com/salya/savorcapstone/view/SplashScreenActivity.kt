package com.salya.savorcapstone.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.salya.savorcapstone.R
import com.salya.savorcapstone.view.MainActivity
import com.salya.savorcapstone.view.OnboardingActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Example: Check if user is logged in or onboarding is completed
        val sharedPreferences = getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        val onboardingCompleted = sharedPreferences.getBoolean("onboarding_completed", false)

        val targetActivity = if (onboardingCompleted) {
            MainActivity::class.java
        } else {
            OnboardingActivity::class.java
        }

        val intent = Intent(this, targetActivity)
        startActivity(intent)
        finish()
    }
}
