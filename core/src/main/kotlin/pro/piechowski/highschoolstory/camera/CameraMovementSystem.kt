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
import pro.piechowski.highschoolstory.physics.m
import pro.piechowski.highschoolstory.rendering.sprite.CurrentSprite

class CameraMovementSystem :
    IntervalSystem(),
    KoinComponent {
    private val logger = KotlinLogging.logger { }

    private val cameraSet by inject<CameraSet>()

    override fun onTick() {
        world
            .family { all(PlayerCharacterTag, CurrentSprite) }
            .singleOrNull()
            ?.let { PlayerCharacter(it) }
            ?.let { playerCharacter ->
                cameraSet.moveTo(playerCharacter.body.body.position.x.m, playerCharacter.body.body.position.y.m)
            }
    }
}
