package com.margarita.voicenotes.models

data class NoteItem(val id: Int,
                    var description: String,
                    var date: Long,
                    var photoPath: String? = null)