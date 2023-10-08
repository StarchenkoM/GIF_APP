package com.example.gifapp

import com.example.gifapp.ui.home.GifUiItem

class GifsMapperImpl : GifsMapper {
    // TODO: use extensions?
    override fun mapToDataBaseItem(item: GifNetItem): GifDBEntity {
        return GifDBEntity(
            id = item.id,
            title = item.title,
            link = item.images.imageDetails.url,
        )
    }

    override fun mapToUiItem(item: GifNetItem): GifUiItem {
        return GifUiItem(
            title = item.title,
            link = item.images.imageDetails.url,
        )
    }

    override fun mapToUiItem(item: GifDBEntity): GifUiItem {
        return GifUiItem(
            title = item.title,
            link = item.link,
        )
    }
}