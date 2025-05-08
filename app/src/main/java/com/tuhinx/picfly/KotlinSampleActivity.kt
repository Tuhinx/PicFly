package com.tuhinx.picfly

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.github.picfly.R

/**
 * Sample activity demonstrating PicFly usage in Kotlin.
 */
class KotlinSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        // Find all ImageViews
        val imageView1 = findViewById<ImageView>(R.id.imageView1)
        val imageView2 = findViewById<ImageView>(R.id.imageView2)
        val imageView3 = findViewById<ImageView>(R.id.imageView3)
        val imageView4 = findViewById<ImageView>(R.id.imageView4)
        val imageView5 = findViewById<ImageView>(R.id.imageView5)

        // Basic usage
        imageView1?.let {
            PicFly.get(this)
                .load("https://picsum.photos/500/500")
                .into(it)
        }

        // With placeholder and error handling
        imageView2?.let {
            PicFly.get(this)
                .load("https://picsum.photos/500/500")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(it)
        }

        // With transformations
        imageView3?.let {
            PicFly.get(this)
                .load("https://picsum.photos/500/500")
                .placeholder(R.drawable.placeholder)
                .grayscale()
                .into(it)
        }

        // With resizing
        imageView4?.let {
            PicFly.get(this)
                .load("https://picsum.photos/500/500")
                .placeholder(R.drawable.placeholder)
                .resize(300, 300)
                .into(it)
        }

        // With blur transformation
        imageView5?.let {
            PicFly.get(this)
                .load("https://picsum.photos/500/500")
                .placeholder(R.drawable.placeholder)
                .blur(15f)
                .into(it)
        }

        // Preload an image
        val preloadButton = findViewById<Button>(R.id.preloadButton)
        preloadButton?.setOnClickListener {
            PicFly.get(this)
                .load("https://picsum.photos/1000/1000")
                .preload()
        }

        // Clear caches
        val clearCacheButton = findViewById<Button>(R.id.clearCacheButton)
        clearCacheButton?.setOnClickListener {
            PicFly.get(this).clearAllCaches()
        }
    }

    override fun onDestroy() {
        // Cancel any pending requests when the activity is destroyed
        PicFly.get(this).cancelAll()
        super.onDestroy()
    }
}
