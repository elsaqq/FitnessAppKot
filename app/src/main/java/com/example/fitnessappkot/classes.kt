package com.example.fitnessappkot


data class Recipe(
    val name: String = "",
    val ingredients: List<String> = emptyList(),
    val carbs : Int =0,
    val fats: Int = 0,
    val proteins: Int = 0,
    val calories: Int = 0,
    val steps: List<String> = emptyList(),
    val duration: String = "",
    val levelOfDifficulty: String = "",
    val pictureUrl: String = ""
)




data class UserMacros(
    var calories: Double = 0.0,
    var proteins: Double = 0.0,
    var carbs: Double = 0.0,
    var fats: Double = 0.0
)
data class SupportMessage(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val message: String = "",
    val timestamp: Long = 0
)





data class DiaryEntry(
    val id: String = "",
    val userId: String = "",
    val date: Long = System.currentTimeMillis(),
    val foodName: String = "",
    val quantity: String = "",
    val mealTime: String = "",
    val proteins: Double = 0.0,
    val carbs: Double = 0.0,
    val fats: Double = 0.0


)



data class Review(
    val userId: String = "",
    val name: String = "", // Add this line
    val username: String = "",
    val rating: Float = 0f,
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

