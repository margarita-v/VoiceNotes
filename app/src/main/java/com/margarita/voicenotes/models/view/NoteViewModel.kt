package com.margarita.voicenotes.models.view

import com.margarita.voicenotes.models.entities.NoteItem

class NoteViewModel(noteItem: NoteItem,
                    checked: Boolean = false) : BaseViewModel<NoteItem>(noteItem, checked)