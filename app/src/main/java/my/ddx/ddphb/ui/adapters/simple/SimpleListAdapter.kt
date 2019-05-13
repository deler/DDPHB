package my.ddx.ddphb.ui.adapters.simple

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

class SimpleListAdapter(private val mModels: List<CellModel>) : RecyclerView.Adapter<CellViewHolder>() {

    private val cellListener = CellListener()
    private val onClickSubject = PublishSubject.create<CellModel>()

    val clickFlowable: Flowable<CellModel>
        get() = onClickSubject.toFlowable(BackpressureStrategy.DROP)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(CellViewHolder.layoutId, parent, false)
        return CellViewHolder(view, cellListener)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val model = mModels[position]
        holder.setup(model)
    }

    override fun getItemCount(): Int {
        return mModels.size
    }

    private inner class CellListener : CellViewHolder.CellListener {
        override fun onClick(model: CellModel) {
            onClickSubject.onNext(model)
        }
    }
}
