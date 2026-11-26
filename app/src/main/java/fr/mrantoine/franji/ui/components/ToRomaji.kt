package fr.mrantoine.franji.ui.components

import dev.esnault.wanakana.core.Wanakana
import fr.mrantoine.franji.storage.SettingsStorage

fun String.formatKana(): String =
    if(SettingsStorage.isRomaji()) Wanakana.toRomaji(this, upcaseKatakana = true) else this