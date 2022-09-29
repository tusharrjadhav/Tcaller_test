package com.tcall.tcall_test.screens

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.tcall.tcall_test.BR

class DataModel : BaseObservable() {

    @get:Bindable
    var char10th: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.char10th)
        }

    @get:Bindable
    var every10thChar: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.every10thChar)
        }

    @get:Bindable
    var wordCount: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.wordCount)
        }
}