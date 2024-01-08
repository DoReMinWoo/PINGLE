package org.sopt.pingle.presentation.ui.main.plan

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import org.sopt.pingle.R
import org.sopt.pingle.databinding.FragmentPlanCategoryBinding
import org.sopt.pingle.presentation.type.CategoryType
import org.sopt.pingle.util.base.BindingFragment
import timber.log.Timber

class PlanCategoryFragment :
    BindingFragment<FragmentPlanCategoryBinding>(R.layout.fragment_plan_category) {
    private val viewModel by viewModels<PlanViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.planViewModel = viewModel

        initLayout()
        addListeners()
    }

    private fun initLayout() {
    }

    private fun addListeners() {
        with(binding) {
            includePlanCategoryTypePlay.root.setOnClickListener {
                viewModel.setSelectedCategory(CategoryType.PLAY)
            }
            includePlanCategoryTypeStudy.root.setOnClickListener {
                viewModel.setSelectedCategory(CategoryType.STUDY)
            }
            includePlanCategoryTypeMulti.root.setOnClickListener {
                viewModel.setSelectedCategory(CategoryType.MULTI)
            }
            includePlanCategoryTypeOthers.root.setOnClickListener {
                viewModel.setSelectedCategory(CategoryType.OTHERS)
            }
        }
    }
}
