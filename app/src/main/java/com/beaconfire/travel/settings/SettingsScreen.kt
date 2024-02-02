package com.beaconfire.travel.settings

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beaconfire.travel.repo.model.User
import com.beaconfire.travel.ui.component.ProfileImage
import com.beaconfire.travel.utils.CurrencyManager

@Composable
fun SettingsScreen() {
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)
    val settingsUiModel by settingsViewModel.settingsUiModel.collectAsState()
    val currencyManager = CurrencyManager.getInstance()
    var selectedCurrency by remember {mutableStateOf(currencyManager.currency)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        settingsUiModel.user?.let {
            SettingHeader(
                user = it,
                imageUri = settingsUiModel.profilePhotoUri
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        SettingItem("Feedback", Icons.Filled.Feedback, onClick = {})
        SettingItem("Currency", Icons.Filled.CurrencyExchange, onClick = {})

        Column {
            currencyManager.currencyOptions.forEach { currency ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (currency == selectedCurrency),
                            onClick = {
                                selectedCurrency = currency
                                currencyManager.setCurrency(selectedCurrency)
                            }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (currency == selectedCurrency),
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = currency)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { settingsViewModel.logout() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log Out")
        }


    }
}

@Composable
fun SettingHeader(
    user: User,
    imageUri: Uri?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileImage(
            modifier = Modifier
                .size(256.dp)
                .padding(bottom = 8.dp), imageUri = imageUri
        )
        Text(text = user.displayName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = user.email, fontSize = 16.sp, color = Color.Gray)
    }
}

@Composable
fun SettingItem(
    text: String,
    iconId: ImageVector,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
    ) {
        Icon(
            iconId,
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingPreview() {
    MaterialTheme {
        SettingsScreen()
    }
}
