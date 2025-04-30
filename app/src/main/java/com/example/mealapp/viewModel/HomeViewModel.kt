package com.example.mealapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealapp.model.CategoryMeal
import com.example.mealapp.model.MealRepoImp
import com.example.mealapp.model.RandomMeal
import com.example.mealapp.model.SubCategoryMeal
import com.example.mealapp.network.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repo : MealRepoImp) : ViewModel() {
    private val _randomMeal=MutableStateFlow<ResponseState<List<RandomMeal>>>(ResponseState.Loading)
    val randomMeal =_randomMeal.asStateFlow()
    fun getRandomMeal(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getRandom()?.catch { error->_randomMeal.value=ResponseState.Error(error) }
                ?.collect{data->_randomMeal.value=ResponseState.Success(data)
                    Log.d("viewModel", "getRandomMeal: $data")}
        }
    }
    private val _categoryMeal=MutableStateFlow<ResponseState<List<CategoryMeal>>>(ResponseState.Loading)
    val categoryMeal=_categoryMeal.asStateFlow()
    fun getCategoryMeal(){
        viewModelScope.launch (Dispatchers.IO){
            repo.getCategory()?.catch { error->_categoryMeal.value=ResponseState.Error(error) }
                ?.collect{data-> _categoryMeal.value=ResponseState.Success(data)}
        }
    }
    private val _subCategory=MutableStateFlow<ResponseState<List<SubCategoryMeal>>>(ResponseState.Loading)
    val subCategory=_subCategory.asStateFlow()
    fun getSubCategory(category:String){
     viewModelScope.launch(Dispatchers.IO){
         repo.getSubCategory(category)?.catch { error->_subCategory.value=ResponseState.Error(error) }
             ?.collect{data->_subCategory.value=ResponseState.Success(data)}
     }
    }

}