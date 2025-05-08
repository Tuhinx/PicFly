package com.github.picfly;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        // Find all ImageViews
        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView3 = findViewById(R.id.imageView3);
        ImageView imageView4 = findViewById(R.id.imageView4);
        ImageView imageView5 = findViewById(R.id.imageView5);

        // Basic usage
        if (imageView1 != null) {
            PicFly.get(this)
                    .load("https://i.imgur.com/DvpvklR.png")
                    .into(imageView1);
        }

        // With placeholder and error handling
        if (imageView2 != null) {
            PicFly.get(this)
                    .load("https://i.imgur.com/DvpvklR.png")
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(imageView2);
        }

        // With transformations
        if (imageView3 != null) {
            PicFly.get(this)
                    .load("https://i.imgur.com/DvpvklR.png")
                    .placeholder(R.drawable.placeholder)
                    .grayscale()
                    .into(imageView3);
        }

        // With resizing
        if (imageView4 != null) {
            PicFly.get(this)
                    .load("https://i.imgur.com/DvpvklR.png")
                    .placeholder(R.drawable.placeholder)
                    .resize(300, 300)
                    .into(imageView4);
        }

        // With blur transformation
        if (imageView5 != null) {
            PicFly.get(this)
                    .load("https://i.imgur.com/DvpvklR.png")
                    .placeholder(R.drawable.placeholder)
                    .blur(15f)
                    .into(imageView5);
        }

        // Preload an image
        Button preloadButton = findViewById(R.id.preloadButton);
        if (preloadButton != null) {
            preloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PicFly.get(MainActivity.this)
                            .load("https://i.imgur.com/DvpvklR.png")
                            .preload();
                    Toast.makeText(MainActivity.this, "Image preloaded", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Clear caches
        Button clearCacheButton = findViewById(R.id.clearCacheButton);
        if (clearCacheButton != null) {
            clearCacheButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PicFly.get(MainActivity.this).clearAllCaches();
                    Toast.makeText(MainActivity.this, "Caches cleared", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        // Cancel any pending requests when the activity is destroyed
        PicFly.get(this).cancelAll();
        super.onDestroy();
    }
}