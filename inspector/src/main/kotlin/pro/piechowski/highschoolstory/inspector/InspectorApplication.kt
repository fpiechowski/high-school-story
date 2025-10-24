package pro.piechowski.highschoolstory.inspector

import io.github.oshai.kotlinlogging.KotlinLogging
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.annotation.KoinInternalApi
import pro.piechowski.highschoolstory.inspector.ecs.EcsView
import pro.piechowski.highschoolstory.inspector.ecs.EcsViewModel
import pro.piechowski.highschoolstory.inspector.koin.GlobalInstancesView
import pro.piechowski.highschoolstory.inspector.koin.GlobalInstancesViewModel
import pro.piechowski.highschoolstory.inspector.`object`.ObjectInspectorView
import pro.piechowski.highschoolstory.inspector.`object`.ObjectInspectorViewModel
import pro.piechowski.highschoolstory.inspector.runtime.RuntimeView
import pro.piechowski.highschoolstory.inspector.runtime.RuntimeViewModel

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@KoinInternalApi
class InspectorApplication : Application() {
    private val logger = KotlinLogging.logger { }

    private val adapters = Adapters()

    private val objectInspectorViewModel by lazy { ObjectInspectorViewModel(null) }
    private val objectInspectorView by lazy { ObjectInspectorView(objectInspectorViewModel) }

    private val globalInstancesViewModel by lazy { GlobalInstancesViewModel(adapters.globalInstances) }
    private val globalInstancesView by lazy { GlobalInstancesView(globalInstancesViewModel, objectInspectorViewModel) }

    private val ecsViewModel by lazy { EcsViewModel(adapters.ecs) }
    private val ecsView by lazy { EcsView(ecsViewModel, objectInspectorViewModel) }

    private val runtimeViewModel by lazy { RuntimeViewModel(adapters.runtime) }
    private val runtimeView by lazy { RuntimeView(runtimeViewModel) }

    private val inspectorApplicationView =
        InspectorApplicationView(
            InspectorApplicationViewModel(),
            globalInstancesView,
            ecsView,
            runtimeView,
            objectInspectorView,
        )

    override fun start(primaryStage: Stage) {
        primaryStage.scene = Scene(inspectorApplicationView.root)
        primaryStage.isMaximized = true
        primaryStage.show()
    }

    companion object {
        fun launch() = launch(InspectorApplication::class.java)
    }
}
