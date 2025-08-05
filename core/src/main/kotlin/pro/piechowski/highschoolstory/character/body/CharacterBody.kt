package pro.piechowski.highschoolstory.character.body

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import ktx.box2d.body
import ktx.box2d.box
import pro.piechowski.highschoolstory.character.Character.HEIGHT_TO_DEPTH_RATIO
import pro.piechowski.highschoolstory.character.rendering.CharacterSprite
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.px

object CharacterBody {
    context(physicsWorld: PhysicsWorld)
    operator fun invoke(): Body =
        physicsWorld.body(BodyDef.BodyType.DynamicBody) {
            box(
                CharacterSprite.WIDTH.px
                    .toMeter()
                    .value,
                CharacterSprite.HEIGHT.px
                    .toMeter()
                    .value / HEIGHT_TO_DEPTH_RATIO,
                bodyFixturePositionOffset,
            )
        }

    val bodyFixturePositionOffset = Vector2(0f, -(CharacterSprite.HEIGHT / HEIGHT_TO_DEPTH_RATIO).px.toMeter().value)
}
