package com.example.sg772.textonimage.Utils

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import java.lang.reflect.Field
import java.time.Duration

class NonSwipeableViewPager : ViewPager {
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    constructor(context: Context) : super(context) {
        setMyScroller()
    }

    private fun setMyScroller() {
        try {
            var viewpager = ViewPager::class.java
            var scroller: Field = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            scroller.set(this, MySqroller(context))
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }


    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        setMyScroller()
    }
}

class MySqroller : Scroller {
    constructor(context: Context) : super(context, DecelerateInterpolator()) {
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, 400)
    }
}
