package pro.piechowski.highschoolstory.camera.character

import com.github.quillraven.fleks.IntervalSystem
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.camera.CameraManager
import pro.piechowski.highschoolstory.camera.MeterCamera
import pro.piechowski.highschoolstory.character.player.PlayerCharacterManager
import pro.piechowski.highschoolstory.physics.m

class CameraFollowingPlayerCharacterSystem :
    IntervalSystem(),
    KoinComponent {
    private val logger = KotlinLogging.logger { }

    private val meterCamera by inject<MeterCamera>()
    private val cameraManager by inject<CameraManager>()
    private val playerCharacterManager: PlayerCharacterManager by inject()

    override fun onTick() {
        if (cameraManager.followingPlayerCharacterValue) {
            playerCharacterManager.playerCharacter.value?.let { playerCharacter ->
                meterCamera.moveTo(
                    playerCharacter.body.body.position.x.m,
                    playerCharacter.body.body.position.y.m,
                )
            }
        }
    }
}
