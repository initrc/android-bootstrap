package io.github.initrc.bootstrap.view

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import io.github.initrc.bootstrap.R
import util.AnimationUtils
import util.setTextSizeFromDimen

/**
 * A view that slides in and let you select.
 */
class SlideSelectView : RelativeLayout {
    val items = ArrayList<TextView>()
    var onClickListeners = ArrayList<OnClickListener>()
    private var onDismiss: (() -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        setOnClickListener {
            hide()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        for (i in items.indices) {
            items[i].setOnClickListener {
                onClickListeners[i].onClick(items[i])
                updateItems(i)
                hide()
            }
        }
    }

    fun show() {
        visibility = View.VISIBLE
        AnimationUtils.slideFromRight(items)
    }

    fun hide() {
        AnimationUtils.slideToRight(items) { visibility = View.GONE }
        onDismiss?.invoke()
    }

    private fun updateItems(selectedIndex: Int) {
        for (i in items.indices) {
            TextDecorator.decorate(context, items[i], i == selectedIndex)
        }
    }

    class Builder(_context: Context) {
        val context: Context = _context
        val textList = ArrayList<CharSequence>()
        var selectedIndex = 0
        val onClickListeners = ArrayList<OnClickListener>()
        var onDismiss: (() -> Unit)? = null

        fun addItem(text: CharSequence, listener: OnClickListener): Builder {
            textList.add(text)
            onClickListeners.add(listener)
            return this
        }

        fun setSelectedIndex(index: Int): Builder {
            selectedIndex = index
            return this
        }

        fun setOnDismiss(onDismiss: () -> Unit): Builder {
            this.onDismiss = onDismiss
            return this
        }

        fun build(): SlideSelectView {
            val rootView = SlideSelectView(context)
            val rootLp = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            rootLp.addRule(ALIGN_PARENT_RIGHT)
            rootLp.addRule(CENTER_VERTICAL)

            val margin = context.resources.getDimension(R.dimen.m2).toInt()
            val groupLp = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            groupLp.setMargins(0, margin, 0, margin)

            val viewGroup = LinearLayout(context)
            viewGroup.orientation = LinearLayout.VERTICAL
            for (i in textList.indices) {
                val view = buildItem(textList[i], onClickListeners[i])
                TextDecorator.decorate(context, view, i == selectedIndex)
                rootView.items.add(view)
                viewGroup.addView(view, groupLp)
            }
            rootView.addView(viewGroup, rootLp)
            rootView.onDismiss = onDismiss
            rootView.onClickListeners = ArrayList(onClickListeners)
            return rootView
        }

        private fun buildItem(text: CharSequence, listener: OnClickListener): TextView {
            val view = TextView(context)
            val padding = context.resources.getDimension(R.dimen.margin).toInt()
            view.setPadding(padding * 4, padding, padding, padding)
            view.setTextSizeFromDimen(R.dimen.t4)
            view.setTypeface(null, Typeface.BOLD)
            view.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            view.text = text
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.elevation = context.resources.getDimension(R.dimen.elevation)
            }
            return view
        }
    }

    object TextDecorator {
        fun decorate(context: Context, view: TextView, selected: Boolean) {
            view.setTextColor(ContextCompat.getColor(context,
                    if (selected) R.color.white else R.color.text))
            view.background = ContextCompat.getDrawable(context,
                    if (selected) R.drawable.drag_select_view_item_selected_bg
                    else R.drawable.drag_select_view_item_bg)
        }
    }
}
