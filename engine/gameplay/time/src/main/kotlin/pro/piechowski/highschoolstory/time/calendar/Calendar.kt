package pro.piechowski.highschoolstory.time.calendar

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent

class Calendar : KoinComponent {
    private val _date = MutableStateFlow(LocalDate(2015, 8, 29))

    val date = _date.asStateFlow()

    var currentDate get() = date.value
        set(value) {
            _date.value = value
        }
}
