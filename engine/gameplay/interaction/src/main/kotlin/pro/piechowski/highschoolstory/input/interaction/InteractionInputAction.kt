package pro.piechowski.highschoolstory.input.interaction

import com.badlogic.gdx.Input
import pro.piechowski.highschoolstory.input.InputAction
import pro.piechowski.highschoolstory.input.InputMapping

val InputAction.Companion.Interaction get() = InputAction("interaction")

val InteractionInputMapping = InputAction.Interaction to listOf(Input.Keys.ENTER)
