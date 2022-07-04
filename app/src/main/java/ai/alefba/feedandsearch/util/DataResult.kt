package ai.alefba.feedandsearch.util

sealed class DataResult<T>(val message : String? = null, val data : T? = null) {
    class Loading<T>(data: T? = null) : DataResult<T>(data = data)
    class Error<T>(message: String, data: T? = null) : DataResult<T>(message, data)
    class Success<T>(data: T) : DataResult<T>(data = data)
}
