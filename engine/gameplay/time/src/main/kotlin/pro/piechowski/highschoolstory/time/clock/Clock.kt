package pro.piechowski.highschoolstory.time.clock

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.koin.core.component.KoinComponent
import kotlin.time.Duration.Companion.minutes

class Clock : KoinComponent {
    private val _time = MutableStateFlow(LocalTime(17, 0, 0))

    val time = _time.asStateFlow()

    var currentTime get() = time.value
        set(value) {
            _time.value = value
        }

    companion object {
        val gameTimeInterval = 15.minutes

        val wakeUpTime = LocalTime(6, 0, 0)
        val sleepTime = LocalTime(22, 0, 0)
        val curfewTime = LocalTime(21, 0, 0)
    }
}
