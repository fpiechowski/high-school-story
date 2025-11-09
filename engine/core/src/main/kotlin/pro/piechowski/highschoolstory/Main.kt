package pro.piechowski.highschoolstory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.github.quillraven.fleks.IntervalSystem
import com.kotcrab.vis.ui.VisUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import ktx.scene2d.Scene2DSkin
import org.koin.core.Koin
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

@ExperimentalCoroutinesApi
@KoinInternalApi
@ExperimentalContextParameters
class Main(
    private val koin: Koin,
) : KtxGame<KtxScreen>() {
    override fun create() {
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
    private fun startGame() {
        addScreen(get<GameScreen>())
        setScreen<GameScreen>()
    }
}
