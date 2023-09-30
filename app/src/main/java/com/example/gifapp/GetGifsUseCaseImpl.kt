package com.example.gifapp

import javax.inject.Inject

class GetGifsUseCaseImpl @Inject constructor(
    private val gifsRepository: GifsRepository
) : GetGifsUseCase {
    override fun getGifs() = gifsRepository.getGifs()
}