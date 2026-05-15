package com.kumbarakala.app.model

import androidx.compose.runtime.mutableStateListOf
import com.kumbarakala.app.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
data class Product(
    val id: String,
    val name: String,
    val subtitle: String,
    val storyCount: String,
    val description: String,
    val ecoChipText: String,
    val category: String,
    var isFavorite: Boolean = false, // 🔥 Fixed: Added missing comma
    val imageRes: Int = 0,
    val imageUri: String? = null,
    val rating: String = "4.5"
)

// 🔥 YOUR ORIGINAL LIST - EXACTLY AS IT WAS
val productList = listOf(

    Product(
        id = "id_pot",
        name = "Clay Water Pot",
        subtitle = "Keeps water naturally cool",
        imageRes = R.drawable.clay_pot,
        rating = "4.8",
        storyCount = "120+",
        description = "Handcrafted with care by skilled artisans, this clay water pot keeps your water naturally cool and pure. A perfect blend of tradition and wellness for your everyday life.",
        ecoChipText = "Eco-friendly",
        category = "Pots"
    ),

    Product(
        id = "id_diya",
        name = "Clay Diya",
        subtitle = "Brings purity and positivity",
        imageRes = R.drawable.clay_diya,
        rating = "4.9",
        storyCount = "85+",
        description = "Light up your home with these traditional hand-molded clay diyas. Made from organic earth, they provide a long-lasting, steady flame for your spiritual rituals.",
        ecoChipText = "Natural Dye",
        category = "Lamps"
    ),

    Product(
        id = "id_earthen",
        name = "Earthen Cooking Pot",
        subtitle = "Enhances natural taste",
        imageRes = R.drawable.clay_cooking,
        rating = "4.7",
        storyCount = "98+",
        description = "Experience the authentic taste of slow-cooked meals. This alkaline clay pot neutralizes the pH balance of food, adding essential minerals while retaining moisture.",
        ecoChipText = "Artisan Made",
        category = "Utensils"
    ),

    Product(
        id = "id_glass",
        name = "Clay Glass",
        subtitle = "Healthy & eco-friendly",
        imageRes = R.drawable.clay_glass,
        rating = "4.6",
        storyCount = "110+",
        description = "Ditch the plastic for these reusable clay glasses. They provide a unique earthy aroma to your water and buttermilk, keeping drinks cool even in peak summer.",
        ecoChipText = "Recycled",
        category = "Clays"
    ),

    Product(
        id = "id_plate",
        name = "Clay Plate",
        subtitle = "Handmade & traditional",
        imageRes = R.drawable.clay_plate,
        rating = "4.8",
        storyCount = "72+",
        description = "Serve your meals in style with these lead-free clay plates. Each piece is hand-finished to ensure a smooth texture while maintaining a rustic, organic look.",
        ecoChipText = "Lead Free",
        category = "Utensils"
    ),

    Product(
        id = "id_lantern",
        name = "Decorative Lantern",
        subtitle = "Warm light for your space",
        imageRes = R.drawable.clay_lantern,
        rating = "4.9",
        storyCount = "135+",
        description = "A stunning piece of terracotta art. This lantern creates intricate shadow patterns on your walls, bringing a warm, cozy atmosphere to any room.",
        ecoChipText = "Unique Design",
        category = "Lamps"
    ),

    Product(
        id = "aroma_diffuser_1",
        name = "Terracotta Aroma Diffuser",
        subtitle = "Natural fragrance & calming ambiance",
        imageRes = R.drawable.aroma_diffuser,
        rating = "4.9",
        storyCount = "92+",
        description = "This beautifully handcrafted terracotta aroma diffuser is designed to fill your space with soothing fragrances and warm earthy charm. Perfect for meditation spaces, bedrooms, and wellness corners.",
        ecoChipText = "Handcrafted",
        category = "Lamps"
    ),

    Product(
        id = "flower_vase_1",
        name = "Terracotta Flower Vase",
        subtitle = "Elegant handcrafted floral decor",
        imageRes = R.drawable.flower_vase,
        rating = "4.8",
        storyCount = "88+",
        description = "This artisan-made terracotta flower vase brings warmth and earthy beauty to any space. Perfect for fresh flowers, dried arrangements, and rustic home décor.",
        ecoChipText = "Eco Clay",
        category = "Pots"
    ),

    Product(
        id = "plant_pot_1",
        name = "Handcrafted Clay Planter",
        subtitle = "Eco-friendly indoor plant pot",
        imageRes = R.drawable.plant_pot,
        rating = "4.7",
        storyCount = "75+",
        description = "A handcrafted clay planter designed for modern indoor greenery. Its breathable natural clay helps maintain healthy roots while adding a warm traditional aesthetic.",
        ecoChipText = "Eco Pot",
        category = "Pots"
    ),

    Product(
        id = "tea_set_1",
        name = "Terracotta Tea Set",
        subtitle = "Traditional handcrafted tea experience",
        imageRes = R.drawable.tea_set,
        rating = "4.9",
        storyCount = "104+",
        description = "This handcrafted terracotta tea set combines timeless pottery craftsmanship with natural elegance. Ideal for tea lovers, cozy evenings, and cultural home décor.",
        ecoChipText = "Traditional",
        category = "Utensils"
    ),
    Product(
        id = "bird_feeder_1",
        name = "Terracotta Bird Feeder",
        subtitle = "Nature-friendly balcony décor",
        imageRes = R.drawable.bird_feeder,
        rating = "4.9",
        storyCount = "76+",
        description = "A handcrafted clay bird feeder designed to welcome sparrows and small birds into your balcony or garden while adding a rustic earthy aesthetic.",
        ecoChipText = "Eco-Friendly",
        category = "Garden"
    ),

    Product(
        id = "wind_chime_1",
        name = "Terracotta Wind Chime",
        subtitle = "Peaceful earthy sound décor",
        imageRes = R.drawable.wind_chime,
        rating = "4.8",
        storyCount = "64+",
        description = "A handcrafted clay wind chime that produces soft calming tones, perfect for balconies, gardens, and meditation spaces.",
        ecoChipText = "Handcrafted",
        category = "Decor"
    ),

    Product(
        id = "pet_bowl_1",
        name = "Handmade Pet Water Bowl",
        subtitle = "Natural cooling for pets",
        imageRes = R.drawable.pet_bowl,
        rating = "4.7",
        storyCount = "52+",
        description = "A durable clay bowl designed to naturally keep water cooler for pets during hot summer days while adding rustic charm to your home.",
        ecoChipText = "Pet Safe",
        category = "Utility"
    ),

    Product(
        id = "spice_set_1",
        name = "Terracotta Spice Jar Set",
        subtitle = "Traditional kitchen storage",
        imageRes = R.drawable.spice_jar,
        rating = "4.9",
        storyCount = "81+",
        description = "A handcrafted terracotta spice container set perfect for storing masalas, herbs, and seasonings while bringing a warm traditional touch to your kitchen.",
        ecoChipText = "Kitchen Safe",
        category = "Kitchen"
    ),

    Product(
        id = "coffee_mug_1",
        name = "Clay Coffee Mug",
        subtitle = "Warm earthy coffee experience",
        imageRes = R.drawable.coffee_mug,
        rating = "4.8",
        storyCount = "94+",
        description = "A handcrafted terracotta coffee mug designed to enhance the aroma and warmth of your favorite beverages with a natural earthy feel.",
        ecoChipText = "Reusable",
        category = "Drinkware"
    ),

    Product(
        id = "storage_pot_1",
        name = "Terracotta Storage Pot",
        subtitle = "Rustic handcrafted storage",
        imageRes = R.drawable.storage_pot,
        rating = "4.7",
        storyCount = "69+",
        description = "A beautifully handcrafted clay storage pot ideal for keeping dry ingredients fresh while adding a traditional artisan touch to your kitchen décor.",
        ecoChipText = "Natural Clay",
        category = "Kitchen"
    ),
)

val globalProductList = mutableStateListOf<Product>().apply {
    addAll(productList)
}

object FavoritesManager {
    val favoriteProductIds = mutableStateListOf<String>()

    fun toggleFavorite(productId: String) {
        if (favoriteProductIds.contains(productId)) {
            favoriteProductIds.remove(productId)
        } else {
            favoriteProductIds.add(productId)
        }
    }

    fun isFavorite(productId: String): Boolean {
        return favoriteProductIds.contains(productId)
    }
}

// 🔥 ADDING NEW PRODUCTS VIA APP
fun addNewProductToGlobalList(name: String, category: String, description: String, imageUri: String?) {
    val newProduct = Product(
        id = java.util.UUID.randomUUID().toString(),
        name = name,
        subtitle = "Handcrafted $category",
        category = category,
        imageUri = imageUri,
        description = description,
        rating = "5.0",
        storyCount = "1",
        ecoChipText = "Handmade"
    )
    globalProductList.add(0, newProduct)
}
fun saveProductsToDisk(context: android.content.Context) {
    val prefs = context.getSharedPreferences("kumbarakala_prefs", android.content.Context.MODE_PRIVATE)
    val json = Gson().toJson(globalProductList.toList())
    prefs.edit().putString("saved_products", json).apply()
}

fun loadProductsFromDisk(context: android.content.Context) {
    try {
        val prefs = context.getSharedPreferences("kumbarakala_prefs", android.content.Context.MODE_PRIVATE)
        val json = prefs.getString("saved_products", null)
        if (!json.isNullOrEmpty()) {
            val type = object : com.google.gson.reflect.TypeToken<List<Product>>() {}.type
            val savedList: List<Product> = com.google.gson.Gson().fromJson(json, type)

            // Only update if we actually got a valid list back
            if (savedList.isNotEmpty()) {
                globalProductList.clear()
                globalProductList.addAll(savedList)
            }
        }
    } catch (e: Exception) {
        // If it fails, we just don't load the new items,
        // preventing the app from closing!
        android.util.Log.e("ProductLoad", "Failed to load products: ${e.message}")
    }
}
fun deleteProduct(productId: String, context: android.content.Context) {
    // 1. Remove from memory
    globalProductList.removeAll { it.id == productId }
    // 2. Remove from favorites if it was liked
    FavoritesManager.favoriteProductIds.remove(productId)
    // 3. Save the new list to disk so it stays deleted
    saveProductsToDisk(context)
}