package my.ddx.ddphb.ui.adapters.simple

import android.text.TextUtils
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_simple_list.view.*
import my.ddx.ddphb.R

/**
 * CellViewHolder for [SimpleListAdapter]
 */

class CellViewHolder(itemView: View, private val listener: CellListener?) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        var layoutId = R.layout.cell_simple_list
    }

    private var model: CellModel? = null

    init {
        itemView.setOnClickListener {
            model?.let {
                listener?.onClick(it)
            }
        }
    }

    fun setup(model: CellModel) {
        this.model = model

        itemView.textTitle.text = model.title

        if (model.drawable == null) {
            itemView.imageIcon.visibility = View.GONE
        } else {
            itemView.imageIcon.visibility = View.VISIBLE
            itemView.imageIcon.setImageResource(model.drawable)
        }

        if (TextUtils.isEmpty(model.description)) {
            itemView.textDescription.visibility = View.GONE
        } else {
            itemView.textDescription.visibility = View.VISIBLE
            itemView.textDescription.text = model.description
        }
    }

    interface CellListener {
        fun onClick(model: CellModel)
    }
}
