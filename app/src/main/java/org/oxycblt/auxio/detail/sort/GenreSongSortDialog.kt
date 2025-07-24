/*
 * Copyright (c) 2023 zefio Project
 * GenreSongSortDialog.kt is part of zefio.
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
 
package org.oxycblt.zefio.detail.sort

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.oxycblt.zefio.databinding.DialogSortBinding
import org.oxycblt.zefio.detail.DetailViewModel
import org.oxycblt.zefio.list.sort.Sort
import org.oxycblt.zefio.list.sort.SortDialog
import org.oxycblt.zefio.music.Genre
import org.oxycblt.zefio.util.collectImmediately
import org.oxycblt.zefio.util.logD

/**
 * A [SortDialog] that controls the [Sort] of [DetailViewModel.genreSongSort].
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@AndroidEntryPoint
class GenreSongSortDialog : SortDialog() {
    private val detailModel: DetailViewModel by activityViewModels()

    override fun onBindingCreated(binding: DialogSortBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)

        // --- VIEWMODEL SETUP ---
        collectImmediately(detailModel.currentGenre, ::updateGenre)
    }

    override fun getInitialSort() = detailModel.genreSongSort

    override fun applyChosenSort(sort: Sort) {
        detailModel.applyGenreSongSort(sort)
    }

    override fun getModeChoices() =
        listOf(
            Sort.Mode.ByName,
            Sort.Mode.ByArtist,
            Sort.Mode.ByAlbum,
            Sort.Mode.ByDate,
            Sort.Mode.ByDuration)

    private fun updateGenre(genre: Genre?) {
        if (genre == null) {
            logD("No genre to sort, navigating away")
            findNavController().navigateUp()
        }
    }
}
