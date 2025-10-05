package com.example.resepmakanan.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.resepmakanan.data.Repository
import com.example.resepmakanan.models.FoodRecipe
import com.example.resepmakanan.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                // Penanganan error yang lebih spesifik
                recipesResponse.value = when (e) {
                    is UnknownHostException -> NetworkResult.Error("Tidak dapat terhubung ke server. Periksa koneksi internet Anda.")
                    is java.net.SocketTimeoutException -> NetworkResult.Error("Timeout: Server terlalu lama merespons")
                    is java.net.ConnectException -> NetworkResult.Error("Tidak dapat terhubung ke server")
                    else -> NetworkResult.Error("Terjadi kesalahan: ${e.localizedMessage ?: "Unknown error"}")
                }
            }
        } else {
            recipesResponse.value = NetworkResult.Error("Tidak ada koneksi internet")
        }
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        return when {
            response.message().toString().contains("timeout", true) -> NetworkResult.Error("Timeout")
            response.code() == 402 -> NetworkResult.Error("API Key Limited")
            response.code() == 401 -> NetworkResult.Error("API Key Invalid")
            response.code() == 429 -> NetworkResult.Error("API Rate Limit Exceeded")
            response.code() == 500 -> NetworkResult.Error("Server Error")
            response.code() == 502 -> NetworkResult.Error("Bad Gateway")
            response.code() == 503 -> NetworkResult.Error("Service Unavailable")
            response.isSuccessful -> {
                val foodRecipes = response.body()
                if (foodRecipes != null) {
                    NetworkResult.Success(foodRecipes)
                } else {
                    NetworkResult.Error("Data tidak ditemukan")
                }
            }
            else -> NetworkResult.Error("Error ${response.code()}: ${response.message()}")
        }
    }

    private fun hasInternetConnection(): Boolean {
        return try {
            val connectivityManager = getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }

    // Fungsi tambahan untuk mengecek status koneksi lebih detail
    fun getNetworkStatus(): String {
        return if (hasInternetConnection()) {
            "Terhubung ke internet"
        } else {
            "Tidak terhubung ke internet"
        }
    }
}