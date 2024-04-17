package com.example.videoplayerapplication

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val imageView = findViewById<ImageView>(R.id.splash_image)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)


        imageView.postOnAnimationDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, fadeOut.duration)
    }
}

