package pro.piechowski.kge.scene

import com.github.quillraven.fleks.World
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ktx.async.KTX
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.physics.PhysicsWorld
import pro.piechowski.highschoolstory.physics.removeAll

class SceneManager : KoinComponent {
    private val coroutineScope = CoroutineScope(Dispatchers.KTX + SupervisorJob())

    private val _currentScene = MutableStateFlow<Scene?>(null)
    val currentScene = _currentScene.asStateFlow()

    private val world: World by inject()
    private val physicsWorld: PhysicsWorld by inject()

    fun play(
        scene: Scene,
        additive: Boolean = false,
    ): Job =
        coroutineScope.launch {
            if (!additive) {
                world.removeAll()
                physicsWorld.removeAll()
            }

            scene.play()
        }
}
