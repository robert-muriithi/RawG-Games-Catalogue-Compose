package dev.robert.gametrail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.datastore.domain.usecase.ThemeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    themeUseCase: ThemeUseCase
) : ViewModel() {

    val theme = themeUseCase.getTheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0,
        )


}