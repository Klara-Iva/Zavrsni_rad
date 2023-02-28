package com.example.zavrsni_rad.ui.preferences

import java.util.Arrays

object SavedUserChips {
    var list: ArrayList<String> = arrayListOf()

    fun addItems(items:ArrayList<String>) {
        list.clear()
        list.addAll(items)

    }

    fun getChips(): List<String> = list
}