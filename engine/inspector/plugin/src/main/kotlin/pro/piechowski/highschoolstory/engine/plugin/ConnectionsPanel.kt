package pro.piechowski.highschoolstory.engine.plugin

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.treeStructure.SimpleTree
import javax.swing.JComponent
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class ConnectionsPanel(
    private val project: Project,
) {
    val component: JComponent

    private val rootNode = DefaultMutableTreeNode("Connections")
    private val treeModel = DefaultTreeModel(rootNode)
    private val tree = SimpleTree(treeModel)

    init {
        rootNode.add(DefaultMutableTreeNode("Test"))
    }

    init {
        tree.emptyText.text = "No connections yet"

        component =
            panel {
                row {
                    cell(buildToolbar()).apply {
                        align(AlignX.FILL)
                    }
                }
                row {
                    scrollCell(tree)
                        .align(Align.FILL)
                }
            }
    }

    private fun buildToolbar(): JComponent {
        val actionGroup =
            DefaultActionGroup().apply {
                add(CreateConnectionAction())
                add(Separator.getInstance())
                add(RefreshConnectionsAction())
            }

        return ActionManager
            .getInstance()
            .createActionToolbar("ConnectionsToolbar", actionGroup, true)
            .apply {
                targetComponent = tree
            }.component
    }
}
