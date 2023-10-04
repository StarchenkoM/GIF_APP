package com.example.gifapp

import com.google.gson.annotations.SerializedName

data class GifsApiResponse(
    @SerializedName("data")
    val gifs: List<GifNetItem>,
)

data class GifNetItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String = "no title",
    @SerializedName("images")
    val images: Images
)

data class Images(
//    @SerializedName("original")
//    @SerializedName("fixed_height")
    @SerializedName("fixed_height_downsampled")
    val imageDetails: ImageDetails,
)

data class ImageDetails(
    @SerializedName("url")
    val url: String,
)
