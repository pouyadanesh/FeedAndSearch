package ai.alefba.feedandsearch.ui.feed

import ai.alefba.feedandsearch.R
import ai.alefba.feedandsearch.data.remote.entity.feed.Session
import ai.alefba.feedandsearch.ui.common.ErrorColumn
import ai.alefba.feedandsearch.ui.common.ErrorRow
import ai.alefba.feedandsearch.ui.common.loading.LoadingColumn
import ai.alefba.feedandsearch.ui.common.loading.LoadingRow
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.input.ImeAction
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    val loadError by remember { viewModel.loadError }
    val searchQuery = remember { mutableStateOf("") }
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            Surface(modifier = Modifier.fillMaxWidth(), elevation = 16.dp) {
                Column(
                    Modifier
                        .background(MaterialTheme.colors.surface)
                        .padding(bottom = 2.dp)
                ) {
                    SearchBar(searchQuery, viewModel::onSearch)
                }
            }
        },
        content = {
            FeedGridScreen(viewModel)
        }
    )
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
}

@Composable
private fun SearchBar(searchQuery: MutableState<String>, onSearch: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .heightIn(max = 50.dp)
            .fillMaxWidth(),
        value = searchQuery.value,
        textStyle = MaterialTheme.typography.subtitle1,
        singleLine = true,
        shape = RoundedCornerShape(50),
        placeholder = { Text(stringResource(id = R.string.search), color = Color.Gray) },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            AnimatedVisibility(visible = searchQuery.value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        searchQuery.value = ""
                        onSearch("")
                    }
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        onValueChange = { query ->
            searchQuery.value = query
            onSearch(query)
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = MaterialTheme.colors.surface,
            unfocusedIndicatorColor = MaterialTheme.colors.surface
        )
    )
}

