

package com.bnyro.translate.ui.models

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnyro.translate.DatabaseHolder.Companion.Db
import com.bnyro.translate.db.obj.HistoryItem
import com.bnyro.translate.db.obj.HistoryItemType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
class HistoryModel : ViewModel() {
    val items = mutableStateListOf<HistoryItem>()

    fun fetchItems(itemType: HistoryItemType) {
        items.clear()

        viewModelScope.launch(Dispatchers.IO) {
            items.addAll(Db.historyDao().getAll(itemType).reversed())
        }
    }

    fun clearItems(itemType: HistoryItemType) {
        items.clear()

        viewModelScope.launch(Dispatchers.IO) {
            Db.historyDao().deleteAll(itemType)
        }
    }

    fun deleteItem(historyItem: HistoryItem) {
        items.removeAll { it.id == historyItem.id }
        
        viewModelScope.launch(Dispatchers.IO) {
            Db.historyDao().delete(historyItem)
        }
    }
}
