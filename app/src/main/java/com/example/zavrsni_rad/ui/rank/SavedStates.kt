package com.example.zavrsni_rad.ui.rank

object SavedStates {
    var spinnerindex:Int=0

    fun setSpinnerIndex(index:Int){
        spinnerindex =index
    }

    fun getSpinnerIndex():Int{
        return spinnerindex
    }

    var navigationBarIndex: Int = 0

    fun setnavigationBarIndex(index:Int){
        navigationBarIndex =index
    }

    fun getnavigationBarIndex():Int{
        return navigationBarIndex
    }
}