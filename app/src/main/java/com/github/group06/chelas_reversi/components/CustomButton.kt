package com.github.group06.chelas_reversi.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {},
    fontSize: Float = 5f,
    fontWeight: FontWeight = FontWeight.Bold,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    enabled: Boolean = true
) {

    Button(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        enabled = enabled
    ) {

        Text(
            text = text,
            fontSize = TextUnit(fontSize, TextUnitType.Em),
            fontWeight = fontWeight
        )

    }

}

@Composable
fun RoundedIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    backgroundColor: Color,
    tint: Color = Color.White,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {

    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(backgroundColor, CircleShape)
    ) {
        Icon(imageVector, contentDescription = "", tint = tint)
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CustomButtonPreview() {
    CustomButton(text = "Clica-me!", onClick = {})
}