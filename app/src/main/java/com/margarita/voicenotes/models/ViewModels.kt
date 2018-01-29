package com.margarita.voicenotes.models

import com.margarita.voicenotes.models.entities.NoteItem
import io.realm.RealmObject

/**
 * Base view model for all item types
 */
abstract class BaseViewModel<ItemType: RealmObject>(var item: ItemType,
                                                    var checked: Boolean = false)

/**
 * View model for note items
 */
class NoteViewModel(noteItem: NoteItem,
                    checked: Boolean = false) : BaseViewModel<NoteItem>(noteItem, checked)
