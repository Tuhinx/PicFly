package com.github.picfly;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Sample activity demonstrating PicFly usage in Java.
 */
public class JavaSampleActivity extends AppCompatActivity {

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
        ImageView imageView6 = findViewById(R.id.imageView6);
        ImageView imageView7 = findViewById(R.id.imageView7);
        ImageView imageView8 = findViewById(R.id.imageView8);
        ImageView imageView9 = findViewById(R.id.imageView9);
        ImageView imageView10 = findViewById(R.id.imageView10);

        // Basic usage
        if (imageView1 != null) {
            PicFly.get(this)
                    .load("https://picsum.photos/seed/picsum/200/300")
                    .into(imageView1);
        }

        // With placeholder and error handling
        if (imageView2 != null) {
            PicFly.get(this)
                    .load("https://picsum.photos/seed/picsum/200/300")
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(imageView2);
        }

        // With transformations
        if (imageView3 != null) {
            PicFly.get(this)
                    .load("https://picsum.photos/seed/picsum/200/300")
                    .placeholder(R.drawable.placeholder)
                    .grayscale()
                    .into(imageView3);
        }

        // With resizing
        if (imageView4 != null) {
            PicFly.get(this)
                    .load("https://picsum.photos/seed/picsum/200/300")
                    .placeholder(R.drawable.placeholder)
                    .resize(300, 300)
                    .into(imageView4);
        }

        // With blur transformation
        if (imageView5 != null) {
            PicFly.get(this)
                    .load("https://picsum.photos/seed/picsum/200/300")
                    .placeholder(R.drawable.placeholder)
                    .blur(15f)
                    .into(imageView5);
        }

        // With circle crop transformation
        if (imageView6 != null) {
            PicFly.get(this)
                    .load("https://picsum.photos/seed/picsum/200/300")
                    .placeholder(R.drawable.placeholder)
                    .circleCrop()
                    .into(imageView6);
        }

        // With rounded corners transformation
        if (imageView7 != null) {
            PicFly.get(this)
                    .load("https://picsum.photos/seed/picsum/200/300")
                    .placeholder(R.drawable.placeholder)
                    .roundedCorners(30f)
                    .into(imageView7);
        }

        // With color filter transformation
        if (imageView8 != null) {
            PicFly.get(this)
                    .load("https://picsum.photos/seed/picsum/200/300")
                    .placeholder(R.drawable.placeholder)
                    .colorFilter(Color.argb(80, 255, 0, 0)) // Semi-transparent red
                    .into(imageView8);
        }

        // With rotate transformation
        if (imageView9 != null) {
            PicFly.get(this)
                    .load("https://picsum.photos/seed/picsum/200/300")
                    .placeholder(R.drawable.placeholder)
                    .rotate(90f)
                    .into(imageView9);
        }

        // With brightness transformation
        if (imageView10 != null) {
            PicFly.get(this)
                    .load("https://picsum.photos/seed/picsum/200/300")
                    .placeholder(R.drawable.placeholder)
                    .brightness(0.3f)
                    .into(imageView10);
        }

        // Preload an image
        Button preloadButton = findViewById(R.id.preloadButton);
        if (preloadButton != null) {
            preloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PicFly.get(JavaSampleActivity.this)
                            .load("https://picsum.photos/seed/picsum/200/300")
                            .preload();
                }
            });
        }

        // Clear caches
        Button clearCacheButton = findViewById(R.id.clearCacheButton);
        if (clearCacheButton != null) {
            clearCacheButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PicFly.get(JavaSampleActivity.this).clearAllCaches();
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
