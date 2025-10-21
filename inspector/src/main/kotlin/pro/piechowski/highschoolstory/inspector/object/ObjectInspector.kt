package pro.piechowski.highschoolstory.inspector.`object`

class ObjectInspector(
    initialObject: Any,
) {
    private val model = ObjectInspectorModel(initialObject)
    private val viewModel = ObjectInspectorViewModel(model)
    private val view = ObjectInspectorView(viewModel)

    companion object {
        private var instance: ObjectInspector? = null

        fun pushObject(`object`: Any) {
            instance?.model?.pushObject(`object`) ?: ObjectInspector(`object`).also { instance = it }
        }
    }
}
