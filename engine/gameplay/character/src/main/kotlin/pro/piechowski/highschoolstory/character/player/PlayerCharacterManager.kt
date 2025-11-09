package pro.piechowski.highschoolstory.character.player

import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

class PlayerCharacterManager : KoinComponent {
    val playerCharacter = MutableStateFlow<PlayerCharacterBase?>(null)
}
