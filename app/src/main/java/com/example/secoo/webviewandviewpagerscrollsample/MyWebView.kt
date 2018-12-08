package com.example.secoo.webviewandviewpagerscrollsample

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.GridView
import android.widget.HorizontalScrollView
import android.widget.ScrollView

class MyWebView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    private fun dumpMessage(message: String) {
        Log.i(LOGTAG, "dumpMessage message=$message")
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val viewParent = findViewParentIfNeeds(this)
            viewParent?.requestDisallowInterceptTouchEvent(true)
        }
        return super.onTouchEvent(event)
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        dumpMessage("onOverScrolled scrollX=" + scrollX + ";scrollY=" + scrollY
                + ";clampedX=" + clampedX + ";clampedY=" + clampedY)
        if (clampedX) {
            val viewParent = findViewParentIfNeeds(this)
            viewParent?.requestDisallowInterceptTouchEvent(false)
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
    }

    internal fun findViewParentIfNeeds(tag: View): ViewParent? {
        val parent = tag.parent
        if (parent == null) {
            return parent
        }
        return if (parent is ViewPager ||
                parent is AbsListView ||
                parent is ScrollView ||
                parent is HorizontalScrollView ||
                parent is GridView) {
            parent
        } else {
            if (parent is View) {
                findViewParentIfNeeds(parent as View)
            } else {
                parent
            }
        }
    }

    companion object {
        private val LOGTAG = "MyWebView"
    }

}
