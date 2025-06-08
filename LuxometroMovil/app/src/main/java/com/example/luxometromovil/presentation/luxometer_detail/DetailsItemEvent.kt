package com.example.luxometromovil.presentation.luxometer_detail

sealed class DetailsItemEvent {
    data class GetDetailItemById(val id: Int): DetailsItemEvent()
}