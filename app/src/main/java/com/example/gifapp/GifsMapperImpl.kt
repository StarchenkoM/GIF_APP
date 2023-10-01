package com.example.gifapp

import com.example.gifapp.ui.home.GifUiItem

class GifsMapperImpl : GifsMapper {
    override fun convertNetToDataBaseItem(item: GifNetItem): GifDBItem {
        TODO("Not yet implemented")
    }

    override fun convertNetToUiItem(item: GifNetItem): GifUiItem {
        return GifUiItem(
            title = item.gifName,
            gifLink = item.gifLink,
        )
    }

    override fun convertDataBaseItemToUiItem(item: GifDBItem): GifUiItem {
        TODO("Not yet implemented")
    }
}