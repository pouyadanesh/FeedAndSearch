package ai.alefba.feedandsearch.data.repository

import ai.alefba.feedandsearch.data.remote.FeedApi
import ai.alefba.feedandsearch.data.remote.entity.feed.Data
import ai.alefba.feedandsearch.util.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FeedRepositoryImpl(
    private val api : FeedApi
): FeedRepository {

    override fun getAllFeed(): Flow<DataResult<Data>> = flow {
        emit(DataResult.Loading())
        //Fetch remote data
        val remoteFeed = api.getAllFeed()
        if (remoteFeed.data!=null && remoteFeed.data.session!=null && remoteFeed.data.session.isNotEmpty())
            emit(DataResult.Success(remoteFeed.data))
        else
            emit(DataResult.Error(message = "Api call had an empty response"))
    }

    override fun getSearchResult(): Flow<DataResult<Data>> = flow {
        emit(DataResult.Loading())
    }
}