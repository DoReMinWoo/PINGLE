package org.sopt.pingle.util.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import org.sopt.pingle.R
import org.sopt.pingle.databinding.CardPingleBinding
import org.sopt.pingle.domain.model.PingleEntity
import org.sopt.pingle.presentation.mapper.isCompleted
import org.sopt.pingle.presentation.type.CategoryType
import org.sopt.pingle.presentation.ui.participant.ParticipantActivity
import org.sopt.pingle.util.convertToCalenderDetailWithNewLine
import org.sopt.pingle.util.view.colorOf
import org.sopt.pingle.util.view.stringOf

@SuppressLint("CustomViewStyleable")
class PingleCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: CardPingleBinding
    private var onChatButtonClick: () -> Unit = {}
    private var onParticipateButtonClick: (Long?) -> Unit = {}
    private var _pinId: Long? = null
    val pinId get() = _pinId

    init {
        binding = CardPingleBinding.inflate(LayoutInflater.from(context), this, true)

        addListeners()
    }

    private fun addListeners() {
        binding.btnCardBottomMapChat.setOnClickListener {
            onChatButtonClick()
        }

        binding.btnCardBottomMapParticipate.setOnClickListener {
            onParticipateButtonClick(pinId)
        }
    }

    fun initLayout(pingleEntity: PingleEntity) {
        val category: CategoryType = CategoryType.fromString(pingleEntity.category)

        with(binding) {
            badgeCardTopInfo.setBadgeCategoryType(category)
            tvCardTopInfoName.text = pingleEntity.name
            tvCardTopInfoName.setTextColor(colorOf(category.textColor))
            tvCardTopInfoOwnerName.text = pingleEntity.ownerName
            tvCardBottomCalenderDetail.text = convertToCalenderDetailWithNewLine(
                date = pingleEntity.date,
                startAt = pingleEntity.startAt,
                endAt = pingleEntity.endAt
            )
            tvCardBottomMapDetail.text = pingleEntity.location
            btnCardBottomMapChat.isEnabled = pingleEntity.isParticipating
            btnCardBottomMapParticipate.isEnabled = when {
                pingleEntity.isOwner -> true
                else -> pingleEntity.isParticipating || !pingleEntity.isCompleted()
            }

            btnCardBottomMapParticipate.text =
                when {
                    pingleEntity.isOwner -> stringOf(R.string.map_card_delete)
                    pingleEntity.isParticipating -> stringOf(R.string.map_card_cancel)
                    else -> stringOf(R.string.map_card_participate)
                }

            if (pingleEntity.isCompleted()) {
                tvCardTopInfoParticipationStatusSlash.visibility = View.INVISIBLE
                tvCardTopInfoParticipationStatusCurrentParticipants.visibility = View.INVISIBLE
                tvCardTopInfoParticipationStatusMaxParticipants.visibility = View.INVISIBLE
                tvCardTopInfoParticipationCompleted.visibility = View.VISIBLE
            } else {
                tvCardTopInfoParticipationStatusCurrentParticipants.text =
                    pingleEntity.curParticipants.toString()
                tvCardTopInfoParticipationStatusCurrentParticipants.setTextColor(colorOf(category.textColor))
                tvCardTopInfoParticipationStatusSlash.visibility = View.VISIBLE
                tvCardTopInfoParticipationStatusCurrentParticipants.visibility = View.VISIBLE
                tvCardTopInfoParticipationStatusMaxParticipants.visibility = View.VISIBLE
                tvCardTopInfoParticipationCompleted.visibility = View.INVISIBLE
                tvCardTopInfoParticipationStatusMaxParticipants.text =
                    pingleEntity.maxParticipants.toString()
            }

            layoutCardTopParticipationStatus.setOnClickListener {
                Intent(context, ParticipantActivity::class.java).apply {
                    putExtra(MEETING_ID, pingleEntity.id)
                    context.startActivity(this)
                }
            }
        }
    }

    fun setPinId(pinId: Long) {
        _pinId = pinId
    }

    fun setOnChatButtonClick(chatButtonClickListener: () -> Unit) {
        onChatButtonClick = chatButtonClickListener
    }

    fun setOnParticipateButtonClick(participateButtonClickListener: (Long?) -> Unit) {
        onParticipateButtonClick = participateButtonClickListener
    }

    companion object {
        const val MEETING_ID = "meetingId"
    }
}
