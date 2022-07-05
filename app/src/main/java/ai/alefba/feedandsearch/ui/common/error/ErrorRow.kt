package ai.alefba.feedandsearch.ui.common

import ai.alefba.feedandsearch.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ErrorRow(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Face,
            contentDescription = stringResource(id = R.string.error_icon_content_description),
            modifier = Modifier.size(40.dp)
        )
        Text(title)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ErrorRowPreview() {
    ErrorRow(title = "Oopsie!")
}