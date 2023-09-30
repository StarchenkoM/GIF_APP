package com.example.gifapp

import com.example.gifapp.ui.home.GifUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GifsRepositoryImpl @Inject constructor(
//    private val gifsNetSource: GifsNetSource,
//    private val gifsDao: GifsDao,
//    private val mapper: GifsMapper,

) : GifsRepository {
    override fun getGifs(): Flow<List<GifUiModel>> {
        val testGifs = mutableListOf<GifUiModel>()
        for (i in 0..25) {
            val gifImage01 =
                "https://th.bing.com/th/id/R.ab4176a242be1235baf6f8f309346a80?rik=xJ09NPURCmGn4w&pid=ImgRaw&r=0"
            val gifImage02 =
                "https://th.bing.com/th/id/OIP.llFY_2VlxrXU16pmqrvHggHaLH?pid=ImgDet&rs=1"
            val image = if (i % 2 == 0) gifImage01 else gifImage02
            testGifs.add(
                GifUiModel(
                    gifName = "Name_$i",
                    gifLink = image
                )
            )
        }

        return flowOf(testGifs.toList())
    }
}