package com.beaconfire.travel.profile

import ProfileViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.beaconfire.travel.ui.component.ProfileImage
import com.beaconfire.travel.ui.component.button.CameraCaptureButton
import com.beaconfire.travel.ui.component.button.GalleryPhotoPickerButton
import com.beaconfire.travel.ui.component.review.ReviewCard

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val profileUiModel by profileViewModel.profileUiModel.collectAsState()
    val reviewUiModel by profileViewModel.reviewUiModel.collectAsState()
    var showEditProfile by remember { mutableStateOf(false) }
    val showUploadedPhotos by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        profileUiModel.profile?.let { profile ->

            val imageModifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable {}

            if (profileUiModel.capturedImageUri?.path?.isNotEmpty() == true) {
                ProfileImage(
                    modifier = Modifier
                        .size(256.dp)
                        .padding(bottom = 8.dp),
                    imageUri = profileUiModel.capturedImageUri
                )
            }

            GalleryPhotoPickerButton {
                profileViewModel.onImageCaptured(it)
                profileViewModel.setAsProfilePhoto()
            }
            CameraCaptureButton {
                profileViewModel.onImageCaptured(it)
                profileViewModel.setAsProfilePhoto()
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
        Text(
            text = "$label: $newValue",
            maxLines = maxLines,
            modifier = Modifier.clickable {
                showDialog = !showDialog
            }
        )

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
                        onValueChange = {
                            newValue = it
                        },
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
    }
}


@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    MaterialTheme {
        val profileViewModel = viewModel<ProfileViewModel>()
        ProfileScreen(profileViewModel)
    }
}



@Composable
fun ImageLoader(url: String, modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(model = url)

    Image(
        painter = painter,
        contentDescription = "Loaded image",
        modifier = modifier,
        contentScale = ContentScale.Crop // or other ContentScale as needed
    )
}


