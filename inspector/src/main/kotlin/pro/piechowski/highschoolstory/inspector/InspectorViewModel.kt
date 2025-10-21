package pro.piechowski.highschoolstory.inspector

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class InspectorViewModel {
    private val _opened = MutableStateFlow(false)
    val opened: StateFlow<Boolean> = _opened

    fun open() {
        _opened.value = true
    }
}
