package com.example.mealapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealapp.model.pojo.CategoryMeal
import com.example.mealapp.model.pojo.MealRepoImp
import com.example.mealapp.model.pojo.Meals
import com.example.mealapp.model.pojo.RandomMeal
import com.example.mealapp.model.pojo.SubCategoryMeal
import com.example.mealapp.model.network.ResponseState
import com.example.mealapp.model.pojo.MealDataFav
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repo : MealRepoImp) : ViewModel() {
    private val _randomMeal=MutableStateFlow<ResponseState<List<RandomMeal>>>(ResponseState.Loading)
    val randomMeal =_randomMeal.asStateFlow()
    fun getRandomMeal(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getRandom()?.catch { error->_randomMeal.value= ResponseState.Error(error) }
                ?.collect{data->_randomMeal.value= ResponseState.Success(data)
                    Log.d("viewModel", "getRandomMeal: $data")}
        }
    }
    private val _categoryMeal=MutableStateFlow<ResponseState<List<CategoryMeal>>>(ResponseState.Loading)
    val categoryMeal=_categoryMeal.asStateFlow()
    fun getCategoryMeal(){
        viewModelScope.launch (Dispatchers.IO){
            repo.getCategory()?.catch { error->_categoryMeal.value= ResponseState.Error(error) }
                ?.collect{data-> _categoryMeal.value= ResponseState.Success(data)}
        }
    }
    private val _subCategory=MutableStateFlow<ResponseState<List<SubCategoryMeal>>>(ResponseState.Loading)
    val subCategory=_subCategory.asStateFlow()
    fun getSubCategory(category:String){
     viewModelScope.launch(Dispatchers.IO){
         repo.getSubCategory(category)?.catch { error->_subCategory.value= ResponseState.Error(error) }
             ?.collect{data->_subCategory.value= ResponseState.Success(data)}
     }
    }
    private val _categoryDetails=MutableStateFlow<ResponseState<List<Meals>>>(ResponseState.Loading)
    val categoryDetails=_categoryDetails.asStateFlow()
    fun getCategoryDetails(id:String){
        viewModelScope.launch(Dispatchers.IO){
            repo.getCategoryDetails(id)?.catch { error->_categoryDetails.value= ResponseState.Error(error) }
                ?.collect{data->_categoryDetails.value= ResponseState.Success(data)}
        }
    }
private val _fav=MutableStateFlow<ResponseState<List<MealDataFav>>>(ResponseState.Loading)
    val fav=_fav.asStateFlow()
    fun getMealFav(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getAllMeal()?.catch { error->_fav.value=ResponseState.Error(error) }
                ?.collect{data->_fav.value=ResponseState.Success(data)}
        }
    }

        fun insertMealToFav(meal:MealDataFav){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertMovieToDav(meal)
        }
        }
    fun deleteMealFromDB(meal: MealDataFav){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteMovieFromFav(meal)
        }
    }
}