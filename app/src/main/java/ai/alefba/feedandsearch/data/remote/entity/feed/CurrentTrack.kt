package ai.alefba.feedandsearch.data.remote.entity.feed

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CurrentTrack(
    @SerializedName("title") val title: String,
    @SerializedName("artwork_url") val artworkUrl: String
): Serializable
