package pro.piechowski.highschoolstory

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.UniqueId
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import pro.piechowski.highschoolstory.character.player.PlayerCharacterTag
import pro.piechowski.highschoolstory.direction.Direction
import pro.piechowski.highschoolstory.direction.Direction4
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection4
import pro.piechowski.highschoolstory.physics.movement.facedirection.FaceDirection8

fun Json() =
    Json {
        serializersModule =
            SerializersModule {
                contextual(Vector2Serializer)

                polymorphic(Component::class) {
                    subclass(FaceDirection4::class, FaceDirection4.Serializer)
                    subclass(FaceDirection8::class, FaceDirection8.Serializer)
                }

                polymorphic(UniqueId::class) {
                    subclass(PlayerCharacterTag::class, PlayerCharacterTag.serializer())
                }
            }

        allowStructuredMapKeys = true
    }
