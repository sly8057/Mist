package com.example.mist.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mist.R
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.mist.ui.theme.DutchWhite
import com.example.mist.ui.theme.EerieBlack
import com.example.mist.ui.theme.Night
import com.example.mist.ui.theme.getImageVectorFromDrawable

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button (
        modifier = Modifier
            .width(170.dp)
            .height(50.dp),
        shape = RoundedCornerShape(size = 20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = { onClick() },
        enabled = enabled
    ) {
        Text(
            text = text,
            // MonoText
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)),
                // color = MaterialTheme.colorScheme.onBackground,
            )
        )
    }
}

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit) {
    Button (
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(size = 20.dp)
            )
            .width(170.dp)
            .height(50.dp),
        shape = RoundedCornerShape(size = 20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = { onClick() }
    ) {
        Text(
            text = text,
            // MonoText
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)),
                // color = MaterialTheme.colorScheme.onBackground,
            )
        )
    }
}

@Composable
fun PrimaryTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier, // importante para permitir personalización externa
) {
    TextButton(
        onClick = { onClick() },
        modifier = modifier
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold))
            )
        )
    }
}

@Composable
fun SecondaryTextButton(
    text: String,
    onClick: () -> Unit
) {
    TextButton(onClick = { onClick() }) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold))
            )
        )
    }
}

@Composable
fun PrimaryInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPasswordField: Boolean = false,
    passwordVisible: Boolean = false,
    onVisibilityToggle: (() -> Unit)? = null,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { newText ->
            val filteredText = newText.filter { it != ' ' && it != '\n' && it != '\t' }
            onValueChange(filteredText)
        },
        label = { Text(text = label) },
        singleLine = singleLine,
        visualTransformation = if (isPasswordField && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPasswordField && onVisibilityToggle != null) {
            {
                val icon = if (passwordVisible)
                    getImageVectorFromDrawable(R.drawable.ic_lock)
                else
                    getImageVectorFromDrawable(R.drawable.ic_lock_open)

                IconButton(onClick = onVisibilityToggle) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (passwordVisible) "Contraseña visible" else "Contraseña oculta",
                        tint = DutchWhite.copy(alpha = 0.5f)
                    )
                }
            }
        } else null,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Night.copy(alpha = 0.1f),
            unfocusedContainerColor = Night.copy(alpha = 0.1f),
            focusedTextColor = DutchWhite,
            unfocusedTextColor = DutchWhite.copy(alpha = 0.5f)
        )
    )
}

@Composable
fun SecondaryInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    trailingIcon: Int,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier,
        shape = RoundedCornerShape(size = 16.dp),
        value = value,
        onValueChange = { newText ->
            val filteredText = newText.filter { it != '\n' && it != '\t' }
            onValueChange(filteredText)
        },
        placeholder = { Text(text = label) },
        singleLine = singleLine,
        trailingIcon = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = trailingIcon),
                    contentDescription = "Buscar",
                    tint = DutchWhite.copy(alpha = 0.5f)
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Night,
            unfocusedContainerColor = EerieBlack,
            focusedTextColor = DutchWhite,
            unfocusedTextColor = DutchWhite.copy(alpha = 0.8f),
            focusedIndicatorColor = DutchWhite,
            unfocusedIndicatorColor = DutchWhite.copy(alpha = 0.8f)
        )
    )
}
