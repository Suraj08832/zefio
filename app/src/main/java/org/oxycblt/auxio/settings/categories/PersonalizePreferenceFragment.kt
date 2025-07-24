/*
 * Copyright (c) 2023 zefio Project
 * PersonalizePreferenceFragment.kt is part of zefio.
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
 
package org.oxycblt.zefio.settings.categories

import androidx.navigation.fragment.findNavController
import org.oxycblt.zefio.R
import org.oxycblt.zefio.settings.BasePreferenceFragment
import org.oxycblt.zefio.settings.ui.WrappedDialogPreference
import org.oxycblt.zefio.util.logD
import org.oxycblt.zefio.util.navigateSafe

/**
 * Personalization settings interface.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
class PersonalizePreferenceFragment : BasePreferenceFragment(R.xml.preferences_personalize) {
    override fun onOpenDialogPreference(preference: WrappedDialogPreference) {
        if (preference.key == getString(R.string.set_key_home_tabs)) {
            logD("Navigating to home tab dialog")
            findNavController().navigateSafe(PersonalizePreferenceFragmentDirections.tabSettings())
        }
    }
}
