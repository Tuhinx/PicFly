<p align="center">
  <img src="https://i.imgur.com/xnJCsKX.png" alt="PicFly Logo 1" width="180"/>
</p>

<h1 align="center">PicFly</h1>

<p align="center">
  <b>A lightweight, efficient image loading library for Android</b>
</p>

<p align="center">
  <a href="#features">Features</a> •
  <a href="#installation">Installation</a> •
  <a href="#usage">Usage</a> •
  <a href="#advanced-usage">Advanced Usage</a> •
  <a href="#license">License</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform Android"/>
  <img src="https://img.shields.io/badge/Min%20SDK-24-brightgreen.svg" alt="Min SDK 24"/>
  <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License MIT"/>
  <img src="https://img.shields.io/badge/Version-1.0.0-orange.svg" alt="Version 1.0.0"/>
</p>

<br>

## 📱 Overview

PicFly is a modern image loading library for Android that provides a simple and fluent API for loading images from URLs into ImageViews. It's designed to be lightweight yet powerful, with support for caching, transformations, and more.

## ✨ Features

- **🚀 Simple API**: Fluent interface with method chaining for easy use
- **💾 Memory Caching**: Efficient caching using LruCache
- **💿 Disk Caching**: Persistent caching between app sessions
- **🖼️ Placeholder Support**: Show placeholder images while loading
- **⚠️ Error Handling**: Display error images when loading fails
- **🔄 Image Transformations**:
  - 🌫️ Blur with customizable radius
  - ⚪ Grayscale
  - 🛠️ Support for custom transformations
- **📏 Image Resizing**: Resize images to specific dimensions
- **📜 RecyclerView Support**: Optimized for efficient image loading in RecyclerViews
- **⏱️ Preloading**: Preload images for smoother scrolling
- **🔄 Kotlin & Java Support**: Works seamlessly with both languages

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
    implementation 'com.github:picfly:1.0.0'
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
    implementation("com.github:picfly:1.0.0")
}
```

## 🚀 Usage

### Basic Usage

<table>
<tr>
<th>Java</th>
<th>Kotlin</th>
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

### With Placeholder and Error Handling

<table>
<tr>
<th>Java</th>
<th>Kotlin</th>
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

### Image Transformations

<table>
<tr>
<th>Java</th>
<th>Kotlin</th>
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
```

</td>
</tr>
</table>

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

<details>
<summary>MIT License</summary>

```
MIT License

Copyright (c) 2025 TuhinX

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
  <b>Made with ❤️ by TuhinX</b>
</p>
