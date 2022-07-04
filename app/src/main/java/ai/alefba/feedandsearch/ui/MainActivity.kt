package ai.alefba.feedandsearch.ui

import ai.alefba.feedandsearch.ui.feed.FeedScreen
import ai.alefba.feedandsearch.ui.feed.FeedViewModel
import ai.alefba.feedandsearch.ui.theme.FeedAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FeedAppTheme {
                FeedScreen()
            }
        }
    }
}