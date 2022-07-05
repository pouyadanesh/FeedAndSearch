package ai.alefba.feedandsearch.ui.feed

import ai.alefba.feedandsearch.R
import ai.alefba.feedandsearch.data.remote.entity.feed.Session
import ai.alefba.feedandsearch.ui.common.ErrorColumn
import ai.alefba.feedandsearch.ui.common.ErrorRow
import ai.alefba.feedandsearch.ui.common.loading.LoadingColumn
import ai.alefba.feedandsearch.ui.common.loading.LoadingRow
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path.Companion.combine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun FeedGridScreen(
    viewModel: FeedViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        val sessionsList: LazyPagingItems<Session> = viewModel.sessions.collectAsLazyPagingItems()
        val state = rememberLazyGridState()
        LaunchedEffect(Unit) {
            viewModel.searchQueryChanges.onEach {
                    state.scrollToItem(0)
                    sessionsList.refresh()
                }
                .launchIn(this)
        }
        when (sessionsList.loadState.refresh) {
            is LoadState.Loading -> {
                LoadingColumn(stringResource(id = R.string.fetching_sessions))
            }
            is LoadState.Error -> {
                val error = sessionsList.loadState.refresh as LoadState.Error
                ErrorColumn(error.error.message.orEmpty())
            }
            else -> {
                LazyMoviesGrid(state, sessionsList)
            }
        }
    }
}

@Composable
private fun LazyMoviesGrid(state: LazyGridState, sessionsList: LazyPagingItems<Session>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = state
    ) {
        items(sessionsList.itemCount) { index ->
            val session = sessionsList.peek(index) ?: return@items
            SessionItem(session)
        }
        renderLoading(sessionsList.loadState)
        renderError(sessionsList.loadState)
    }
}

fun LazyGridScope.renderLoading(loadState: CombinedLoadStates) {
    if (loadState.append !is LoadState.Loading) return

    item(span = span) {
        val title = stringResource(R.string.fetching_sessions)
        LoadingRow(title = title, modifier = Modifier.padding(vertical = 8.dp))
    }
}

private fun LazyGridScope.renderError(loadState: CombinedLoadStates) {
    val message = (loadState.append as? LoadState.Error)?.error?.message ?: return

    item(span = span) {
        ErrorRow(title = message, modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun SessionItem(session: Session) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(8.dp, 4.dp)
            .fillMaxSize()
            .aspectRatio(1f)
    ) {
        Box(
            modifier = Modifier
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(session.currentTrack.artworkUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.description),
                contentScale = ContentScale.FillBounds
            )
            Card(
                shape = RoundedCornerShape(12.dp),
                backgroundColor = colorResource(id = R.color.shaded_white),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_baseline_headset_24),
                        contentDescription = "listeners count",
                    )
                    Text(
                        text = "${session.listenerCount}",
                        fontSize = 18.sp
                    )

                }
            }
            var genres = ""
            for (g in session.genres)
                genres += "$g, "
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = session.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(8.dp, 0.dp)
                )
                Text(
                    text = genres,
                    fontSize = 18.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }
    }
}