package pro.piechowski.highschoolstory.inspector

import javafx.application.Platform
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.property.SimpleObjectProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch

fun <T> StateFlow<T>.asObservableValue(scope: CoroutineScope): ReadOnlyObjectProperty<T> = this.asObservableValue(scope, value)

fun <T> Flow<T>.asObservableValue(
    scope: CoroutineScope,
    initialValue: T,
): ReadOnlyObjectProperty<T> {
    val prop = SimpleObjectProperty(initialValue)

    scope.launch(Dispatchers.JavaFx) {
        collect { newValue ->
            if (Platform.isFxApplicationThread()) {
                prop.set(newValue)
            } else {
                Platform.runLater { prop.set(newValue) }
            }
        }
    }

    return prop
}

fun <T> MutableStateFlow<T>.asBidirectionalObservableValue(scope: CoroutineScope): ObjectProperty<T> {
    val prop = SimpleObjectProperty(this.value)

    scope.launch(Dispatchers.JavaFx) {
        this@asBidirectionalObservableValue.collect { newValue ->
            if (prop.value != newValue) {
                if (Platform.isFxApplicationThread()) {
                    prop.set(newValue)
                } else {
                    Platform.runLater { prop.set(newValue) }
                }
            }
        }
    }

    prop.addListener { _, _, newValue ->
        if (this@asBidirectionalObservableValue.value != newValue) {
            this@asBidirectionalObservableValue.value = newValue
        }
    }

    return prop
}
