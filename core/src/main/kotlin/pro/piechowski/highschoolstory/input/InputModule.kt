package pro.piechowski.highschoolstory.input

import org.koin.dsl.module

val MainInputModule =
    module {
        single { InputState() }
        single { GameInputMultiplexer() }
    }

val GameInputModule =
    module {
        single { GameInputMultiplexer() }
    }
