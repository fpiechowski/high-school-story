package pro.piechowski.highschoolstory.engine.plugin

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class CreateConnectionAction : AnAction(
    "New Connection",
    "Create a new server connection",
    AllIcons.General.Add
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        CreateConnectionDialog(project).show()
    }
}

class RefreshConnectionsAction : AnAction(
    "Refresh",
    "Reload connections list",
    AllIcons.Actions.Refresh
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        // your refresh code
    }
}
