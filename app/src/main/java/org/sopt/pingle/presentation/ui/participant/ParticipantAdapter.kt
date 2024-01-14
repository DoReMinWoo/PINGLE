package org.sopt.pingle.presentation.ui.participant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import org.sopt.pingle.databinding.ItemParticipantBinding
import org.sopt.pingle.domain.model.ParticipantEntity
import org.sopt.pingle.util.view.ItemDiffCallback

class ParticipantAdapter :
    ListAdapter<ParticipantEntity, ParticipantViewHolder>(
        ItemDiffCallback<ParticipantEntity>(
            onItemsTheSame = { old, new -> old.participant == new.participant },
            onContentsTheSame = { old, new -> old == new },
        ),
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder =
        ParticipantViewHolder(
            ItemParticipantBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}
