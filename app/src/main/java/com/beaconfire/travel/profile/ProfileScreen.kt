package com.beaconfire.travel.profile

import ProfileViewModel
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.beaconfire.travel.repo.model.Profile


@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val profileUiModel by profileViewModel.profileUiModel.collectAsState()
    var showEditProfile by remember { mutableStateOf(false) }
    var showUploadedPhotos by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileItem("Edit Profile", Icons.Filled.Edit) { showEditProfile = !showEditProfile }

        if (showEditProfile) {
            EditProfileSection(profileViewModel, profileUiModel)
        }

        ProfileItem("Your reviews", Icons.Filled.Reviews, onClick = {})
        Spacer(modifier = Modifier.height(128.dp))

        Button(
            onClick = { /* Handle log out */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Account")
        }
    }
}

@Composable
fun EditProfileSection(profileViewModel: ProfileViewModel, profileUiModel: ProfileUiModel?) {
    profileUiModel?.profile?.let { profile ->
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                ImageLoader(
                    url = profile.photoImage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Text("Full Name: ${profile.fullName}", Modifier.padding(8.dp))
                ProfileEditableField("Full Name", profile.fullName) { newName ->
                    profileViewModel.updateFullName(newName)
                }
                Text("Location: ${profile.location}", Modifier.padding(8.dp))
                ProfileEditableField("Location", profile.location) { newLocation ->
                    profileViewModel.updateLocation(newLocation)
                }
                Text("About You: ${profile.aboutYou}", Modifier.padding(8.dp))
                ProfileEditableField("About You", profile.aboutYou, isMultiline = true) { newAbout ->
                    profileViewModel.updateAboutYou(newAbout)
                }
                Text("Join Date: ${profile.joinDate}", Modifier.padding(8.dp))

                // If you want to display uploaded photos, uncomment the line below
                // UploadedPhotosSection(profileViewModel, showUploadedPhotos)


            }
        }
    }
}


@Composable
fun ProfileEditableField(label: String, value: String, isMultiline: Boolean = false, onUpdate: (String) -> Unit) {
    var text by remember { mutableStateOf(value) }
    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
    Button(
        onClick = { onUpdate(text) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Update $label")
    }
}

//@Composable
//fun UploadedPhotosSection(profileViewModel: ProfileViewModel, showUploadedPhotos: Boolean) {
//    // Button to show/hide uploaded photos
//    Button(onClick = { showUploadedPhotos = !showUploadedPhotos }) {
//        Text("Uploaded Photos")
//    }
//
//    // Display uploaded photos when button is clicked
//    if (showUploadedPhotos) {
//        Column {
//            profileViewModel.uploadedPhotos.forEach { photoUrl ->
//                ImageLoader(url = photoUrl, modifier = Modifier.fillMaxWidth().height(150.dp))
//            }
//        }
//    }
//}

//@Composable
//fun ProfileHeader(profile: Profile, image: Int, size: Int) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
////        ProfileImage(image = image, size = size)
//        Text(text = profile.fullName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
//        // You can add more Text composables here for other profile properties like location.
//    }
//}


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
