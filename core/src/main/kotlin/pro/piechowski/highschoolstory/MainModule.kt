package pro.piechowski.highschoolstory

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.UniqueId
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import ktx.assets.async.AssetStorage
import org.koin.dsl.module
import pro.piechowski.highschoolstory.character.PlayerCharacter
import pro.piechowski.highschoolstory.movement.facedirection.FaceDirection

val mainModule =
    module {
        single { AssetStorage() }
        single { SpriteBatch() }
        single<InputProcessor> { InputAdapter() }
        single { InputMultiplexer(get<InputProcessor>()) }
        single {
            Json {
                serializersModule =
                    SerializersModule {
                        contextual(Vector2Serializer)

                        polymorphic(Component::class) {
                            subclass(FaceDirection::class, FaceDirection.serializer())
                        }

                        polymorphic(UniqueId::class) {
                            subclass(PlayerCharacter::class, PlayerCharacter.serializer())
                        }
                    }

                allowStructuredMapKeys = true
            }
        }
    }
