package com.jugal.trainingsample.app.ui.main.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jugal.trainingsample.R
import com.jugal.trainingsample.data.model.PeopleRemote
import kotlinx.android.synthetic.main.holder_people_item.view.*

class PeopleItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val placeholder = with(itemView) {
        ContextCompat.getDrawable(
            this.context,
            R.drawable.ph_content
        )?.apply {
            this.alpha = 80
            this.setTint(context.getColor(R.color.color_on_surface))
        }
    }

    fun bind(people: PeopleRemote, listener: (View, PeopleRemote) -> Unit) {
        itemView.setOnClickListener { listener(it, people) }
        itemView.holder_people_item_container.apply {
            this.holder_people_item_name.background = null
            this.holder_people_item_user_id.text = people.id.toString()
            this.holder_people_item_name.text = people.getFullName()
            this.holder_people_item_email.text = people.email
        }
    }

    companion object {
        fun newInstance(parent: ViewGroup) =
            PeopleItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.holder_people_item,
                    parent,
                    false
                )
            )
    }

}