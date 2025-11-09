package pro.piechowski.highschoolstory.interaction.interactable

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import ktx.graphics.use
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.piechowski.highschoolstory.interaction.interactor.Interactor
import pro.piechowski.highschoolstory.interaction.interactor.Interactors
import pro.piechowski.highschoolstory.interaction.isInInteractionRangeOf
import pro.piechowski.highschoolstory.physics.m
import pro.piechowski.highschoolstory.sprite.CurrentSprite

class InteractableDebugSystem :
    IteratingSystem(
        World.family {
            all(Interactable, CurrentSprite)
        },
    ),
    KoinComponent {
    private val shaperRenderer: ShapeRenderer by inject()

    override fun onTickEntity(entity: Entity) {
        val interactable = InteractableEntity(entity)
        val currentSprite = entity[CurrentSprite]

        shaperRenderer.use(ShapeRenderer.ShapeType.Line) {
            it.color = Color.RED.cpy()
            it.rect(
                interactable.position.x.m
                    .toPixels()
                    .value - currentSprite.sprite.originX,
                interactable.position.y.m
                    .toPixels()
                    .value - currentSprite.sprite.originY,
                48f,
                96f,
            )
        }

        world.Interactors.forEach {
            val interactor = Interactor(it)

            if (interactable.isInInteractionRangeOf(interactor)) {
                shaperRenderer.use(ShapeRenderer.ShapeType.Line) {
                    it.color = Color.GREEN.cpy()
                    it.rect(
                        interactable.position.x.m
                            .toPixels()
                            .value - currentSprite.sprite.originX,
                        interactable.position.y.m
                            .toPixels()
                            .value - currentSprite.sprite.originY,
                        48f,
                        96f,
                    )
                }
            }
        }
    }
}
