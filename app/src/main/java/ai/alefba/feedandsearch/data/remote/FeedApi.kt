package ai.alefba.feedandsearch.data.remote

import ai.alefba.feedandsearch.data.remote.entity.feed.Data
import ai.alefba.feedandsearch.data.remote.entity.feed.ServerResponse
import ai.alefba.feedandsearch.util.DataResult
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedApi {

    companion object{
        const val BASE_URL = "https://www.mocky.io/v2/"
    }

    @GET("5df79a3a320000f0612e0115")
    suspend fun getAllFeed(@Query("page") page: Int) : ServerResponse<Data>

    @GET("5df79b1f320000f4612e011e")
    suspend fun getSearchResults() : ServerResponse<Data>
}