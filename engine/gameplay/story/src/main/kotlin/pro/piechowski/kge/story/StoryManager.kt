package pro.piechowski.kge.story

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StoryManager : KoinComponent {
    private val story: Story by inject()
    private val _completedBeats = MutableStateFlow<List<Story.Beat>>(emptyList())
    private val _currentBeat = MutableStateFlow<Story.Beat?>(null)

    val currentBeat: StateFlow<Story.Beat?> = _currentBeat
    val completedBeats: StateFlow<List<Story.Beat>> = _completedBeats

    suspend fun play(beat: Story.Beat) {
        beat.play()
        _completedBeats.update { it + beat }
    }
}
