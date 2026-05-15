package com.kumbarakala.app.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kumbarakala.app.ui.screens.StoryCardState

class CardViewModel : ViewModel() {

    val currentCardState = mutableStateOf<StoryCardState?>(null)

    fun saveCard(state: StoryCardState) {
        currentCardState.value = state
    }
}