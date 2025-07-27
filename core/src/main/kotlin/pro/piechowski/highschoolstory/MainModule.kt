package pro.piechowski.highschoolstory

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.assets.async.AssetStorage
import org.koin.dsl.module
import pro.piechowski.highschoolstory.debug.DebugTextSystem
import pro.piechowski.highschoolstory.rendering.BeginRenderingBatchSystem
import pro.piechowski.highschoolstory.rendering.EndRenderingBatchSystem
import pro.piechowski.highschoolstory.ui.uiViewportQualifier

fun mainModule() =
    module {
        single { Config.load() }
        single { AssetStorage() }
        single { SpriteBatch() }
        single { BitmapFont() }
        single { BeginRenderingBatchSystem() }
        single { EndRenderingBatchSystem() }
        single { DebugTextSystem() }
        single<InputProcessor> { InputAdapter() }
        single { InputMultiplexer(get<InputProcessor>(), get<Stage>()) }
        single { Json() }
        single(gameModuleQualifier) { gameModule() }
        single<Viewport>(uiViewportQualifier) { FitViewport(1280f, 720f) }
        single { Stage(get(uiViewportQualifier)) }
    }
