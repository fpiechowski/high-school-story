package pro.piechowski.highschoolstory.camera

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.IntervalSystem
import io.github.oshai.kotlinlogging.KotlinLogging
import ktx.graphics.moveTo
import ktx.math.plus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.character.player.PlayerCharacter
import pro.piechowski.highschoolstory.character.player.PlayerCharacterTag
import pro.piechowski.highschoolstory.rendering.sprite.CurrentSprite

class CameraMovementSystem :
    IntervalSystem(),
    KoinComponent {
    private val logger = KotlinLogging.logger { }

    private val pixelCamera by inject<Camera>(pixelCameraQualifier)
    private val meterCamera by inject<Camera>(meterCameraQualifier)

    override fun onTick() {
        val playerCharacter =
            PlayerCharacter(
                world
                    .family { all(PlayerCharacterTag, CurrentSprite) }
                    .single(),
            )

        pixelCamera.moveTo(
            Vector2(playerCharacter.sprite.sprite.x, playerCharacter.sprite.sprite.y) +
                Vector2(playerCharacter.sprite.sprite.originX, playerCharacter.sprite.sprite.originY),
        )

        meterCamera.moveTo(
            playerCharacter.body.body.position,
        )
    }
}
