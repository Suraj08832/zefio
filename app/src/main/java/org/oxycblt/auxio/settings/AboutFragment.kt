/*
 * Copyright (c) 2021 zefio Project
 * AboutFragment.kt is part of zefio.
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
 
package org.oxycblt.zefio.settings

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import org.oxycblt.zefio.BuildConfig
import org.oxycblt.zefio.R
import org.oxycblt.zefio.databinding.FragmentAboutBinding
import org.oxycblt.zefio.music.MusicViewModel
import org.oxycblt.zefio.playback.formatDurationMs
import org.oxycblt.zefio.ui.ViewBindingFragment
import org.oxycblt.zefio.util.collectImmediately
import org.oxycblt.zefio.util.openInBrowser
import org.oxycblt.zefio.util.systemBarInsetsCompat

/**
 * A [ViewBindingFragment] that displays information about the app and the current music library.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@AndroidEntryPoint
class AboutFragment : ViewBindingFragment<FragmentAboutBinding>() {
    private val musicModel: MusicViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateBinding(inflater: LayoutInflater) = FragmentAboutBinding.inflate(inflater)

    override fun onBindingCreated(binding: FragmentAboutBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)

        // --- UI SETUP ---
        binding.aboutToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.aboutContents.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(bottom = insets.systemBarInsetsCompat.bottom)
            insets
        }

        binding.aboutVersion.text = BuildConfig.VERSION_NAME
        binding.aboutCode.setOnClickListener { requireContext().openInBrowser(LINK_SOURCE) }
        binding.aboutWiki.setOnClickListener { requireContext().openInBrowser(LINK_WIKI) }
        binding.aboutLicenses.setOnClickListener { requireContext().openInBrowser(LINK_LICENSES) }
        binding.aboutProfile.setOnClickListener { requireContext().openInBrowser(LINK_PROFILE) }
        binding.aboutDonate.setOnClickListener { requireContext().openInBrowser(LINK_DONATE) }
        binding.aboutSupportersPromo.setOnClickListener {
            requireContext().openInBrowser(LINK_DONATE)
        }

        // VIEWMODEL SETUP
        collectImmediately(musicModel.statistics, ::updateStatistics)
    }

    private fun updateStatistics(statistics: MusicViewModel.Statistics?) {
        val binding = requireBinding()
        binding.aboutSongCount.text = getString(R.string.fmt_lib_song_count, statistics?.songs ?: 0)
        requireBinding().aboutAlbumCount.text =
            getString(R.string.fmt_lib_album_count, statistics?.albums ?: 0)
        requireBinding().aboutArtistCount.text =
            getString(R.string.fmt_lib_artist_count, statistics?.artists ?: 0)
        requireBinding().aboutGenreCount.text =
            getString(R.string.fmt_lib_genre_count, statistics?.genres ?: 0)
        binding.aboutTotalDuration.text =
            getString(
                R.string.fmt_lib_total_duration,
                (statistics?.durationMs ?: 0).formatDurationMs(false))
    }

    private companion object {
        const val LINK_SOURCE = "https://github.com/Suraj08832/zefio"
        const val LINK_WIKI = "$LINK_SOURCE/wiki"
        const val LINK_LICENSES = "$LINK_WIKI/Licenses"
        const val LINK_PROFILE = "https://github.com/Suraj08832"
        const val LINK_DONATE = "https://github.com/sponsors/OxygenCobalt"
    }
}
