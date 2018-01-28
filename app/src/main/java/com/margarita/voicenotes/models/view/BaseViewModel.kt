package com.margarita.voicenotes.models.view

import io.realm.RealmObject

abstract class BaseViewModel<ItemType: RealmObject>(var item: ItemType,
                                                    var checked: Boolean = false)