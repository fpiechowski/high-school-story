package pro.piechowski.highschoolstory.vehicle.bus

import box2dLight.ConeLight
import com.badlogic.gdx.graphics.Color
import pro.piechowski.kge.direction.Direction4
import pro.piechowski.kge.di.DependencyInjection.Global.get
import pro.piechowski.kge.physics.body.PhysicsBody
import pro.piechowski.kge.vehicle.VehicleLights

object BusLights {
    object Headlights {
        operator fun invoke(
            direction4: Direction4,
            physicsBody: PhysicsBody,
        ) = VehicleLights.Headlights(
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
                    direction4.angleDegrees,
                    20f,
                ).apply {
                    attachToBody(physicsBody.body)
                },
            ),
        )
    }
}
