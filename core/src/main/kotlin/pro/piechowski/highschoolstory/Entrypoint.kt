package pro.piechowski.highschoolstory

import arrow.fx.coroutines.await.ExperimentalAwaitAllApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pro.piechowski.highschoolstory.asset.Assets
import pro.piechowski.highschoolstory.asset.AssetsLoader
import pro.piechowski.highschoolstory.character.player.PlayerCharacter
import pro.piechowski.highschoolstory.sprite.character.player.PlayerCharacterSpriteSheet
import pro.piechowski.kge.di.DependencyInjection.Global.get
import pro.piechowski.kge.Entrypoint
import pro.piechowski.kge.input.InputManager

@ExperimentalCoroutinesApi
@ExperimentalAwaitAllApi
class SandboxEntrypoint : Entrypoint {
    override suspend fun run() {
        get<AssetsLoader>().load()

        val playerCharacter = PlayerCharacter("Test", "Character")

        get<InputManager>().passOwnership(playerCharacter)
    }
}

class GameEntrypoint : Entrypoint {
    override suspend fun run() = TODO()
}
