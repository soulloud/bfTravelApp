package com.beaconfire.travel.settings

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beaconfire.travel.R
import com.beaconfire.travel.repo.model.User
import com.beaconfire.travel.ui.component.ProfileImage
import com.beaconfire.travel.utils.MockData

@Composable
fun SettingsScreen() {
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SettingHeader(user = MockData.users[0], image = R.drawable.ic_profile_shuaige, 96)
        Spacer(modifier = Modifier.height(24.dp))
        SettingItem("Feedback", Icons.Filled.Feedback, onClick = {})
        SettingItem("Currency", Icons.Filled.CurrencyExchange, onClick = {})
        Spacer(modifier = Modifier.height(128.dp))
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
    image: Int,
    size: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileImage(image = image, size = size)
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
