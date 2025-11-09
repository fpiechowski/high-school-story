package pro.piechowski.highschoolstory

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.World
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ktx.assets.async.AssetStorage
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import pro.piechowski.highschoolstory.camera.GameCameraModule
import pro.piechowski.highschoolstory.camera.MainCameraModule
import pro.piechowski.highschoolstory.debug.DebugModule
import pro.piechowski.highschoolstory.debug.server.DebugServer
import pro.piechowski.highschoolstory.debug.server.FleksEntityComponentsProvider
import pro.piechowski.highschoolstory.debug.server.KoinObjectsProvider
import pro.piechowski.highschoolstory.debug.text.DebugTextSystem
import pro.piechowski.highschoolstory.ecs.invoke
import pro.piechowski.highschoolstory.input.GameInputModule
import pro.piechowski.highschoolstory.input.GameInputMultiplexer
import pro.piechowski.highschoolstory.input.MainInputModule
import pro.piechowski.highschoolstory.light.LightModule
import pro.piechowski.highschoolstory.map.MapModule
import pro.piechowski.highschoolstory.movement.MovementModule
import pro.piechowski.highschoolstory.physics.PhysicsModule
import pro.piechowski.highschoolstory.rendering.RenderingModule
import pro.piechowski.highschoolstory.transition.TransitionModule
import pro.piechowski.highschoolstory.ui.UserInterfaceViewport
import pro.piechowski.highschoolstory.weather.WeatherModule

@ExperimentalCoroutinesApi
@ExperimentalContextParameters
@KoinInternalApi
fun coreModule() =
    module {
        single { with(getKoin()) { Config.load() } }
        single {
            AssetStorage().apply {
                setLoader { TmxMapLoader(fileResolver) }
            }
        }
        single { SpriteBatch() }
        single { BitmapFont() }
        single { DebugTextSystem() }
        single { Json() }
        single { Stage(get<UserInterfaceViewport>()) }

        single { DebugServer() }
        single { KoinObjectsProvider() }
        single { FleksEntityComponentsProvider() }

        includes(MainInputModule)
        includes(MainCameraModule)
        includes(GameInputModule)
        includes(PhysicsModule)
        includes(MovementModule)
        includes(RenderingModule)
        includes(MapModule)
        includes(GameCameraModule)
        includes(DebugModule)
        includes(TransitionModule)
        includes(WeatherModule)
        includes(LightModule)

        single { GameScreen() }

        single { GameInputMultiplexer() }

        single<World> {
            World()
        }
    }
