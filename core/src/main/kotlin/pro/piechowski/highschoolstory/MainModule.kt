package pro.piechowski.highschoolstory

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.assets.async.AssetStorage
import org.koin.dsl.module
import pro.piechowski.highschoolstory.camera.MainCameraModule
import pro.piechowski.highschoolstory.debug.text.DebugTextSystem
import pro.piechowski.highschoolstory.input.MainInputModule
import pro.piechowski.highschoolstory.ui.UserInterfaceViewport

fun mainModule() =
    module {
        includes(MainInputModule)
        includes(MainCameraModule)

        single { Config.load() }
        single {
            AssetStorage().apply {
                setLoader { TmxMapLoader(fileResolver) }
            }
        }
        single { SpriteBatch() }
        single { BitmapFont() }
        single { DebugTextSystem() }
        single { Json() }
        single(gameModuleQualifier) { gameModule() }
        single { Stage(get<UserInterfaceViewport>()) }
    }
