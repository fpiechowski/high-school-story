package pro.piechowski.highschoolstory

import com.sksamuel.hoplite.PropertySource
import org.koin.dsl.module
import pro.piechowski.highschoolstory.exterior.ExteriorTexture
import pro.piechowski.highschoolstory.ui.UserInterface
import pro.piechowski.kge.scene.IntroScene

val highSchoolStoryModule =
    module {
        single<PropertySource> { PropertySource.resource("/config.yml") }
        single { IntroScene() }
        single<Entrypoint> { GameEntrypoint() }
        single { systemComposer }
        single { ExteriorTexture() }
        single { UserInterface() }
    }
