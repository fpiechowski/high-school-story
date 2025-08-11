package pro.piechowski.highschoolstory.vehicle.bus

import box2dLight.ConeLight
import box2dLight.Light
import box2dLight.PointLight
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

sealed class VehicleLights(
    open val lights: List<Light>,
) {
    class Headlights(
        override val lights: List<ConeLight>,
    ) : VehicleLights(lights),
        Component<Headlights> {
        override fun type(): ComponentType<Headlights> = Headlights

        companion object : ComponentType<Headlights>()
    }

    class Taillights(
        override val lights: List<PointLight>,
    ) : VehicleLights(lights),
        Component<Taillights> {
        override fun type(): ComponentType<Taillights> = Taillights

        companion object : ComponentType<Taillights>()
    }
}
