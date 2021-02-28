package com.example.testingproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testingproject.databinding.ActivityLottieSplashBinding
import kotlinx.android.synthetic.main.activity_lottie_splash.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LottieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLottieSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            Intent(this@LottieActivity, SplashActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }
}
