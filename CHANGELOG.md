## v1.0.2

fix: Fix the bug that StickyItemPainter still uses the outdated position to get the itemType when the adapter's itemCount changes from greater than 0 to 0, causing a crash

## v1.0.1

fix: Fix the bug that the parameter of `invisibleOriginItemWhenStickyItemShowing` is invalid in
horizontal layout

## v1.0.0-new

* Rewritten in kotlin
* maven group changed to `io.github.panpnf.stickyitemdecoration`
* Added support for [AssemblyAdapter](https://github.com/panpf/assembly-adapter) 4
* Support horizontal layout

## v1.0.0

* Initial release