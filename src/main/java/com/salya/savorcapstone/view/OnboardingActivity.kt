package com.salya.savorcapstone.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.rd.PageIndicatorView
import com.salya.savorcapstone.R
import com.salya.savorcapstone.ViewPagerAdapter
import com.salya.savorcapstone.view.MainActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pageIndicatorView: PageIndicatorView
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if onboarding is already completed
        val sharedPreferences = getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        val onboardingCompleted = sharedPreferences.getBoolean("onboarding_completed", false)

        if (onboardingCompleted) {
            // If onboarding is completed, start MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewpager)
        pageIndicatorView = findViewById(R.id.Indicator)

        adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        pageIndicatorView.setViewPager(viewPager)
    }
}
