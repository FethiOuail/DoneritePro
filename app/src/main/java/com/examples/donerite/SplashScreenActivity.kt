package com.examples.donerite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import com.examples.donerite.databinding.ActivitySplashScreenBinding
import com.examples.donerite.ui.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tvLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_in))

        Handler().postDelayed({
            binding.tvLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_out))
            Handler().postDelayed({
                binding.tvLogo.visibility = View.GONE
                // val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }, 500)
        }, 3000)



    }
}