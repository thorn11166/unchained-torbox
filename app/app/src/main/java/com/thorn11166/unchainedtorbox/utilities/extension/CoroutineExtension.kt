package com.thorn11166.unchainedtorbox.utilities.extension

import kotlinx.coroutines.Job

fun Job.cancelIfActive() {
    if (isActive) cancel()
}
