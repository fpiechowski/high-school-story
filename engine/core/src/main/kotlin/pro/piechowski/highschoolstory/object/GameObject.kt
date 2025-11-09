package pro.piechowski.highschoolstory.`object`

import com.github.quillraven.fleks.Entity

/**
 * Represent an object known to the game, either ECS entity components aggregate @EntityGameObject or any object eg. Koin singleton.
 */
interface GameObject

interface EntityGameObject {
    val entity: Entity
}
