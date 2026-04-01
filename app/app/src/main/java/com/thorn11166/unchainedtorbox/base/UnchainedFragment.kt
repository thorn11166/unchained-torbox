package com.thorn11166.unchainedtorbox.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.thorn11166.unchainedtorbox.start.viewmodel.MainActivityViewModel

/** Base [Fragment] class, giving simple access to the activity ViewModel to its subclasses */
abstract class UnchainedFragment : Fragment() {

    // activity viewModel. To be used for alerting of expired token or missing network
    val activityViewModel: MainActivityViewModel by activityViewModels()
}
