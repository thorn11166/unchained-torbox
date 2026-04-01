package com.thorn11166.unchainedtorbox.user.view

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.thorn11166.unchainedtorbox.R
import com.thorn11166.unchainedtorbox.base.UnchainedFragment
import com.thorn11166.unchainedtorbox.data.model.User
import com.thorn11166.unchainedtorbox.databinding.FragmentUserProfileBinding
import com.thorn11166.unchainedtorbox.settings.view.SettingsActivity
import com.thorn11166.unchainedtorbox.settings.view.SettingsFragment.Companion.KEY_REFERRAL_ASKED
import com.thorn11166.unchainedtorbox.settings.view.SettingsFragment.Companion.KEY_REFERRAL_USE
import com.thorn11166.unchainedtorbox.statemachine.authentication.FSMAuthenticationState
import com.thorn11166.unchainedtorbox.utilities.ACCOUNT_LINK
import com.thorn11166.unchainedtorbox.utilities.REFERRAL_LINK
import com.thorn11166.unchainedtorbox.utilities.extension.openExternalWebPage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

/** A simple [UnchainedFragment] subclass. Shows a user profile details. */
@AndroidEntryPoint
class UserProfileFragment : UnchainedFragment() {

    @Inject lateinit var preferences: SharedPreferences

    private var _binding: FragmentUserProfileBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val user: User? = activityViewModel.getCachedUser()
        if (user == null) {
            activityViewModel.fetchUser()
        } else {
            populateUserView(user)
        }
        lifecycleScope.launch {
            if (activityViewModel.isTokenPrivate()) {
                binding.tvLoginDescription.text = getString(R.string.login_type_private)
            } else {
                binding.tvLoginDescription.text = getString(R.string.login_type_open)
            }
        }

        activityViewModel.userLiveData.observe(viewLifecycleOwner) {
            populateUserView(it.peekContent())
            lifecycleScope.launch {
                if (activityViewModel.isTokenPrivate()) {
                    binding.tvLoginDescription.text = getString(R.string.login_type_private)
                } else {
                    binding.tvLoginDescription.text = getString(R.string.login_type_open)
                }
            }
        }

        binding.bAccount.setOnClickListener {
            // if we never asked, show a dialog
            if (!preferences.getBoolean(KEY_REFERRAL_ASKED, false)) {
                // set asked as true
                with(preferences.edit()) {
                    putBoolean(KEY_REFERRAL_ASKED, true)
                    apply()
                }

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.referral))
                    .setMessage(getString(R.string.referral_proposal))
                    .setNegativeButton(getString(R.string.decline)) { _, _ ->
                        with(preferences.edit()) {
                            putBoolean(KEY_REFERRAL_USE, false)
                            apply()
                        }
                        context?.openExternalWebPage(ACCOUNT_LINK)
                    }
                    .setPositiveButton(getString(R.string.accept)) { _, _ ->
                        with(preferences.edit()) {
                            putBoolean(KEY_REFERRAL_USE, true)
                            apply()
                        }
                        context?.openExternalWebPage(REFERRAL_LINK)
                    }
                    .show()
            } else {
                if (preferences.getBoolean(KEY_REFERRAL_USE, false))
                    context?.openExternalWebPage(REFERRAL_LINK)
                else context?.openExternalWebPage(ACCOUNT_LINK)
            }
        }

        activityViewModel.fsmAuthenticationState.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it.peekContent()) {
                    is FSMAuthenticationState.WaitingUserAction -> {
                        // an error occurred, check it and eventually go back to the start fragment
                        val action = UserProfileFragmentDirections.actionUserToStartFragment()
                        findNavController().navigate(action)
                    }

                    FSMAuthenticationState.StartNewLogin -> {
                        // the user reset the login, go to the auth fragment
                        val action =
                            UserProfileFragmentDirections.actionUserToAuthenticationFragment()
                        findNavController().navigate(action)
                    }

                    FSMAuthenticationState.AuthenticatedOpenToken,
                    FSMAuthenticationState.AuthenticatedPrivateToken,
                    FSMAuthenticationState.RefreshingOpenToken -> {
                        // managed by activity
                    }

                    FSMAuthenticationState.CheckCredentials -> {
                        // shouldn't matter
                    }

                    FSMAuthenticationState.Start,
                    FSMAuthenticationState.WaitingToken,
                    FSMAuthenticationState.WaitingUserConfirmation -> {
                        // shouldn't happen
                    }
                }
            }
        }

        binding.bSettings.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            activityViewModel.requireNotificationPermissions()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun populateUserView(user: User?) {
        user?.let {
            binding.tvName.text = it.username
            binding.tvMail.text = it.email
            // todo: check https://coil-kt.github.io/coil/image_loaders/#caching
            binding.ivProfilePic.load(it.avatar) { crossfade(true) }
            if (it.premium > 0) {
                binding.tvPremium.text = getString(R.string.premium)
            } else {
                binding.tvPremium.text = getString(R.string.not_premium)
            }
            binding.tvPremiumDays.text =
                getString(R.string.premium_days_format, it.premium / 60 / 60 / 24)
            binding.tvPoints.text = getString(R.string.premium_points_format, it.points)
            binding.pointsBar.setProgressCompat(it.points, true)
        }
    }
}
