package me.panpf.recycler.sticky.sample.bean

class AppHeader(val title: String) {

    var expand: Boolean = false
        set(value) {
            field = value
            listenrs.forEach { it.onExpandChanged() }
        }

    val listenrs = mutableListOf<StatusListener>()

    var count = 0

    interface StatusListener {
        fun onExpandChanged()
    }
}