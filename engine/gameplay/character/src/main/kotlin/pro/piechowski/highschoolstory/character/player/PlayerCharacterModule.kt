package pro.piechowski.highschoolstory.character.player

import org.koin.dsl.module

val PlayerCharacterModule =
    module {
        single { PlayerCharacterManager() }
    }
