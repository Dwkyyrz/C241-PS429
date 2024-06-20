package com.TeamBangkit.animaldetection.View.History

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TeamBangkit.animaldetection.Data.DB.ClassificationHistory
import com.TeamBangkit.animaldetection.MyApplication
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val _history = MutableLiveData<List<ClassificationHistory>>()
    val history: LiveData<List<ClassificationHistory>> = _history

    init {
        fetchHistory()
    }

    fun fetchHistory() {
        viewModelScope.launch {
            val historyList = MyApplication.database.classificationHistoryDao().getAll()
            _history.postValue(historyList)
        }
    }

    fun deleteHistory(history: ClassificationHistory) {
        viewModelScope.launch {
            MyApplication.database.classificationHistoryDao().delete(history)
            fetchHistory()
        }
    }
}