<div align="center">
  <img src="https://i.imgur.com/xnJCsKX.png" alt="PicFly Logo" width="200"/>

# PicFly

  <p><strong>A lightweight, efficient image loading library for Android</strong></p>

  <div>
    <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Platform Android"/>
    <img src="https://img.shields.io/badge/Min%20SDK-24-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Min SDK 24"/>
    <img src="https://img.shields.io/badge/License-MIT-orange?style=flat-square" alt="License MIT"/>
    <img src="https://img.shields.io/badge/Version-2.0.1-blue?style=flat-square" alt="Version 2.0.1"/>
  </div>

  <br>

  <div>
    <a href="#-features">Features</a> •
    <a href="#-installation">Installation</a> •
    <a href="#-usage">Usage</a> •
    <a href="#-advanced-usage">Advanced</a> •
    <a href="#-license">License</a>
  </div>
</div>

<br>

## 📱 Overview

**PicFly** is a modern image loading library for Android that provides a simple and fluent API for loading images from URLs into ImageViews. It's designed to be lightweight yet powerful, with support for caching, transformations, and more.

<div align="center">
  <img src="https://i.imgur.com/DvpvklR.png" alt="Sample Image" width="300"/>
  <p><em>Sample image loaded with PicFly</em></p>
</div>

## ✨ Features

<table>
  <tr>
    <td>🚀 <strong>Simple API</strong></td>
    <td>Fluent interface with method chaining for easy use</td>
  </tr>
  <tr>
    <td>💾 <strong>Memory Caching</strong></td>
    <td>Efficient caching using LruCache</td>
  </tr>
  <tr>
    <td>💿 <strong>Disk Caching</strong></td>
    <td>Persistent caching between app sessions</td>
  </tr>
  <tr>
    <td>🖼️ <strong>Placeholder Support</strong></td>
    <td>Show placeholder images while loading</td>
  </tr>
  <tr>
    <td>⚠️ <strong>Error Handling</strong></td>
    <td>Display error images when loading fails</td>
  </tr>
  <tr>
    <td>🔄 <strong>Image Transformations</strong></td>
    <td>
      • 🌫️ Blur with customizable radius<br>
      • ⚪ Grayscale<br>
      • 🔄 Rotate<br>
      • ⭕ Circle crop with optional border<br>
      • 🔘 Rounded corners with customizable radius<br>
      • 🎨 Color filter<br>
      • ☀️ Brightness adjustment<br>
      • 🛠️ Support for custom transformations
    </td>
  </tr>
  <tr>
    <td>📏 <strong>Image Resizing</strong></td>
    <td>Resize images to specific dimensions</td>
  </tr>
  <tr>
    <td>📜 <strong>RecyclerView Support</strong></td>
    <td>Optimized for efficient image loading in RecyclerViews</td>
  </tr>
  <tr>
    <td>⏱️ <strong>Preloading</strong></td>
    <td>Preload images for smoother scrolling</td>
  </tr>
  <tr>
    <td>🔄 <strong>Kotlin & Java Support</strong></td>
    <td>Works seamlessly with both languages</td>
  </tr>
</table>

## 📦 Installation

### Gradle

Add the JitPack repository to your project-level build.gradle:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your app-level build.gradle:

```groovy
dependencies {
    implementation 'com.github.tuhinx:picfly:2.0.1'
}
```

### Kotlin DSL

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencies {
    implementation("com.github.tuhinx:picfly:2.0.1")
}
```

## 🚀 Usage

### Basic Usage

<div align="center">
  <table width="100%">
    <tr>
      <th width="50%">Java</th>
      <th width="50%">Kotlin</th>
    </tr>
    <tr>
      <td>

```java
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .into(imageView);
```

</td>
<td>

```kotlin
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .into(imageView)
```

</td>
    </tr>
  </table>
</div>

### With Placeholder and Error Handling

<div align="center">
  <table width="100%">
    <tr>
      <th width="50%">Java</th>
      <th width="50%">Kotlin</th>
    </tr>
    <tr>
      <td>

```java
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .placeholder(R.drawable.placeholder)
    .error(R.drawable.error)
    .into(imageView);
```

</td>
<td>

```kotlin
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .placeholder(R.drawable.placeholder)
    .error(R.drawable.error)
    .into(imageView)
```

</td>
    </tr>
  </table>
</div>

### Image Transformations

<div align="center">
  <table width="100%">
    <tr>
      <th width="50%">Java</th>
      <th width="50%">Kotlin</th>
    </tr>
    <tr>
      <td>

```java
// Grayscale
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .grayscale()
    .into(imageView);

// Blur
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .blur(15f) // Radius: 0-25
    .into(imageView);

// Circle Crop
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .circleCrop()
    .into(imageView);

// Circle Crop with Border
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .circleCrop(Color.BLACK, 4f) // Border color, width
    .into(imageView);

// Rounded Corners
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .roundedCorners(25f) // Radius
    .into(imageView);

// Color Filter
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .colorFilter(Color.RED)
    .into(imageView);

// Rotate
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .rotate(90f) // Degrees
    .into(imageView);

// Brightness
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .brightness(0.3f) // -1.0f to 1.0f
    .into(imageView);
```

</td>
<td>

```kotlin
// Grayscale
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .grayscale()
    .into(imageView)

// Blur
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .blur(15f) // Radius: 0-25
    .into(imageView)

// Circle Crop
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .circleCrop()
    .into(imageView)

// Circle Crop with Border
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .circleCrop(Color.BLACK, 4f) // Border color, width
    .into(imageView)

// Rounded Corners
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .roundedCorners(25f) // Radius
    .into(imageView)

// Color Filter
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .colorFilter(Color.RED)
    .into(imageView)

// Rotate
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .rotate(90f) // Degrees
    .into(imageView)

// Brightness
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .brightness(0.3f) // -1.0f to 1.0f
    .into(imageView)
```

</td>
    </tr>
  </table>
</div>

## 🔍 Advanced Usage

### Image Resizing

```java
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .resize(300, 300)
    .into(imageView);
```

### RecyclerView Integration

```java
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .into(imageView, viewHolder);
```

### Preloading Images

```java
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .preload();
```

### Cache Management

```java
// Clear memory cache
PicFly.get(context).clearMemoryCache();

// Clear disk cache
PicFly.get(context).clearDiskCache();

// Clear all caches
PicFly.get(context).clearAllCaches();
```

### RecyclerView Preloading

```java
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>
    implements RecyclerViewPreloader.PreloadModelProvider<MyItem> {

    private List<MyItem> items;

    @Override
    public List<String> getPreloadUrls(@NonNull MyItem item) {
        return Collections.singletonList(item.getImageUrl());
    }

    @Override
    public int[] getPreloadDimensions(@NonNull MyItem item) {
        return new int[]{300, 300}; // Width, Height
    }
}

// In your activity/fragment:
RecyclerView recyclerView = findViewById(R.id.recyclerView);
MyAdapter adapter = new MyAdapter();
recyclerView.setAdapter(adapter);

// Add the preloader
RecyclerViewPreloader<MyItem> preloader = PicFly.get(this)
    .getRecyclerViewPreloader(adapter);
recyclerView.addOnScrollListener(preloader);
```

### Custom Transformations

```java
public class CustomTransformation implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        // Apply your transformation here
        return transformedBitmap;
    }

    @Override
    public String key() {
        // Return a unique key for this transformation
        return "custom_transformation";
    }
}

// Usage
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .transform(new CustomTransformation())
    .into(imageView);
```

## 📄 License

PicFly is released under the MIT License. See the LICENSE file for details.

<details>
<summary>MIT License</summary>

```text
MIT License

Copyright (c) 2025 PicFly

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

</details>

---

<p align="center">
  <b>Made with ❤️ by PicFly</b>
</p>
