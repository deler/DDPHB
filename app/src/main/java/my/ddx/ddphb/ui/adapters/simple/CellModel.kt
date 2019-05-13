package my.ddx.ddphb.ui.adapters.simple

/**
 * CellModel for [SimpleListAdapter]
 */

class CellModel(val id: String, val title: CharSequence, val description: CharSequence?, val drawable: Int?) {

    override fun toString(): String {
        return "CellModel{" +
                "id='" + id + '\''.toString() +
                ", mTitle=" + title +
                ", mDescription=" + description +
                '}'.toString()
    }
}
