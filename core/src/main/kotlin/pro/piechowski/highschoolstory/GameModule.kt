package pro.piechowski.highschoolstory

import com.github.quillraven.fleks.World
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import pro.piechowski.highschoolstory.dialogue.DialogueModule
import pro.piechowski.highschoolstory.ecs.invoke
import pro.piechowski.highschoolstory.input.InputProcessorMultiplexer
import pro.piechowski.highschoolstory.interaction.InteractionModule
import pro.piechowski.highschoolstory.physics.PhysicsModule
import pro.piechowski.highschoolstory.physics.movement.MovementModule
import pro.piechowski.highschoolstory.rendering.RenderingModule
import pro.piechowski.highschoolstory.ui.UserInterfaceModule

val gameModuleQualifier = StringQualifier("gameModule")

fun gameModule() =
    module {
        includes(PhysicsModule)
        includes(InteractionModule)
        includes(MovementModule)
        includes(DialogueModule)
        includes(UserInterfaceModule)
        includes(RenderingModule)

        single { GameScreen() }

        single { InputProcessorMultiplexer() }

        single<World> { World() }
    }
