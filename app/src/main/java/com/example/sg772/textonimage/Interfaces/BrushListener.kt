package com.example.sg772.textonimage.Interfaces

interface BrushListener {
    fun brushSizeChangeListener(size: Float)
    fun brushOpacityChangeListener(opacity: Int)
    fun brushColorChangeListener(color: Int)
    fun brushStateChangeListener(isEraser: Boolean)
}