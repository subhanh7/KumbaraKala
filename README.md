# KumbaraKala

KumbaraKala is a modern Android marketplace application designed to promote traditional terracotta craftsmanship through a storytelling based shopping experience. The app connects local artisans with users by showcasing eco friendly handmade clay products using a visually rich and culturally inspired interface.

Built using Kotlin and Jetpack Compose, the application includes product browsing, artisan profiles, favorites system, category filtering and dynamic product management with a clean Material 3 UI.
KumbaraKala

Traditional Handcrafted Terracotta Marketplace App

KumbaraKala is a modern Android marketplace application designed to preserve and promote traditional terracotta craftsmanship by connecting local artisans directly with users through a digital platform. The application focuses on eco friendly clay products, artisan storytelling and culturally inspired shopping experiences.

The project combines traditional Indian pottery culture with modern Android development using Kotlin and Jetpack Compose. The app provides a visually rich and storytelling based interface where users can explore handmade terracotta products, view artisan profiles, save favorite items and discover eco friendly handcrafted products.

⸻

Project Vision

Many traditional artisans struggle to gain visibility in the modern digital marketplace. Handmade terracotta products often remain limited to local markets and seasonal exhibitions.

KumbaraKala was created to solve this problem by providing:

* A digital platform for artisans
* Better product visibility
* Storytelling based shopping experience
* Eco friendly product awareness
* Preservation of traditional craftsmanship
* Modern UI experience for cultural products

The app aims to bridge tradition and technology through a clean, elegant and user friendly Android application.

⸻

Features

User Authentication

* Login and Signup system
* Persistent user session using SharedPreferences
* Simple and smooth onboarding flow

Product Marketplace

* Browse handcrafted terracotta products
* Multiple product categories
* Detailed product descriptions
* Product ratings and storytelling cards

Categories

* Pots
* Lamps
* Kitchenware
* Drinkware
* Decor
* Garden
* Pet Utility
* Traditional Utility Products

Favorites System

* Save favorite products
* Personalized product collection
* Quick access to liked items

Artisan Profiles

* Display artisan information
* Storytelling based artisan introduction
* Traditional craftsmanship representation

Add Product Feature

* Dynamic product adding screen
* Product image support
* Product category management

Modern UI Design

* Earthy terracotta inspired color palette
* Material 3 design components
* Responsive Jetpack Compose UI
* Traditional aesthetic blended with modern UX

Performance Features

* Fast navigation system
* Lightweight architecture
* Smooth scrolling experience
* Efficient image rendering using Coil

⸻

Technologies Used

Technology	Purpose
Kotlin	Main programming language
Jetpack Compose	Modern Android UI toolkit
Android Studio	Development environment
Supabase	Authentication and backend services
SharedPreferences	Session management
Material 3	UI design components
Coil	Image loading library
GitHub	Version control and repository hosting
Stitch AI	UI inspiration and interface planning
Antigravity	Design support and visual workflow

⸻

App Architecture

The application follows a modular Android project structure to keep the code clean, maintainable and scalable.

Architecture Style

* MVVM inspired structure
* Modular UI components
* Separate data and presentation layers
* Reusable composables

⸻

Project Structure

KumbaraKala/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/kumbarakala/app/
│   │   │   │   ├── data/
│   │   │   │   ├── model/
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/
│   │   │   │   │   ├── screens/
│   │   │   │   ├── utils/
│   │   │   │   ├── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   ├── values/
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md

⸻

Main Modules Explanation

data/

Contains backend related logic and repositories.

Includes

* Authentication repository
* Product repository
* Supabase configuration

model/

Contains application data models.

Includes

* Product data model
* Artisan model
* Story card model

ui/components/

Reusable Jetpack Compose UI components.

Includes

* Product cards
* Bottom navigation bar
* Category chips
* Search bars
* Reusable UI sections

ui/screens/

Contains all application screens.

Includes

* Splash screen
* Authentication screens
* Home screen
* Product detail screen
* Favorites screen
* Add product screen
* Profile screen
* Artisan setup screen

utils/

Contains helper classes and utility logic.

Includes

* SharedPreferences helper
* Image utilities

⸻

Product Categories Added

The application currently contains handcrafted terracotta products including:

* Clay Water Pot
* Clay Diya
* Earthen Cooking Pot
* Clay Glass
* Clay Plate
* Decorative Lantern
* Terracotta Aroma Diffuser
* Terracotta Flower Vase
* Clay Planter
* Terracotta Tea Set
* Bird Feeder
* Wind Chime
* Pet Water Bowl
* Spice Container Set
* Clay Coffee Mug
* Storage Pot

Each product includes:

* Product image
* Product title
* Description
* Category
* Ratings
* Story count
* Eco chip tag

⸻

UI and Design Philosophy

The application design focuses heavily on:

* Warm earthy colors
* Traditional terracotta aesthetics
* Minimal and clean UI
* Storytelling based shopping experience
* Artisan focused presentation
* Cultural authenticity

The UI is inspired by handmade pottery textures and natural clay tones.

⸻

How The App Works

Authentication Flow

1. User opens the app
2. Splash screen loads
3. User logs in or signs up
4. Session is stored using SharedPreferences
5. User is redirected to Home Screen

Product Browsing Flow

1. User explores categories
2. Product cards are displayed
3. User clicks any product
4. Product detail screen opens
5. User can favorite products

Add Product Flow

1. Artisan opens Add Product screen
2. Product details are entered
3. Product is added dynamically
4. New product appears in product list

⸻

Installation Guide

Prerequisites

Before running the project ensure you have:

* Android Studio installed
* Android SDK installed
* Kotlin support enabled
* Internet connection for Supabase services

⸻

Clone Repository

git clone https://github.com/subhanh7/KumbaraKala.git

⸻

Open Project

1. Open Android Studio
2. Click Open Project
3. Select the KumbaraKala folder
4. Wait for Gradle Sync

⸻

Run The Application

1. Connect Android device or emulator
2. Click Run
3. Build process starts
4. Application launches successfully

⸻

APK Download

Google Drive APK Link

https://drive.google.com/file/d/1lyqCaBysF0ysuX0E8zhFKfIOuyj0wMbH/view?usp=sharing

⸻

Screenshots

Recommended Screenshots To Add

Create a folder named:

screenshots/

Add screenshots for:

* Splash Screen
* Login Screen
* Signup Screen
* Home Screen
* Product Details
* Favorites Screen
* Add Product Screen
* Artisan Profile Screen

⸻

Future Improvements

* Online product ordering
* Payment gateway integration
* Real time chat with artisans
* Product reviews and comments
* Multi language support
* AI based product recommendations
* Admin dashboard
* Cloud image storage
* Delivery tracking
* AR based pottery preview

⸻

Challenges Faced During Development

* Creating a visually consistent terracotta theme
* Managing dynamic product lists
* Structuring reusable Jetpack Compose components
* Designing storytelling based UI
* Maintaining clean navigation architecture
* Optimizing product image rendering

⸻

Learning Outcomes

This project helped improve:

* Android development skills
* Jetpack Compose UI design
* State management
* Kotlin programming
* App architecture understanding
* UI and UX design thinking
* GitHub project management
* Real world project structuring

⸻

Why This Project Is Unique

Unlike regular ecommerce applications, KumbaraKala focuses on preserving traditional culture and storytelling through technology.

The application combines:

* Traditional artisan products
* Modern Android UI
* Storytelling experience
* Cultural aesthetics
* Eco friendly awareness

This makes the project both technically strong and socially meaningful.

⸻

Repository Checklist

* Complete source code included
* Modular folder structure
* Multiple screens implemented
* Authentication system added
* Dynamic products implemented
* Favorites functionality included
* Clean Jetpack Compose UI
* APK link provided
* Documentation included
* GitHub repository maintained

⸻

Author

Mohammed Subhan

Android Developer and AI enthusiast passionate about building modern applications inspired by culture, creativity and meaningful user experiences.

⸻

License

This project is created for educational and learning purposes.
