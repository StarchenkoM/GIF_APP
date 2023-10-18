package com.example.gifapp.domain.usecases

import com.example.gifapp.domain.GifsRepository
import javax.inject.Inject

class DeleteGifsUseCaseImpl @Inject constructor(
    private val gifsRepository: GifsRepository
) : DeleteGifsUseCase {

    override suspend fun deleteGifs() {
        gifsRepository.deleteAllGifs()
    }
}