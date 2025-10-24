package pro.piechowski.highschoolstory.inspector

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.annotation.KoinInternalApi

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@KoinInternalApi
fun main() {
    InspectorApplication.launch()
}
