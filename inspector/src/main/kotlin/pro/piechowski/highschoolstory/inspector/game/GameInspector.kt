package pro.piechowski.highschoolstory.inspector.game

import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.KoinInternalApi
import pro.piechowski.highschoolstory.inspector.SharedInspectorViewModel

@KoinInternalApi
class GameInspector(
    gameScope: CoroutineScope,
        sharedInspectorViewModel: SharedInspectorViewModel,
) {
    val model = GameInspectorModel(gameScope)
    val viewModel = GameInspectorViewModel(model)
    private val view = GameInspectorView(viewModel, sharedInspectorViewModel)
}
