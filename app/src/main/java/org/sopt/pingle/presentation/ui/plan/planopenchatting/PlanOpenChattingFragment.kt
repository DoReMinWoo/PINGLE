package org.sopt.pingle.presentation.ui.plan.planopenchatting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.pingle.R
import org.sopt.pingle.databinding.FragmentPlanOpenChattingBinding
import org.sopt.pingle.presentation.ui.plan.PlanViewModel
import org.sopt.pingle.util.base.BindingFragment
import org.sopt.pingle.util.context.hideKeyboard

@AndroidEntryPoint
class PlanOpenChattingFragment :
    BindingFragment<FragmentPlanOpenChattingBinding>(R.layout.fragment_plan_open_chatting) {
    private val planViewModel: PlanViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = planViewModel

        addListeners()
    }

    private fun addListeners() {
        binding.root.setOnClickListener {
            requireActivity().hideKeyboard(it)
        }
    }
}
