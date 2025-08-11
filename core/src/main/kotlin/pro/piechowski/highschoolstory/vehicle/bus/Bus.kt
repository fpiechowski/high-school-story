package pro.piechowski.highschoolstory.vehicle.bus

import box2dLight.ConeLight
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ktx.box2d.body
import ktx.box2d.box
import org.koin.core.Koin
import pro.piechowski.highschoolstory.direction.Direction4
import pro.piechowski.highschoolstory.ecs.Archetype
import pro.piechowski.highschoolstory.ecs.plusAssign
import pro.piechowski.highschoolstory.exterior.ExteriorTexture
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.body.PhysicsBody
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection4
import pro.piechowski.highschoolstory.physics.px
import pro.piechowski.highschoolstory.power.Powered
import pro.piechowski.highschoolstory.spatial.Spatial
import pro.piechowski.highschoolstory.sprite.CurrentSprite

class Bus(
    override val entity: Entity,
) : Spatial {
    companion object {
        context(koin: Koin)
        suspend operator fun invoke(
            direction4: Direction4,
            color: BusColor,
        ) = Bus(
            koin.get<World>().entity {
                it += archetype(direction4, color)
            },
        )

        context(koin: Koin)
        suspend fun archetype(
            direction4: Direction4,
            color: BusColor,
        ) = with(koin) {
            Archetype {
                this += FaceDirection4(direction4)
                this +=
                    when (direction4) {
                        Direction4.Right ->
                            when (color) {
                                BusColor.YELLOW -> CurrentSprite(BusSprite.Yellow.Right(get<ExteriorTexture>()))
                            }

                        Direction4.Down ->
                            when (color) {
                                BusColor.YELLOW -> CurrentSprite(BusSprite.Yellow.Down(get<ExteriorTexture>()))
                            }

                        Direction4.Left ->
                            when (color) {
                                BusColor.YELLOW -> CurrentSprite(BusSprite.Yellow.Left(get<ExteriorTexture>()))
                            }

                        Direction4.Up ->
                            when (color) {
                                BusColor.YELLOW -> CurrentSprite(BusSprite.Yellow.Up(get<ExteriorTexture>()))
                            }
                    }
                val physicsBody =
                    PhysicsBody(
                        get<PhysicsWorld>()
                            .body {
                                box(336f.px.toMeter().value, 192f.px.toMeter().value)
                            },
                    )
                this += physicsBody
                this += Powered()
                this +=
                    VehicleLights.Headlights(
                        listOf(
                            ConeLight(
                                get(),
                                64,
                                Color(1f, 0.95f, 0.85f, 1f),
                                20f,
                                when (direction4) {
                                    Direction4.Right -> 10f
                                    else -> TODO()
                                },
                                when (direction4) {
                                    Direction4.Right -> 10f
                                    else -> TODO()
                                },
                                physicsBody.body.angle * MathUtils.radiansToDegrees,
                                20f,
                            ),
                        ),
                    )
            }
        }
    }
}
