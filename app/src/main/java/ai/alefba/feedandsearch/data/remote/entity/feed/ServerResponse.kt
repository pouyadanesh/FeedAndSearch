package ai.alefba.feedandsearch.data.remote.entity.feed

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ServerResponse<T>(
    @SerializedName("data") val `data`: T,
    var page: Int
): Serializable
