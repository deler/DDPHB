package my.ddx.ddphb.ui.widgets

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_tag.view.*
import my.ddx.ddphb.R

class TagView : LinearLayout {

    constructor(context: Context) : super(context) {
        init(null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr, 0)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs, defStyleAttr, defStyleRes)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        setBackgroundResource(R.drawable.selector_tag_default)
        gravity = Gravity.CENTER
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.view_tag, this, true)
    }

    fun setText(text: CharSequence) {
        textLabel.text = text
    }

    fun setTextYRotation(a: Float) {
        textLabel.rotationY = a
    }
}
