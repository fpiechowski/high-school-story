package pro.piechowski.kge.story

data class Story(
    val beats: List<Beat>,
) {
    interface Beat {
        fun isPlayable(): Boolean

        fun play()
    }
}
