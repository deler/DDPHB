package my.ddx.ddphb.ui.widgets

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexboxLayout
import my.ddx.ddphb.R
import my.ddx.ddphb.utils.Listener2
import java.util.HashMap
import java.util.HashSet
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableMap
import kotlin.collections.Set
import kotlin.collections.set

/**
 * TagSelectorView
 * Created by deler on 22.03.17.
 */

class TagSelectorView : FlexboxLayout {


    private var listener: Listener2<Tag, Boolean>? = null
    private var tagTagViewMap: MutableMap<Tag, TagView> = HashMap()
    private var tags: List<Tag> = ArrayList()
    private val tagSelectedSet = HashSet<Tag>()


    val selectedTags: Set<Tag>
        get() = tagSelectedSet

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        alignContent = ALIGN_CONTENT_FLEX_START
        alignItems = ALIGN_ITEMS_FLEX_START
        flexWrap = FLEX_WRAP_WRAP
        justifyContent = JUSTIFY_CONTENT_FLEX_START
    }

    private fun addTagView(view: TagView) {
        val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val margin = context.resources.getDimensionPixelOffset(R.dimen.spacing_small)
        params.bottomMargin = margin
        params.topMargin = margin
        params.leftMargin = margin
        params.rightMargin = margin
        addView(view, params)
    }

    fun setTags(tags: List<Tag>) {
        this.tags = tags
        removeAllViews()
        tagTagViewMap = HashMap()
        for (tag in tags) {
            val tagView = createTagView(tag)
            tagTagViewMap[tag] = tagView
            addTagView(tagView)
        }
    }

    private fun createTagView(tag: Tag): TagView {
        val tagView = TagView(context)
        tagView.setText(tag.name?:"")
        tagView.setOnClickListener {
            listener?.invoke(tag, !isSelected(tag))
        }
        return tagView
    }

    private fun animatedSelectTag(tagView: TagView, selected: Boolean) {
        tagView.animate().rotationYBy(90f).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                tagView.setTextYRotation(180f)
                tagView.isSelected = selected
                tagView.animate().rotationYBy(90f).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        tagView.rotationY = 0f
                        tagView.setTextYRotation(0f)
                    }
                })
            }
        })
    }

    fun isSelected(tag: Tag): Boolean {
        return tagSelectedSet.contains(tag)
    }

    fun setListener(listener: Listener2<Tag, Boolean>?) {
        this.listener = listener
    }

    fun hideUnselectedTags(hide: Boolean) {
        for (tag in tags) {
            val tagView = tagTagViewMap[tag]
            tagView?.let {
                if (isSelected(tag)) {
                    it.visibility = View.VISIBLE
                } else {
                    it.visibility = if (hide) View.GONE else View.VISIBLE
                }
            }
        }
    }

    fun resetSelected(animated: Boolean) {
        if (animated) {
            for (tag in tagSelectedSet) {
                val tagView = tagTagViewMap[tag]
                if (tagView?.isSelected==true) {
                    animatedSelectTag(tagView, false)
                }
            }
            tagSelectedSet.clear()
        } else {
            for (tag in tagSelectedSet) {
                val tagView = tagTagViewMap[tag]
                if (tagView?.isSelected==true) {
                    tagView.isSelected = false
                }
            }
            tagSelectedSet.clear()
        }
    }

    fun setSelectedTag(tagId: String, selected: Boolean, animated: Boolean) {
        for (tag in tags) {
            if (tag.id.equals(tagId, true)) {
                setSelectedTag(tag, selected, animated)
                return
            }
        }
    }

    fun setSelectedTag(tag: Tag, selected: Boolean, animated: Boolean) {
        val changeStatus = tagSelectedSet.contains(tag) != selected
        if (selected) {
            tagSelectedSet.add(tag)
        } else {
            tagSelectedSet.remove(tag)
        }

        val tagView = tagTagViewMap[tag]
        tagView?.let {
            if (changeStatus) {
                if (animated) {
                    animatedSelectTag(it, isSelected(tag))
                } else {
                    it.isSelected = isSelected(tag)
                }
            }
        }
    }

    fun isSeletedAllExclude(tagAllId: String): Boolean {
        var tag: Tag? = null
        for (tag1 in tags) {
            if (tag1.id.equals(tagAllId, true)) {
                tag = tag1
                break
            }
        }

        if (tag == null) return false

        val tags = HashSet(tags)
        tags.removeAll(tagSelectedSet)
        return tags.size == 1 && tags.contains(tag)
    }


    class Tag(val id: String?, val name: CharSequence?) {
        override fun hashCode(): Int {
            var result = id?.hashCode() ?: 0
            result = 31 * result + (name?.hashCode() ?: 0)
            return result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false

            val tag = other as Tag?

            if (if (id != null) id != tag?.id else tag?.id != null) return false
            return if (name != null) name == tag?.name else tag?.name == null

        }
    }
}
