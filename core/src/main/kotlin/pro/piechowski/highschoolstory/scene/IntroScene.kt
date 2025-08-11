package pro.piechowski.highschoolstory.scene

import com.github.quillraven.fleks.World
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import ktx.async.KtxAsync
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.CameraManager
import pro.piechowski.highschoolstory.camera.CameraSet
import pro.piechowski.highschoolstory.character.says
import pro.piechowski.highschoolstory.dialogue.DialogueManager
import pro.piechowski.highschoolstory.dialogue.await
import pro.piechowski.highschoolstory.dialogue.dialogue
import pro.piechowski.highschoolstory.direction.Direction4
import pro.piechowski.highschoolstory.map.Tile
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.physics.times
import pro.piechowski.highschoolstory.place.PlaceManager
import pro.piechowski.highschoolstory.place.Road
import pro.piechowski.highschoolstory.state.GameStateManager
import pro.piechowski.highschoolstory.time.calendar.Calendar
import pro.piechowski.highschoolstory.time.clock.Clock
import pro.piechowski.highschoolstory.transition.Transition
import pro.piechowski.highschoolstory.transition.TransitionManager
import pro.piechowski.highschoolstory.transition.await
import pro.piechowski.highschoolstory.vehicle.bus.Bus
import pro.piechowski.highschoolstory.vehicle.bus.BusColor
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class IntroScene :
    Scene(),
    KoinComponent {
    private val placeManager by inject<PlaceManager>()
    private val cameraSet by inject<CameraSet>()

    private val world by inject<World>()
    private val dialogueManager by inject<DialogueManager>()
    private val gameStateManager by inject<GameStateManager>()
    private val cameraManager by inject<CameraManager>()
    private val transitionManager by inject<TransitionManager>()
    private val clock by inject<Clock>()
    private val calendar by inject<Calendar>()

    override suspend fun play() =
        KtxAsync.launch {
            with(world) {
                with(getKoin()) {
                    calendar.currentDate = LocalDate(2020, 8, 29)
                    clock.currentTime = LocalTime(17, 0, 0)

                    transitionManager.play(Transition.FadeBlack.In(Duration.ZERO, 1f))

                    Bus(Direction4.Right, BusColor.YELLOW).apply {
                        position = Tile.Position(15, 8).toPixel() * px.toMeter()
                    }

                    cameraManager.stopFollowingPlayerCharacter()
                    cameraSet.moveTo(720f.px, 480f.px)

                    placeManager.travelTo(Road)

                    delay(1000)

                    transitionManager
                        .play(Transition.FadeBlack.Out(3.seconds))
                        .await()

                    delay(1000)

                    val playerCharacter = gameStateManager.currentGameState.playerCharacter

                    dialogueManager
                        .startDialogue(
                            dialogue {
                                playerCharacter.says("Ehhhh...")
                            },
                        ).await()

                    delay(2000)

                    dialogueManager
                        .startDialogue(
                            dialogue {
                                playerCharacter.says("Trzy kolejne lata bez starych.")
                            },
                        ).await()
                }
            }
        }
}
