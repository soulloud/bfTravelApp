package com.beaconfire.travel.profile

import ProfileViewModel
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beaconfire.travel.constant.Constant
import com.beaconfire.travel.ui.component.ProfileImage
import com.beaconfire.travel.ui.component.button.CameraCaptureButton
import com.beaconfire.travel.ui.component.button.GalleryPhotoPickerButton
import com.beaconfire.travel.ui.component.review.ReviewCard

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val profileUiModel by profileViewModel.profileUiModel.collectAsState()
    val reviewUiModel by profileViewModel.reviewUiModel.collectAsState()
    var showSaveProfilePhotoBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        profileUiModel.profile?.let { profile ->
            if (profileUiModel.capturedImageUri?.path?.isNotEmpty() == true) {
                ProfileImage(
                    modifier = Modifier
                        .size(256.dp)
                        .padding(bottom = 8.dp),
                    imageUri = profileUiModel.capturedImageUri
                ) { showSaveProfilePhotoBottomSheet = true }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ClickableTextWithDialog(
                label = "Display Name",
                value = profile.fullName,
                profileViewModel = profileViewModel
            )

            Spacer(modifier = Modifier.height(8.dp))

            ClickableTextWithDialog(
                label = "Location",
                value = profile.location,
                profileViewModel = profileViewModel
            )

            Spacer(modifier = Modifier.height(8.dp))

            ClickableTextWithDialog(
                label = "Introduction",
                value = profile.aboutYou,
                maxLines = 5,
                profileViewModel = profileViewModel
            )

            Spacer(modifier = Modifier.height(8.dp))

            ClickableTextWithDialog(
                label = "Join Date",
                value = profile.joinDate,
                profileViewModel = profileViewModel
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileItem("Your reviews", Icons.Filled.Reviews, onClick = {})

            LazyRow(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(reviewUiModel.reviews) { review ->
                    ReviewCard(
                        author = profileUiModel.profile?.fullName,
                        score = review.score,
                        description = review.description,
                        time = review.timestamp
                    )
                }
            }

            if (showSaveProfilePhotoBottomSheet) {
                ProfilePhotoUpdateBottomSheet(profileViewModel) {
                    showSaveProfilePhotoBottomSheet = false
                }
            }
        }
    }
}

@Composable
fun ProfileItem(text: String, iconId: ImageVector, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() }
    ) {
        Icon(iconId, contentDescription = text, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 16.sp)
    }
}

@Composable
fun ClickableTextWithDialog(
    label: String,
    value: String,
    maxLines: Int = 1,
    profileViewModel: ProfileViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var newValue by remember { mutableStateOf(value) }

    Column {
        Row {
            Text(
                text = "$label:",
                maxLines = maxLines,
                modifier = Modifier
                    .weight(1.0f)
                    .padding(start = 16.dp)
            )
            Text(
                text = newValue,
                textAlign = TextAlign.End,
                maxLines = maxLines,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1.0f)
                    .clickable { showDialog = !showDialog }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text(text = "Edit $label")
                },
                text = {
                    OutlinedTextField(
                        value = newValue,
                        onValueChange = { newValue = it },
                        label = { Text("New $label") },
                        maxLines = maxLines,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            profileViewModel.updateName(newValue)
                        }
                    ) {
                        Text(text = "Save")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            )
        }

        HorizontalDivider()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePhotoUpdateBottomSheet(
    profileViewModel: ProfileViewModel,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(
                    start = 8.dp,
                    end = 8.dp,
                    top = 32.dp,
                    bottom = 32.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                itemsIndexed(Constant.DEFAULT_USER_PROFILE_PHOTOS) { _, profilePhotoId ->
                    ProfileImage(modifier = Modifier.size(48.dp), imageId = profilePhotoId) {
                        profileViewModel.onImageCaptured(profilePhotoId)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            GalleryPhotoPickerButton { profileViewModel.onImageCaptured(it) }
            CameraCaptureButton { profileViewModel.onImageCaptured(it) }

            Button(
                modifier = Modifier.padding(bottom = 8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                elevation = null,
                onClick = {
                    profileViewModel.setAsProfilePhoto()
                    onDismiss()
                }) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    text = "Save Profile Photo",
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
