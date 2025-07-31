package pro.piechowski.highschoolstory.map

import org.koin.dsl.module

val MapModule =
    module {
        single { MapManager() }
        single { MapRenderingSystem.Background() }
        single { MapRenderingSystem.Foreground() }
    }
