package ai.alefba.feedandsearch.ui.feed

import ai.alefba.feedandsearch.data.remote.entity.feed.Session
import ai.alefba.feedandsearch.data.repository.FeedRepository
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: FeedRepository
) : ViewModel() {
    private val pager: Pager<Int, Session> =
        Pager(config = PagingConfig(pageSize = 10), pagingSourceFactory = ::initPagingSource)
    val filterStateChanges = MutableSharedFlow<Unit>()
    private val searchQuery = MutableStateFlow("")
    private val _searchQueryChanges = MutableSharedFlow<Unit>()
    val searchQueryChanges: SharedFlow<Unit> = _searchQueryChanges.asSharedFlow()

    @VisibleForTesting
    lateinit var pagingSource: FeedSource
        private set

    init {

        searchQuery
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { _searchQueryChanges.emit(Unit) }
            .launchIn(viewModelScope)
    }

    @VisibleForTesting
    fun initPagingSource() = FeedSource(
        repository,
        searchQuery.value
    ).also(::pagingSource::set)

    fun onSearch(query: String) {
        if (searchQuery.value.isEmpty() && query.isBlank()) return
        if (searchQuery.value.isBlank() && query.length < 3) return

        searchQuery.tryEmit(query)
    }

    val sessions = pager.flow

    var loadError = mutableStateOf("")

    fun clearError() {
        loadError.value = ""
    }
}