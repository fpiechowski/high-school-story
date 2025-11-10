package pro.piechowski.highschoolstory.physics.body.character

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import ktx.box2d.body
import ktx.box2d.box
import pro.piechowski.highschoolstory.sprite.character.CharacterSprite
import pro.piechowski.kge.koin
import pro.piechowski.kge.physics.PhysicsWorld
import pro.piechowski.kge.physics.body.PhysicsBody
import pro.piechowski.kge.physics.px

object CharacterBody {
    operator fun invoke(): PhysicsBody =
        koin
            .get<PhysicsWorld>()
            .body(BodyDef.BodyType.DynamicBody) {
                box(
                    CharacterSprite.size.x.px
                        .toMeter()
                        .value,
                    CharacterSprite.size.y.px
                        .toMeter()
                        .value / HEIGHT_TO_DEPTH_RATIO,
                    bodyFixturePositionOffset,
                )
            }.let { PhysicsBody(it) }

    val bodyFixturePositionOffset =
        Vector2(
            0f,
            -(CharacterSprite.size.x / HEIGHT_TO_DEPTH_RATIO).px.toMeter().value,
        )

    const val HEIGHT_TO_DEPTH_RATIO = 4f
}
