package ai.alefba.feedandsearch.ui.feed

import ai.alefba.feedandsearch.R
import ai.alefba.feedandsearch.data.remote.entity.feed.Session
import ai.alefba.feedandsearch.ui.common.ErrorColumn
import ai.alefba.feedandsearch.ui.common.ErrorRow
import ai.alefba.feedandsearch.ui.common.loading.LoadingColumn
import ai.alefba.feedandsearch.ui.common.loading.LoadingRow
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest

val span: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(2) }

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    val loadError by remember { viewModel.loadError }
    val page by remember { viewModel.page }

    //Make a disposable effect observe lifecycle to trigger a data load/update
    //when activity comes to the foreground
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.sessions
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    //Check if there are errors to display
    if (loadError.isNotEmpty()) {
        Toast.makeText(LocalContext.current, loadError, Toast.LENGTH_LONG).show()
        viewModel.clearError()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        val sessionsList: LazyPagingItems<Session> = viewModel.sessions.collectAsLazyPagingItems()
        val state = rememberLazyGridState()
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
                    fontWeight = Bold,
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