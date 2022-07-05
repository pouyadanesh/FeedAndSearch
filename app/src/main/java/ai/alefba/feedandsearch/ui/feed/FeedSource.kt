package ai.alefba.feedandsearch.ui.feed

import ai.alefba.feedandsearch.data.remote.entity.feed.Session
import ai.alefba.feedandsearch.data.repository.FeedRepository
import androidx.paging.PagingSource
import androidx.paging.PagingState

class FeedSource(
    private val feedRepository: FeedRepository
) : PagingSource<Int, Session>() {

    override fun getRefreshKey(state: PagingState<Int, Session>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Session> {
        return try {
            val nextPage = params.key ?: 1
            val getFeedResponse = feedRepository.getAllFeed(nextPage)

            LoadResult.Page(
                data = getFeedResponse.data.session,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (nextPage >= 6) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}