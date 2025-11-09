package pro.piechowski.highschoolstory.physics.body.character

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import ktx.box2d.body
import ktx.box2d.box
import pro.piechowski.highschoolstory.character.CharacterBase
import pro.piechowski.highschoolstory.physics.PhysicsWorld
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.sprite.character.CharacterSprite

object CharacterBody {
    context(physicsWorld: PhysicsWorld)
    operator fun invoke(): Body =
        physicsWorld.body(BodyDef.BodyType.DynamicBody) {
            box(
                CharacterSprite.Companion.WIDTH.px
                    .toMeter()
                    .value,
                CharacterSprite.Companion.HEIGHT.px
                    .toMeter()
                    .value / CharacterBase.Companion.HEIGHT_TO_DEPTH_RATIO,
                bodyFixturePositionOffset,
            )
        }

    val bodyFixturePositionOffset =
        Vector2(
            0f,
            -(CharacterSprite.Companion.HEIGHT / CharacterBase.Companion.HEIGHT_TO_DEPTH_RATIO).px.toMeter().value,
        )
}
