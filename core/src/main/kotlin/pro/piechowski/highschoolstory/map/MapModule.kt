package pro.piechowski.highschoolstory.map

import org.koin.dsl.module
import pro.piechowski.highschoolstory.place.PlaceManager

val MapModule =
    module {
        single { MapManager() }
        single { MapRenderingSystem.Background() }
        single { MapRenderingSystem.Foreground() }
    }
