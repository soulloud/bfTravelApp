package com.beaconfire.travel.register

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.beaconfire.travel.R
import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.repo.model.City
import com.beaconfire.travel.repo.model.State
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    onNavigate: (Navigation) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf(State.INVALID_STATE) }
    var selectedCity by remember { mutableStateOf(City.INVALID_CITY) }
    var passwordVisibility by remember { mutableStateOf(false) }
    val registerUiModel by registerViewModel.registerUiModel.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp),
            painter = painterResource(id = R.drawable.ic_register_background),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = "Register",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = "Please enter details to register",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Display Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image =
                    if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(image, "Toggle password visibility")
                }
            }
        )
        DropdownForStateAndCity(registerViewModel) { state, city ->
            selectedState = state
            selectedCity = city
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                registerViewModel.register(
                    email,
                    displayName,
                    password,
                    selectedState,
                    selectedCity
                )
            }) {
            Text(
                modifier = Modifier
                    .width(128.dp)
                    .wrapContentSize(Alignment.Center),
                text = "Register"
            )
        }
        Button(onClick = { onNavigate(Navigation.Login) }) {
            Text(
                modifier = Modifier
                    .width(128.dp)
                    .wrapContentSize(Alignment.Center),
                text = "Back to Login"
            )
        }

        when (registerUiModel.registerStatus) {
            RegisterStatus.LoadingStates -> {
                Text(text = "Loading States!")
            }

            RegisterStatus.LoadingCities -> {
                Text(text = "Loading Cities!")
            }

            RegisterStatus.RegistrationSuccess -> {
                onNavigate(Navigation.Login)
            }

            else -> {}
        }

        val context = LocalContext.current
        LaunchedEffect(null) {
            registerViewModel.errorMessage.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DropdownForStateAndCity(
    registerViewModel: RegisterViewModel,
    onCitySelected: (State, City) -> Unit,
) {
    var selectedState by remember { mutableStateOf(State.INVALID_STATE) }
    var selectedCity by remember { mutableStateOf(City.INVALID_CITY) }
    val scope = rememberCoroutineScope()
    var expandedState by remember { mutableStateOf(false) }
    var expandedCity by remember { mutableStateOf(false) }
    val registerUiModel by registerViewModel.registerUiModel.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))
        Box {
            DropdownMenu(
                expanded = expandedState,
                onDismissRequest = { expandedState = false },
                modifier = Modifier.padding(16.dp)
            ) {
                registerUiModel.states.forEach { state ->
                    DropdownMenuItem(
                        text = { Text(state.state) },
                        onClick = {
                            selectedState = state
                            expandedState = false
                            selectedCity = City.INVALID_CITY

                            scope.launch {
                                registerViewModel.getAllCitiesForStates(selectedState)
                            }
                        })
                }
            }

            OutlineText(title = "State", selectedOption = selectedState.state) {
                expandedState = !expandedState
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box {
            DropdownMenu(
                expanded = expandedCity,
                onDismissRequest = { expandedCity = false },
                modifier = Modifier.padding(16.dp)
            ) {
                registerUiModel.cities.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(city.city) },
                        onClick = {
                            selectedCity = city
                            expandedCity = false
                            onCitySelected(selectedState, selectedCity)
                        })
                }
            }

            OutlineText(title = "City", selectedOption = selectedCity.city) {
                expandedCity = !expandedCity
            }
        }
    }
}

@Composable
private fun OutlineText(
    title: String,
    selectedOption: String,
    onClick: () -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.clickable { onClick() },
        value = selectedOption,
        onValueChange = {},
        label = { Text(text = title) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown, contentDescription = null
            )
        },
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = Color.Transparent,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
