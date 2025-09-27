package com.example.lab_week_05_00000097514

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.lab_week_05_00000097514.api.CatApiService
import com.example.lab_week_05_00000097514.model.ImageData
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val catApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }

    private lateinit var apiResponseView: TextView
    private lateinit var imageResultView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiResponseView = findViewById(R.id.api_response)
        imageResultView = findViewById(R.id.image_result)

        getCatImageResponse()
    }

    private fun getCatImageResponse() {
        val call = catApiService.searchImages(1, "full")
        call.enqueue(object : Callback<List<ImageData>> {
            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                Log.e(MAIN_ACTIVITY, "Failed to get response", t)
            }

            override fun onResponse(
                call: Call<List<ImageData>>,
                response: Response<List<ImageData>>
            ) {
                if (response.isSuccessful) {
                    val imageList = response.body()
                    val firstImage = imageList?.firstOrNull()

                    val firstImageUrl = firstImage?.imageUrl.orEmpty()

                    val breedName = if (!firstImage?.breeds.isNullOrEmpty()) {
                        firstImage?.breeds?.firstOrNull()?.name ?: "Unknown"
                    } else {
                        "Unknown"
                    }

                    if (firstImageUrl.isNotBlank()) {
                        Glide.with(this@MainActivity)
                            .load(firstImageUrl)
                            .into(imageResultView)
                    }

                    apiResponseView.text = "Breed: $breedName"
                } else {
                    Log.e(
                        MAIN_ACTIVITY,
                        "Failed to get response\n" + response.errorBody()?.string().orEmpty()
                    )
                }
            }

        })
    }

    companion object {
        const val MAIN_ACTIVITY = "MAIN_ACTIVITY"
    }
}
