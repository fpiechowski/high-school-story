package pro.piechowski.highschoolstory

import ktx.async.newAsyncContext
import kotlin.math.min

object CoroutineContexts {
    val IO = newAsyncContext(4, "IO")
    val Logic = newAsyncContext(maxOf(1, min(Runtime.getRuntime().availableProcessors() - 1, 4)), "Logic")
}
