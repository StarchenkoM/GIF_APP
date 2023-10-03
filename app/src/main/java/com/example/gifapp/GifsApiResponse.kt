package com.example.gifapp

import com.google.gson.annotations.SerializedName

//data class GifsApiResponse(
//    @SerializedName("data")
//    val response: List<GifApi>,
//)
//
//data class GifApi(
////    @SerializedName("title")
////    val title: String,
//    @SerializedName("images")
//    val images: GifImageOriginal,
//)
//
//data class GifImageOriginal(
//    @SerializedName("original")
//    val original: GifData,
//)
//
//data class GifData(
//    @SerializedName("url")
//    val url: String,
//)


data class GifsApiResponse(
    @SerializedName("data")
    val gifs: List<GifImage>,
    // Other relevant fields from the response can be added here
)

data class GifImage(
//    @SerializedName("id")
//    val id: String,

    @SerializedName("title")
    val title: String = "no title",

    @SerializedName("images")
    val images: Images
    // Add other relevant fields as needed
)

data class Images(
//    @SerializedName("original")
//    @SerializedName("fixed_height")
    @SerializedName("fixed_height_downsampled")
    val original: ImageDetails,
    // Add other relevant image details as needed
)

data class ImageDetails(
    @SerializedName("url")
    val url: String,
    // Add other relevant image details as needed
)
