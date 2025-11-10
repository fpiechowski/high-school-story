package pro.piechowski.highschoolstory

import com.sksamuel.hoplite.PropertySource
import org.koin.dsl.module
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

val highSchoolStoryModule =
    module {
        single<PropertySource> { PropertySource.resource("/config.yml") }
        single { IntroScene() }
        single<Entrypoint> { GameEntrypoint() }
        single { systemComposer }
        single { ExteriorTexture() }
        single { UserInterface() }
        single { PlayerCharacterSpriteSheet() }

        includes(CharacterModule)
        includes(DialogueModule)
        includes(TimeModule)
        includes(InteractionModule)
        includes(StoryModule)
        includes(SceneModule)
    }
