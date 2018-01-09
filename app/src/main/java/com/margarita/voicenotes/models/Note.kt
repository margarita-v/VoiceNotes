package com.margarita.voicenotes.models

data class Note(val id: Int,
                var description: String,
                var date: Long,
                var photoPath: String?)