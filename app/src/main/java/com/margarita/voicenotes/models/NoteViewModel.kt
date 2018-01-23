package com.margarita.voicenotes.models

import com.margarita.voicenotes.models.entities.NoteItem

class NoteViewModel(val noteItem: NoteItem,
                    var checked: Boolean = false)