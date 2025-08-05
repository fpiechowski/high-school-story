package pro.piechowski.highschoolstory

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Snapshot
import pro.piechowski.highschoolstory.place.Place

class GameState(
    val currentPlace: Place,
    val worldSnapshot: Map<Entity, Snapshot>,
)
