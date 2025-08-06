package pro.piechowski.highschoolstory.debug.selection

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.debug.ui.DebugUserInterface

class DebugEntitySelectionManager : KoinComponent {
    private val _selectedEntity = MutableStateFlow<Entity?>(null)

    val selectedEntity: StateFlow<Entity?> get() = _selectedEntity.asStateFlow()

    var currentSelectedEntity get() = selectedEntity.value
        set(value) {
            _selectedEntity.value = value
        }

    private val debugUserInterface by inject<DebugUserInterface>()

    private val world by inject<World>()

    init {
        KtxAsync.launch {
            selectedEntity.collect { entity ->
                with(world) {
                    debugUserInterface.debugLabel.setText(entity?.toString() ?: "")
                }
            }
        }
    }
}
