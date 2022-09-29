package com.tcall.tcall_test.screens

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcall.tcall_test.testing.OpenForTesting
import com.tcall.tcall_test.use_cases.GetDataUseCase
import com.tcall.tcall_test.util.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@OpenForTesting
@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val useCase: GetDataUseCase
) : ViewModel() {

    val dataModel = DataModel()

    //region Unused variable design for Compose UI
    private val _tenthChar = mutableStateOf(ScreenState())
    val tenthChar: State<ScreenState> = _tenthChar

    private val _every10thChar = mutableStateOf(ScreenState())
    val every10thChar: State<ScreenState> = _every10thChar

    private val _charCount = mutableStateOf(ScreenState())
    val charCount: State<ScreenState> = _charCount
    //endregion


    fun fetchContent() {
        try {
            get10thCharacterValue()
            getEveryTenthCharacter()
            getCharCount()
        } catch (exception: Exception) {
            Log.e("TAG", ""+exception.message)
        }
    }

    fun get10thCharacterValue() {
        useCase.get10thChar().onEach {
            when (it) {
                is DataResult.Loading -> {
                    _tenthChar.value = ScreenState(loading = true)
                }
                is DataResult.Success -> {
                    //_tenthChar.value = ScreenState(data = get10thChar)
                    dataModel.char10th = it.data.toString()
                }
                is DataResult.Error -> {
                    _tenthChar.value = ScreenState(error = it.exception.toString())
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getEveryTenthCharacter() {
        useCase.getEvery10thChar().onEach {
            when (it) {
                is DataResult.Loading -> {
                    _every10thChar.value = ScreenState(loading = true)
                }
                is DataResult.Success -> {
                    //_every10thChar.value = ScreenState(data = it.data)
                    dataModel.every10thChar = it.data.toString()
                }
                is DataResult.Error -> {
                    _every10thChar.value = ScreenState(error = it.exception.toString())
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getCharCount() {
        useCase.getCharCount().onEach {
            when (it) {
                is DataResult.Loading -> {
                    _charCount.value = ScreenState(loading = true)
                }
                is DataResult.Success -> {
                    //_charCount.value = ScreenState(data = it.data)
                    dataModel.wordCount = it.data.toString()
                }
                is DataResult.Error -> {
                    _charCount.value = ScreenState(error = it.exception.toString())
                }
            }
        }.launchIn(viewModelScope)
    }
}