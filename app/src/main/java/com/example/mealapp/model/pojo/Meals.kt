package com.example.mealapp.model.pojo

data class Meals(
    val idMeal: String,
    val strMeal: String,
    val strMealAlternate: String?,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?,
    val strMealThumb: String?,
    val strTags: String?,
    val strYoutube: String?,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?,
    val strIngredient11: String?,
    val strIngredient12: String?,
    val strIngredient13: String?,
    val strIngredient14: String?,
    val strIngredient15: String?,
    val strIngredient16: String?,
    val strIngredient17: String?,
    val strIngredient18: String?,
    val strIngredient19: String?,
    val strIngredient20: String?,
    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strMeasure10: String?,
    val strMeasure11: String?,
    val strMeasure12: String?,
    val strMeasure13: String?,
    val strMeasure14: String?,
    val strMeasure15: String?,
    val strMeasure16: String?,
    val strMeasure17: String?,
    val strMeasure18: String?,
    val strMeasure19: String?,
    val strMeasure20: String?,
    val strSource: String?,
    val strImageSource: String?,
    val strCreativeCommonsConfirmed: String?,
    val dateModified: String?

)

    fun Meals.toIngredientList(): List<IngredientItem> {
        val ingredients = mutableListOf<IngredientItem>()
        for (i in 1..20) {
            val ingredientField = Meals::class.java.getDeclaredField("strIngredient$i")
            ingredientField.isAccessible = true
            val ingredient = ingredientField.get(this) as? String

            val measureField = Meals::class.java.getDeclaredField("strMeasure$i")
            measureField.isAccessible = true
            val measure = measureField.get(this) as? String

            if (!ingredient.isNullOrBlank()) {
                val imageUrl = "https://www.themealdb.com/images/ingredients/${ingredient.trim()}.png"
                ingredients.add(
                    IngredientItem(
                        name = ingredient,
                        measure = measure ?: "",
                        imageUrl = imageUrl
                    )
                )
            }
        }
        return ingredients
    }


