package pro.piechowski.highschoolstory

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import org.koin.core.context.startKoin

class Main : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        startKoin {
            modules(mainModule)
        }

        addScreen(GameScreen())

        setScreen<GameScreen>()
    }
}

