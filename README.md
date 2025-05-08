# PicFly

<p align="center">
  <img src="https://i.imgur.com/xnJCsKX.png" alt="PicFly Logo 1" width="160"/>
  <img src="https://i.imgur.com/5FIHQgQ.png" alt="PicFly Logo 2" width="160"/>
</p>


PicFly is a lightweight, efficient, and feature-rich image loading library for Android. It provides a simple and fluent API for loading images from URLs into ImageViews with support for caching, transformations, and more.

## Features

- **Simple API**: Fluent interface with method chaining for easy use
- **Memory Caching**: Efficient caching using LruCache
- **Disk Caching**: Persistent caching between app sessions
- **Placeholder Support**: Show placeholder images while loading
- **Error Handling**: Display error images when loading fails
- **Image Transformations**:
  - Blur with customizable radius
  - Grayscale
  - Support for custom transformations
- **Image Resizing**: Resize images to specific dimensions
- **RecyclerView Support**: Optimized for efficient image loading in RecyclerViews
- **Preloading**: Preload images for smoother scrolling
- **Kotlin & Java Support**: Works seamlessly with both languages

## Installation

### Gradle

Add the JitPack repository to your root build.gradle:


### For Gradle

```groovy
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

dependencies {
     implementation "com.github.Tuhinx:PicFly:1.0.0"
	}
}

```

## For Kotlin

```gradle.kts
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}


dependencies {
     implementation("com.github.Tuhinx:PicFly:1.0.0")
	}
	}
}

```

## Usage

### Basic Usage

```java
// Java
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .into(imageView);
```

```kotlin
// Kotlin
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .into(imageView)
```

### With Placeholder and Error Handling

```java
// Java
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .placeholder(R.drawable.placeholder)
    .error(R.drawable.error)
    .into(imageView);
```

```kotlin
// Kotlin
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .placeholder(R.drawable.placeholder)
    .error(R.drawable.error)
    .into(imageView)
```

### With Transformations

```java
// Java - Grayscale
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .grayscale()
    .into(imageView);

// Java - Blur
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .blur(15f) // Radius: 0-25
    .into(imageView);
```

```kotlin
// Kotlin - Grayscale
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .grayscale()
    .into(imageView)

// Kotlin - Blur
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .blur(15f) // Radius: 0-25
    .into(imageView)
```

### With Resizing

```java
// Java
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .resize(300, 300)
    .into(imageView);
```

```kotlin
// Kotlin
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .resize(300, 300)
    .into(imageView)
```

### RecyclerView Integration

```java
// Java
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .into(imageView, viewHolder);
```

```kotlin
// Kotlin
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .into(imageView, viewHolder)
```

### Preloading Images

```java
// Java
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .preload();
```

```kotlin
// Kotlin
PicFly.get(context)
    .load("https://example.com/image.jpg")
    .preload()
```

### Clearing Caches

```java
// Java
// Clear memory cache
PicFly.get(context).clearMemoryCache();

// Clear disk cache
PicFly.get(context).clearDiskCache();

// Clear all caches
PicFly.get(context).clearAllCaches();
```

```kotlin
// Kotlin
// Clear memory cache
PicFly.get(context).clearMemoryCache()

// Clear disk cache
PicFly.get(context).clearDiskCache()

// Clear all caches
PicFly.get(context).clearAllCaches()
```

### RecyclerView Preloading

```java
// Java
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements RecyclerViewPreloader.PreloadModelProvider<MyItem> {

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

## Custom Transformations

You can create custom transformations by implementing the `Transformation` interface:

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

## License

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
