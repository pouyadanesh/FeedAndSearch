package ai.alefba.feedandsearch.data.remote.entity.feed

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Session(
    @SerializedName("name") val name: String,
    @SerializedName("listener_count") val listenerCount: Int,
    @SerializedName("genres") val genres: List<String>,
    @SerializedName("current_track") val currentTrack: CurrentTrack
):Serializable
