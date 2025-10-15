package pro.piechowski.highschoolstory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.kotcrab.vis.ui.VisUI
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import ktx.scene2d.Scene2DSkin
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

class Main : KtxGame<KtxScreen>() {
    override fun create() {
        val koin = startKoin {}.koin

        koin.loadModules(listOf(mainModule()))

        KtxAsync.initiate()

        if (!VisUI.isLoaded()) VisUI.load()

        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("ui/skin/uiskin.json"))

        with(koin) {
            startGame()
        }
    }

    override fun dispose() {
        super.dispose()

        stopKoin()
    }

    context(koin: Koin)
    fun startGame() {
        loadKoinModules(koin.get<Module>(gameModuleQualifier))
        addScreen(koin.get<GameScreen>())
        setScreen<GameScreen>()
    }
}
