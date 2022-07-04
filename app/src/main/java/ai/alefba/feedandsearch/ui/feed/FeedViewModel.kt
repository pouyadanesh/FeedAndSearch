package ai.alefba.feedandsearch.ui.feed

import ai.alefba.feedandsearch.data.remote.entity.feed.Session
import ai.alefba.feedandsearch.data.repository.FeedRepository
import ai.alefba.feedandsearch.util.DataResult
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FeedViewModel@Inject constructor(
    private val repository: FeedRepository
) : ViewModel() {

    var isLoading = mutableStateOf(false)
    var loadError = mutableStateOf("")
    val sessionsList = mutableStateOf(listOf<Session>())

    fun loadSessions() {
        Log.i(javaClass.name,"Loading sessions")
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllFeed().onEach { result ->
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
    }

    fun clearError(){
        loadError.value = ""
    }
}