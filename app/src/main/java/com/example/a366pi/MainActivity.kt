package com.example.a366pi

// Importing Packages
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a366pi.ui.theme._366piTheme
import java.util.*


// Driver Code
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            _366piTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController) }
                        composable("home/{username}") { backStackEntry ->
                            HomeScreen(username = backStackEntry.arguments?.getString("username"))
                        }
                        composable("signup") { SignUpScreen(navController) }
                    }
                }
            }
        }
    }
}

// Login Page
@Composable
fun LoginScreen(navController: NavHostController) {

    val usernameState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    // User Inputs
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        // username input
        Text(
            text = "Username",
            fontSize = 36.sp
        )
        TextField(
            value = usernameState.value,
            onValueChange = { newUsernameState ->
                usernameState.value = newUsernameState
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Enter your Username") }
        )

        // horizontal spacing
        Spacer(modifier = Modifier.height(16.dp))

        // password input
        Text(
            text = "Password",
            fontSize = 36.sp
        )
        TextField(
            value = passwordState.value,
            onValueChange = { newPasswordState ->
                passwordState.value = newPasswordState
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Enter your Password") }
        )

        // horizontal spacing
        Spacer(modifier = Modifier.height(24.dp))

        //  Row under Column
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // Login Button
            Button(
                onClick = {
                    // Navigate to the home screen and pass the username
                    navController.navigate("home/${usernameState.value}")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Login")
            }

            // Vertical Spacing
            Spacer(modifier = Modifier.width(16.dp))

            // Sign up button
            Button(
                onClick = {
                    // Navigate to the sign-up screen
                    navController.navigate("signup")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Sign Up")
            }
        }
    }
}

// Home Screen Page with Greeting as per username
@Composable
fun HomeScreen(username: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome, $username!",
            fontSize = 36.sp
        )
    }
}

// Signup Page
@Composable
fun SignUpScreen(navController: NavHostController) {
    val nameState = remember { mutableStateOf("") }
    val dobState = remember { mutableStateOf(TextFieldValue("")) }
    val profilePhotoState = remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current

    // Image Picker
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            profilePhotoState.value = bitmap.asImageBitmap()
        }
    }

    // Camera Option
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            profilePhotoState.value = it.asImageBitmap()
        }
    }

    // date picker
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            dobState.value = TextFieldValue("$selectedDay/${selectedMonth + 1}/$selectedYear")
//            TextFieldValue(dobState.value) = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        }, year, month, day
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Name Input
        Text(
            text = "Name",
            fontSize = 24.sp
        )
        TextField(
            value = nameState.value,
            onValueChange = { newNameState ->
                nameState.value = newNameState
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Enter your Name") }
        )

        // vertical spacing
        Spacer(modifier = Modifier.height(16.dp))

        // DOB picker
        Text(
            text = "Date of Birth",
            fontSize = 24.sp
        )
        TextField(
            value = dobState.value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    datePickerDialog.show()
                },
            label = { Text(text = "Enter your Date of Birth") },
            readOnly = true
        )

        // vertical spacing
        Spacer(modifier = Modifier.height(16.dp))

        // profile photo section
        Text(
            text = "Profile Photo",
            fontSize = 24.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { pickImageLauncher.launch("image/*") }) {
                Text(text = "Gallery")
            }
            Button(onClick = { takePictureLauncher.launch(null) }) {
                Text(text = "Camera")
            }
        }
        profilePhotoState.value?.let { image ->
            Image(
                bitmap = image,
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .padding(16.dp)
            )
        }

        // vertical spacing
        Spacer(modifier = Modifier.height(24.dp))

        // sign up button
        Button(
            onClick = {
                // redirecting to home screen
                navController.navigate("home/${nameState.value}")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign Up")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    _366piTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "login") {
            composable("login") { LoginScreen(navController) }
            composable("home/{username}") { backStackEntry ->
                HomeScreen(username = backStackEntry.arguments?.getString("username"))
            }
            composable("signup") { SignUpScreen(navController) }
        }
    }
}
