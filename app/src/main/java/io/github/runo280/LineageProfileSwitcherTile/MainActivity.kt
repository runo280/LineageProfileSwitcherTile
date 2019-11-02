package io.github.runo280.LineageProfileSwitcherTile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.service.quicksettings.TileService
import android.view.Gravity
import android.widget.Toast
import io.github.runo280.LineageProfileSwitcherTile.databinding.ActivityMainBinding
import lineageos.app.Profile
import lineageos.app.ProfileManager
import lineageos.profiles.RingModeSettings

class MainActivity : Activity() {

    private lateinit var view: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (TileService.ACTION_QS_TILE_PREFERENCES == intent.action) {
            startActivity(Intent().apply {
                action = "org.lineageos.lineageparts.PROFILES_SETTINGS"
                `package` = "org.lineageos.lineageparts"
            })
            finish()
        }

        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)

        val pm = ProfileManager.getInstance(this)

        view.btnReset.setOnLongClickListener {
            // reset to default profiles
            pm.resetAll()
            done()
            true
        }

        view.btnAdd.setOnLongClickListener {
            //delete default profiles
            pm.profiles.forEach {
                pm.removeProfile(it)
            }
            // add new profiles
            pm.addProfile(createProfile(LOUD))
            pm.addProfile(createProfile(LIGHT))
            pm.addProfile(createProfile(NIGHT))
            pm.addProfile(createProfile(VIBRATE, RingModeSettings.RING_MODE_VIBRATE))
            done()
            true
        }
    }

    private fun createProfile(
        name: String,
        ringtoneMode: String = RingModeSettings.RING_MODE_NORMAL
    ) = Profile(name).apply {
        ringMode = RingModeSettings(ringtoneMode, true)
        profileType = Profile.Type.TOGGLE
    }

    private fun done() =
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
        }.show()
}