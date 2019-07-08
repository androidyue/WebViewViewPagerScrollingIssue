package com.example.secoo.webviewandviewpagerscrollsample

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.GridView
import android.widget.HorizontalScrollView
import android.widget.ScrollView

class MyWebView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    private var scrollParent: ViewGroup? = null

    private fun dumpMessage(message: String) {
        Log.i(LOGTAG, "dumpMessage message=$message")
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (scrollParent == null){
                scrollParent = findViewParentIfNeeds(this, 10)
            }
            scrollParent?.requestDisallowInterceptTouchEvent(true)
        }
        return super.onTouchEvent(event)
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        dumpMessage("onOverScrolled scrollX=" + scrollX + ";scrollY=" + scrollY
                + ";clampedX=" + clampedX + ";clampedY=" + clampedY)
        if (clampedX) {
            if (scrollParent == null){
                scrollParent = findViewParentIfNeeds(this, 10)
            }
            scrollParent?.requestDisallowInterceptTouchEvent(false)
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
    }

    internal fun findViewParentIfNeeds(tag: View, depth: Int): ViewGroup? {
        if (depth < 0) {
            return null
        }
        val parent = tag.parent?: return null
        if (parent is ViewGroup){
            if (canScrollHorizontal(parent)){
                return parent
            }else {
                //增加最大地柜深度判断，防止出现ANR或者异常
                findViewParentIfNeeds(parent, depth-1)
            }
        }
        return null
    }

    /**
     * [view] 是否可以横向滑动
     */
    private fun canScrollHorizontal(view: View) = view.canScrollHorizontally(100) || view.canScrollHorizontally(-100)

    companion object {
        private val LOGTAG = "MyWebView"
    }

}
