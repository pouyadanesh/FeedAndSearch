package ai.alefba.feedandsearch.ui.feed

import ai.alefba.feedandsearch.data.remote.entity.feed.Session
import ai.alefba.feedandsearch.data.repository.FeedRepository
import ai.alefba.feedandsearch.util.DataResult
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FeedViewModel@Inject constructor(
    private val repository: FeedRepository
) : ViewModel() {

    val sessions: Flow<PagingData<Session>> = Pager(PagingConfig(10)) {
        FeedSource(repository)
    }.flow.cachedIn(viewModelScope)

    var loadError = mutableStateOf("")

    /*fun loadSessions() {
        Log.i(javaClass.name,"Loading sessions")
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllFeed(0).onEach { result ->
                withContext(Dispatchers.Main){
                    when(result){
                        is DataResult.Success ->{
                            isLoading.value = false
                            sessionsList.value = result.data?.session!!
                        }
                        is DataResult.Error ->{
                            isLoading.value = false
                            loadError.value = result.message!!
                        }
                        is DataResult.Loading ->{
                            if(result.data!=null)
                                sessionsList.value = result.data.session
                            isLoading.value = true
                        }
                    }
                }
            }.launchIn(this)
        }
    }*/

    fun clearError(){
        loadError.value = ""
    }
}