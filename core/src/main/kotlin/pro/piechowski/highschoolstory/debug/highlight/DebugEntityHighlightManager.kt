package pro.piechowski.highschoolstory.debug.highlight

import com.github.quillraven.fleks.World
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.debug.ui.DebugUserInterface

class DebugEntityHighlightManager : KoinComponent {
    private val _highlighted = MutableStateFlow<Boolean>(false)

    val highlighted: StateFlow<Boolean> get() = _highlighted.asStateFlow()

    var highlightedValue get() = highlighted.value
        set(value) {
            _highlighted.value = value
        }
}
