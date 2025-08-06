package pro.piechowski.highschoolstory.place

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent

class PlaceManager : KoinComponent {
    private val _currentPlace = MutableStateFlow<Place?>(null)

    val currentPlace: StateFlow<Place?> get() = _currentPlace

    fun openPlace(place: Place) {
        _currentPlace.value = place
    }
}
