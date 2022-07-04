package ai.alefba.feedandsearch.data.repository

import ai.alefba.feedandsearch.data.remote.FeedApi
import ai.alefba.feedandsearch.data.remote.entity.feed.Data
import ai.alefba.feedandsearch.util.DataResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface FeedRepository {
    fun getAllFeed(): Flow<DataResult<Data>>

    fun getSearchResult(): Flow<DataResult<Data>>
}