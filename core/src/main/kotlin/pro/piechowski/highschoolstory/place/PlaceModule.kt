package pro.piechowski.highschoolstory.place

import org.koin.dsl.module

val PlaceModule =
    module {
        single { PlaceManager() }
    }
