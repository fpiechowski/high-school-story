package pro.piechowski.highschoolstory.inspector

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class SharedInspectorViewModel {
    private val anyInspectorFocused = MutableSharedFlow<Unit>()
    suspend fun notifyAnyInspectorFocused() = anyInspectorFocused.emit(Unit)

    val toFrontAction: Flow<Unit> = anyInspectorFocused
}
