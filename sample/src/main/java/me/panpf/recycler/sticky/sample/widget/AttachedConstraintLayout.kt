package me.panpf.recycler.sticky.sample.widget

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet

class AttachedConstraintLayout : ConstraintLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var onAttachedListener: OnAttachedListener? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onAttachedListener?.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        onAttachedListener?.onDetachedFromWindow()
        super.onDetachedFromWindow()
    }

    interface OnAttachedListener {
        fun onAttachedToWindow()
        fun onDetachedFromWindow()
    }
}
