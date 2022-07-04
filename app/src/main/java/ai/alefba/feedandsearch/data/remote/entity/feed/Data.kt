package ai.alefba.feedandsearch.data.remote.entity.feed

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Data(
    @SerializedName("sessions") val session: List<Session>
): Serializable
