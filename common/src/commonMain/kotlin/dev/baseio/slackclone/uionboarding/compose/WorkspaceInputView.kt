package dev.baseio.slackclone.uionboarding.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.baseio.slackclone.commonui.theme.SlackCloneColorProvider
import dev.baseio.slackclone.commonui.theme.SlackCloneTypography

@Composable
fun TextHttps() {
    Text(
        text = "https://",
        style = textStyleField().copy(
            color = SlackCloneColorProvider.colors.textPrimary.copy(
                alpha = 0.4f
            )
        )
    )
}

@Composable
fun TextSlackCom() {
    Text(
        ".slack.com",
        style = textStyleField().copy(
            color = SlackCloneColorProvider.colors.textPrimary.copy(
                alpha = 0.4f
            )
        ),
        overflow = TextOverflow.Clip,
        maxLines = 1
    )
}

@Composable
fun WorkspaceTF(workspace: String, onUpdate: (String) -> Unit) {
    BasicTextField(
        value = workspace,
        onValueChange = { newEmail ->
            onUpdate(newEmail)
        },
        textStyle = textStyleField(),
        singleLine = true,
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .padding(top = 12.dp, bottom = 12.dp),
        maxLines = 1,
        cursorBrush = SolidColor(SlackCloneColorProvider.colors.textPrimary),
        decorationBox = { inputTf ->
            Box {
                if (workspace.isEmpty()) {
                    Text(
                        text = "your-workspace",
                        style = textStyleField(),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.width(IntrinsicSize.Max)
                    )
                } else {
                    inputTf()
                }
            }
        }
    )
}

@Composable
private fun textStyleField() = SlackCloneTypography.subtitle1.copy(
    color = SlackCloneColorProvider.colors.textPrimary.copy(alpha = 0.7f),
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Start
)
