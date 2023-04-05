package com.bignerdranch.android.photogallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.photogallery.data.GalleryItem
import com.bignerdranch.android.photogallery.databinding.FragmentPhotoPageBinding
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "PhotoGalleryViewModel"

class PhotoGalleryViewModel : ViewModel() {
    private val photoRepository = PhotoRepository()
    private val preferencesRepository = PreferencesRepository.get()

//    private val _galleryItems: MutableStateFlow<List<GalleryItem>> =
//        MutableStateFlow(emptyList())
//    val galleryItems: StateFlow<List<GalleryItem>>
//        get() = _galleryItems.asStateFlow()

    private val _uiState: MutableStateFlow<PhotoGalleryUiState> = MutableStateFlow(PhotoGalleryUiState())
    val uiState: StateFlow<PhotoGalleryUiState>
        get() = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            preferencesRepository.storedQuery.collectLatest { storedQuery ->
                try {
                    val items = fetchGalleryItems(storedQuery)
                    Log.d(TAG, "Items received: $items")
                    _uiState.update { oldState ->
                        oldState.copy(images = items, query = storedQuery)

                    }
                } catch (ex: Exception) {
                    Log.e(TAG, "Failed to fetch gallery items", ex)
                }
            }
        }

        viewModelScope.launch {
            preferencesRepository.isPolling.collect { isPolling ->
                _uiState.update {
                    it.copy(isPolling = isPolling)
                }
            }
        }
    }


    fun setQuery(query: String) {
        viewModelScope.launch {
//            _galleryItems = fetchGalleryItems(query)
            preferencesRepository.setStoredQuery(query)
        }
    }

    fun toggleIsPolling() {
        viewModelScope.launch {
            preferencesRepository.setIsPolling(!uiState.value.isPolling)
        }
    }

    private suspend fun fetchGalleryItems(query: String): List<GalleryItem> {
        return if (query.isNotEmpty()) {
            photoRepository.searchPhotos(query)
        } else {
            photoRepository.fetchPhotos()
        }
    }
}

data class PhotoGalleryUiState(
    val images: List<GalleryItem> = listOf(),
    val query: String = "",
    val isPolling: Boolean = false,
)
