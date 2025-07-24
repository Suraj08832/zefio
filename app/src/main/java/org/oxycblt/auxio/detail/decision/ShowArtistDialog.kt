/*
 * Copyright (c) 2022 zefio Project
 * ShowArtistDialog.kt is part of zefio.
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
 
package org.oxycblt.zefio.detail.decision

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.oxycblt.zefio.R
import org.oxycblt.zefio.databinding.DialogMusicChoicesBinding
import org.oxycblt.zefio.detail.DetailViewModel
import org.oxycblt.zefio.list.ClickableListListener
import org.oxycblt.zefio.list.adapter.UpdateInstructions
import org.oxycblt.zefio.music.Artist
import org.oxycblt.zefio.ui.ViewBindingMaterialDialogFragment
import org.oxycblt.zefio.util.collectImmediately
import org.oxycblt.zefio.util.logD

/**
 * A picker [ViewBindingMaterialDialogFragment] intended for when the [Artist] to show is ambiguous.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@AndroidEntryPoint
class ShowArtistDialog :
    ViewBindingMaterialDialogFragment<DialogMusicChoicesBinding>(), ClickableListListener<Artist> {
    private val detailModel: DetailViewModel by activityViewModels()
    private val pickerModel: DetailPickerViewModel by viewModels()
    // Information about what artists to show choices for is initially within the navigation
    // arguments as UIDs, as that is the only safe way to parcel an artist.
    private val args: ShowArtistDialogArgs by navArgs()
    private val choiceAdapter = ArtistShowChoice(this)

    override fun onConfigDialog(builder: AlertDialog.Builder) {
        builder.setTitle(R.string.lbl_artists).setNegativeButton(R.string.lbl_cancel, null)
    }

    override fun onCreateBinding(inflater: LayoutInflater) =
        DialogMusicChoicesBinding.inflate(inflater)

    override fun onBindingCreated(binding: DialogMusicChoicesBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)

        binding.choiceRecycler.apply {
            itemAnimator = null
            adapter = choiceAdapter
        }

        detailModel.toShow.consume()
        pickerModel.setArtistChoiceUid(args.itemUid)
        collectImmediately(pickerModel.artistChoices, ::updateChoices)
    }

    override fun onDestroyBinding(binding: DialogMusicChoicesBinding) {
        super.onDestroyBinding(binding)
        binding.choiceRecycler.adapter = null
    }

    override fun onClick(item: Artist, viewHolder: RecyclerView.ViewHolder) {
        findNavController().navigateUp()
        // User made a choice, navigate to the artist.
        detailModel.showArtist(item)
    }

    private fun updateChoices(choices: ArtistShowChoices?) {
        if (choices == null) {
            logD("No choices to show, navigating away")
            findNavController().navigateUp()
            return
        }
        choiceAdapter.update(choices.choices, UpdateInstructions.Diff)
    }
}
