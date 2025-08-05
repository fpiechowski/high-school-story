package pro.piechowski.highschoolstory.character.player

import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.EntityTag
import kotlinx.serialization.Serializable
import ktx.assets.async.AssetStorage
import pro.piechowski.highschoolstory.asset.AssetIdentifiers
import pro.piechowski.highschoolstory.character.Character
import pro.piechowski.highschoolstory.ecs.Archetype
import pro.piechowski.highschoolstory.gdx.PhysicsWorld
import pro.piechowski.highschoolstory.physics.movement.Speed
import pro.piechowski.highschoolstory.physics.movement.input.MovementInput

@Serializable
data object PlayerCharacterTag : EntityTag()
