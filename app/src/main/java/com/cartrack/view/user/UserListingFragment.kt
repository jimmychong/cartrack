package com.cartrack.view.user

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.cartrack.R
import com.cartrack.adapter.user.UserAdapter
import com.cartrack.base.BaseActivity
import com.cartrack.base.MVVMFragment
import com.cartrack.databinding.FragmentUserListingBinding
import com.cartrack.model.user.UserInfo
import com.cartrack.viewmodel.user.UserListingViewModel


class UserListingFragment : MVVMFragment<UserListingViewModel, FragmentUserListingBinding>() {
    private var userAdapter: UserAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAdapter = UserAdapter(object : UserAdapter.OnUserClickListener {
            override fun userClicked(user: UserInfo) {
                baseActivity.getMainFragmentContainer()?.let {
                    baseActivity.pushFragment(
                        UserDetailFragment.newInstance(user),
                        it,
                        isAddToBackStack = true
                    )
                }
            }
        })

        binding.rvUsers.apply {
            adapter = userAdapter
            itemAnimator = null
            addItemDecoration(
                DividerItemDecoration(
                    requireContext().applicationContext,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        viewModel.userStatusList.observe(viewLifecycleOwner) {
            userAdapter?.setUserList(it)
        }

        (activity as UserActivity).showBackButton(false)
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_user_listing
    }

    override fun getViewModelInstance(): UserListingViewModel {
        return UserListingViewModel()
    }

    override fun setBindingData() {

    }
}