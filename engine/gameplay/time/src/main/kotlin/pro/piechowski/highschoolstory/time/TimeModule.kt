package pro.piechowski.highschoolstory.time

import org.koin.dsl.module
import pro.piechowski.highschoolstory.time.calendar.Calendar
import pro.piechowski.highschoolstory.time.clock.Clock

val TimeModule =
    module {
        single<Calendar> { Calendar() }
        single<Clock> { Clock() }
    }
