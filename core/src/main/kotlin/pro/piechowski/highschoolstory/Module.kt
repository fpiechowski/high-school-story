@file:OptIn(ExperimentalAwaitAllApi::class)

package pro.piechowski.highschoolstory

import arrow.fx.coroutines.await.ExperimentalAwaitAllApi
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.sksamuel.hoplite.PropertySource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module
import pro.piechowski.highschoolstory.asset.AssetsLoader
import pro.piechowski.highschoolstory.exterior.ExteriorTexture
import pro.piechowski.highschoolstory.scene.intro.IntroScene
import pro.piechowski.highschoolstory.sprite.character.player.PlayerCharacterSpriteSheet
import pro.piechowski.highschoolstory.ui.UserInterface
import pro.piechowski.kge.Entrypoint
import pro.piechowski.kge.character.CharacterModule
import pro.piechowski.kge.dialogue.DialogueModule
import pro.piechowski.kge.interaction.InteractionModule
import pro.piechowski.kge.scene.SceneModule
import pro.piechowski.kge.story.StoryModule
import pro.piechowski.kge.time.TimeModule
import pro.piechowski.kge.weather.WeatherModule
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
val highSchoolStoryModule =
    module {
        single<PropertySource> { PropertySource.resource("/config.yml") }
        single { IntroScene() }
        single<Entrypoint> { GameEntrypoint() }
        single { systemComposer }
        single { UserInterface() }
        single { Skin(Gdx.files.internal("ui/skin/uiskin.json")) }
        single { AssetsLoader() }
        single { get<AssetsLoader>().assets }
        single { PlayerCharacterSpriteSheet() }
        single { ExteriorTexture() }

        includes(CharacterModule)
        includes(DialogueModule)
        includes(TimeModule)
        includes(InteractionModule)
        includes(StoryModule)
        includes(SceneModule)
        includes(WeatherModule)
    }
