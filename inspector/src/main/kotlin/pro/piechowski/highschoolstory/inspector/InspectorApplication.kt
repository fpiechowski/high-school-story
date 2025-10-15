package pro.piechowski.highschoolstory.inspector

import javafx.application.Application
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.kodein.di.DI
import org.kodein.di.instance
import org.koin.core.annotation.KoinInternalApi
import pro.piechowski.highschoolstory.inspector.ecs.EcsInspector
import pro.piechowski.highschoolstory.inspector.game.GameInspector
import pro.piechowski.highschoolstory.inspector.koin.KoinInspector
import pro.piechowski.highschoolstory.inspector.koin.KoinInspectorView
import pro.piechowski.highschoolstory.lwjgl3.launchLwjgl3

@KoinInternalApi
class InspectorApplication : Application() {
    private val gameScope = CoroutineScope(newSingleThreadContext("GameScope"))

    private val gameInspector = GameInspector(gameScope)
    private val koinInspector = KoinInspector(gameScope)
    private val ecsInspector = EcsInspector(gameScope)

    override fun start(primaryStage: Stage?) {
        gameInspector.show()
        koinInspector.show()
        ecsInspector.show()
    }

    companion object {
        fun launch() = launch(InspectorApplication::class.java)
    }
}
