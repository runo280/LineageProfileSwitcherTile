package io.github.runo280.LineageProfileSwitcherTile

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.DrawableRes
import lineageos.app.Profile
import lineageos.app.ProfileManager

class ProfileSwitchTileService : TileService() {

    override fun onClick() {
        super.onClick()

        val pm = ProfileManager.getInstance(this)
        if (!pm.isProfilesEnabled) {
            toast(this, "Profiles are disabled")
            return
        }

        when (pm.activeProfile.name) {
            LOUD -> setProfile(pm, LIGHT, R.drawable.ic_light)
            LIGHT -> setProfile(pm, VIBRATE, R.drawable.ic_vibrate)
            VIBRATE -> setProfile(pm, NIGHT, R.drawable.ic_night)
            NIGHT -> setProfile(pm, LOUD, R.drawable.ic_loud)
            else -> {
                if (pm.profileExists("Loud"))
                    setProfile(pm, LOUD, R.drawable.ic_loud)
                else
                    toast(this, "Profiles are not available")
            }
        }
    }

    private fun setProfile(pm: ProfileManager, name: String, @DrawableRes icon: Int) {
        getProfileByName(name, pm)?.let {
            pm.setActiveProfile(it.uuid)
            setIcon(name, icon)
        }
    }

    private fun setIcon(title: String, @DrawableRes icon: Int) {
        qsTile.apply {
            label = title
            state = Tile.STATE_ACTIVE
            setIcon(Icon.createWithResource(this@ProfileSwitchTileService, icon))
        }.updateTile()
    }

    private fun getProfileByName(name: String, pm: ProfileManager): Profile? {
        pm.profiles.forEach {
            if (name == it.name) return it
        }
        return null
    }
}