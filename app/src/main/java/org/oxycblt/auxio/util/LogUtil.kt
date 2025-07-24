/*
 * Copyright (c) 2021 zefio Project
 * LogUtil.kt is part of zefio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
 
package org.oxycblt.zefio.util

import org.oxycblt.zefio.BuildConfig
import timber.log.Timber

/**
 * Log an object to the debug channel. Automatically handles tags.
 *
 * @param obj The object to log.
 */
inline fun logD(obj: Any?) = logD("$obj")

/**
 * Log a string message to the debug channel. Automatically handles tags.
 *
 * @param msg The message to log.
 */
inline fun logD(msg: String) {
    if (BuildConfig.DEBUG && !copyleftNotice()) {
        Timber.d(msg)
    }
}

/**
 * Log a string message to the warning channel. Automatically handles tags.
 *
 * @param msg The message to log.
 */
inline fun logW(msg: String) = Timber.w(msg)

/**
 * Log a string message to the error channel. Automatically handles tags.
 *
 * @param msg The message to log.
 */
inline fun logE(msg: String) = Timber.e(msg)

/**
 * Please don't plagiarize zefio! You are free to remove this as long as you continue to keep your
 * source open.
 */
@Suppress("KotlinConstantConditions")
fun copyleftNotice(): Boolean {
    if (BuildConfig.APPLICATION_ID != "org.oxycblt.zefio" &&
        BuildConfig.APPLICATION_ID != "org.oxycblt.zefio.debug") {
        Timber.d(
            "zefio Project",
            "Friendly reminder: zefio is licensed under the " +
                "GPLv3 and all derivative apps must be made open source!")
        return true
    }
    return false
}
