package pro.piechowski.highschoolstory.ui

import org.koin.dsl.module

val UserInterfaceModule =
    module {
        single { UserInterface() }
    }
