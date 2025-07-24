/*
 * Copyright (c) 2023 zefio Project
 * ErrorDetailsDialog.kt is part of zefio.
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
 
package org.oxycblt.zefio.home

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.navArgs
import org.oxycblt.zefio.R
import org.oxycblt.zefio.databinding.DialogErrorDetailsBinding
import org.oxycblt.zefio.ui.ViewBindingMaterialDialogFragment
import org.oxycblt.zefio.util.getSystemServiceCompat
import org.oxycblt.zefio.util.openInBrowser
import org.oxycblt.zefio.util.showToast

/**
 * A dialog that shows a stack trace for a music loading error.
 *
 * @author Alexander Capehart (OxygenCobalt)
 *
 * TODO: Extend to other errors
 */
class ErrorDetailsDialog : ViewBindingMaterialDialogFragment<DialogErrorDetailsBinding>() {
    private val args: ErrorDetailsDialogArgs by navArgs()
    private var clipboardManager: ClipboardManager? = null

    override fun onConfigDialog(builder: AlertDialog.Builder) {
        builder
            .setTitle(R.string.lbl_error_info)
            .setPositiveButton(R.string.lbl_report) { _, _ ->
                requireContext().openInBrowser(LINK_ISSUES)
            }
            .setNegativeButton(R.string.lbl_cancel, null)
    }

    override fun onCreateBinding(inflater: LayoutInflater) =
        DialogErrorDetailsBinding.inflate(inflater)

    override fun onBindingCreated(binding: DialogErrorDetailsBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)

        clipboardManager = requireContext().getSystemServiceCompat(ClipboardManager::class)

        // --- UI SETUP ---
        binding.errorStackTrace.text = args.error.stackTraceToString().trimEnd('\n')
        binding.errorCopy.setOnClickListener { copyStackTrace() }
    }

    override fun onDestroyBinding(binding: DialogErrorDetailsBinding) {
        super.onDestroyBinding(binding)
        clipboardManager = null
    }

    private fun copyStackTrace() {
        requireNotNull(clipboardManager) { "Clipboard was unavailable" }
            .setPrimaryClip(
                ClipData.newPlainText("Exception Stack Trace", args.error.stackTraceToString()))
        // A copy notice is shown by the system from Android 13 onwards
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            requireContext().showToast(R.string.lbl_copied)
        }
    }

    private companion object {
        /** The URL to the bug report issue form */
        const val LINK_ISSUES =
            "https://github.com/Suraj08832/zefio/issues/new" +
                "?assignees=OxygenCobalt&labels=bug&projects=&template=bug-crash-report.yml"
    }
}
