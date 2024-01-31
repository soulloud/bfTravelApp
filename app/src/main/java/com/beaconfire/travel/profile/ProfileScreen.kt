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
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                profileUiModel.profile?.let { profile ->
                    item {
                        // Display profile image
                        ImageLoader(
                            url = profile.photoImage,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Text("Full Name: ${profile.fullName}", Modifier.padding(8.dp))
                        Text("Location: ${profile.location}", Modifier.padding(8.dp))
                        Text("About You: ${profile.aboutYou}", Modifier.padding(8.dp))
                        Text("Join Date: ${profile.joinDate}", Modifier.padding(8.dp))

                        // Button to show/hide uploaded photos
                        Button(onClick = { showUploadedPhotos = !showUploadedPhotos }) {
                            Text("Uploaded Photos")
                        }
                        // LazyColumn to display uploaded photos
                        if (showUploadedPhotos) {
                            LazyColumn {
                                items(profile.uploadedPhotos) { photoUrl ->
                                    ImageLoader(
                                        url = photoUrl,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
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
