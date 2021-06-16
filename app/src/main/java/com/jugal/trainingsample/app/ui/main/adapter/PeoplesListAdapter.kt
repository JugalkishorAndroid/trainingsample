package com.jugal.trainingsample.app.ui.main.adapter

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jugal.trainingsample.app.ui.main.viewholder.PeopleItemViewHolder
import com.jugal.trainingsample.data.model.PeopleRemote
import kotlin.math.roundToInt

class PeoplesListAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PeopleItemViewHolder>() {

    var peoplesList = mutableListOf<PeopleRemote>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleItemViewHolder {
        return PeopleItemViewHolder.newInstance(parent)
    }

    override fun getItemCount(): Int = peoplesList.size

    override fun onBindViewHolder(holder: PeopleItemViewHolder, position: Int) {
        holder.bind(peoplesList[position], onItemClickListener::onClick)
    }

    class PeopleListDecoration : RecyclerView.ItemDecoration() {
        private fun View.dp(value: Int) =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value.toFloat(),
                this.resources.displayMetrics
            ).roundToInt()

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            with(parent) {
                outRect.set(
                    dp(16),
                    dp(4),
                    dp(16),
                    dp(4)
                )
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(view: View, people: PeopleRemote)
    }

}