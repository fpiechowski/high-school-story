package pro.piechowski.highschoolstory

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Suppress("ktlint:standard:backing-property-naming", "ObjectPropertyName")
internal val _koinModulesInitialized = MutableStateFlow(false)

val koinModulesInitialized: StateFlow<Boolean> = _koinModulesInitialized
