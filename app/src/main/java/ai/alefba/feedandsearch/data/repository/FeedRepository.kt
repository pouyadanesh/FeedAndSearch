package ai.alefba.feedandsearch.data.repository

import ai.alefba.feedandsearch.data.remote.FeedApi
import ai.alefba.feedandsearch.data.remote.entity.feed.Data
import ai.alefba.feedandsearch.data.remote.entity.feed.ServerResponse
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val feedApi: FeedApi
) {
    suspend fun getAllFeed(page: Int): ServerResponse<Data>{
        return feedApi.getAllFeed(page)
    }

    suspend fun getSearchResult(): ServerResponse<Data> {
        return feedApi.getSearchResults()
    }
}