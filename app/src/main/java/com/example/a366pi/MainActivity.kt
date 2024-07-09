package com.example.a366pi

import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontFamily.Companion.Monospace
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a366pi.ui.theme._366piTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            _366piTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val pageState = remember { mutableIntStateOf(0) }
                    var username by remember { mutableStateOf("") }
                    var password by remember { mutableStateOf("") }
                    var name by remember { mutableStateOf("") }
                    var confirmPassword by remember { mutableStateOf("") }
                    var dob by remember { mutableStateOf("") }
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

                    var errorMessage by remember { mutableStateOf("") }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (pageState.value) {
                            0 -> {
                                var passwordChecker by remember {
                                    mutableStateOf("")
                                }
                                var usernameChecker by remember {
                                    mutableStateOf("")
                                }
                                Text(
                                    text = "Welcome to",
                                    fontSize = 45.sp,
                                )
                                Text(
                                    text = "366pi",
                                    fontSize = 45.sp,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )

                                Spacer(modifier = Modifier.height(28.dp))

                                // Username input
                                Text(
                                    text = "Username",
                                    fontSize = 24.sp
                                )
                                TextField(
                                    value = usernameChecker,
                                    onValueChange = { newUsername ->
                                        usernameChecker = newUsername
                                        errorMessage = ""
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text(text = "Enter your Username") }
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Password input
                                Text(
                                    text = "Password",
                                    fontSize = 24.sp
                                )
                                TextField(
                                    value = passwordChecker,
                                    onValueChange = { newPassword ->
                                        passwordChecker = newPassword
                                        errorMessage = ""
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text(text = "Enter your Password") },
                                    visualTransformation = PasswordVisualTransformation()
                                )

                                if (errorMessage.isNotEmpty()) {
                                    Text(
                                        text = errorMessage,
                                        color = Color.Red,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Row for Login and Sign Up buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Button(
                                        onClick = {
                                            if (usernameChecker.isEmpty() || passwordChecker.isEmpty()) {
                                                errorMessage = "Please enter your username and password"
                                            }
                                            else if (usernameChecker!=username || passwordChecker!=password) {
                                                errorMessage = "Please enter correct username and password"
                                            }
                                            else {
                                                pageState.value = 1
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(text = "Login")
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Button(
                                        onClick = { pageState.value = 2 },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(text = "Sign Up")
                                    }
                                }
                            }
                            1 -> {
                                // Welcome Screen
                                val configuration = LocalConfiguration.current
                                val screenWidth = configuration.screenWidthDp.dp
                                val imageSize = screenWidth / 3
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Hello, $name!",
                                            style = MaterialTheme.typography.headlineLarge.copy(fontFamily = FontFamily.Monospace),
                                            modifier = Modifier.weight(1f)
                                        )
                                        profilePhotoState.value?.let {
                                            Image(
                                                bitmap = it,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(imageSize)
                                                    .clip(CircleShape)
                                            )
                                        }
                                    }

                                    Row {
                                        Button(
                                            onClick = {},
                                            modifier = Modifier
                                                .weight(0.5f)
                                                .fillMaxWidth()
                                        ) {
                                            Text(text = username)
                                        }

                                        Spacer(modifier = Modifier.width(16.dp))

                                        Button(
                                            onClick = {},
                                            modifier = Modifier
                                                .weight(0.5f)
                                                .fillMaxWidth()
                                        ) {
                                            if (dob.isEmpty()){
                                                Text(text = "D.O.B")
                                            }
                                            else{
                                                Text(text = dob)
                                            }
                                        }
                                    }
                                    Row {
                                        Button(
                                            onClick = {
                                                pageState.value=0
                                            },
                                            modifier = Modifier
                                                .weight(0.5f)
                                                .fillMaxWidth()
                                        ) {
                                            Text(text = "Logout")
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color = Color.DarkGray)
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Write Your Important Notes Here",
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                            2 -> {
                                // SignUp Screen
                                errorMessage=""

                                Text(
                                    text = "Profile Photo",
                                    fontSize = 24.sp,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                profilePhotoState.value?.let { image ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            bitmap = image,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(128.dp)
                                                .clip(CircleShape)
                                        )
                                    }
                                }
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

                                Spacer(modifier = Modifier.height(16.dp))

                                Column(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = "Name",
                                        fontSize = 24.sp,
                                        modifier = Modifier
                                            .padding(start = 6.dp)
                                            .fillMaxWidth()
                                            .align(Alignment.Start)
                                    )
                                    TextField(
                                        value = name,
                                        onValueChange = { newName ->
                                            name = newName
                                            errorMessage = ""
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        label = { Text(text = "Enter your Name") }
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = "Username",
                                        fontSize = 24.sp,
                                        modifier = Modifier
                                            .padding(start = 6.dp)
                                            .fillMaxWidth()
                                            .align(Alignment.Start)
                                    )
                                    TextField(
                                        value = username,
                                        onValueChange = { newUsername ->
                                            username = newUsername
                                            errorMessage = ""
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        label = { Text(text = "Enter your Username") }
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Asking for password
                                    Text(
                                        text = "Password",
                                        fontSize = 24.sp,
                                        modifier = Modifier
                                            .padding(start = 6.dp)
                                            .fillMaxWidth()
                                            .align(Alignment.Start)
                                    )
                                    TextField(
                                        value = password,
                                        onValueChange = { newPassword ->
                                            password = newPassword
                                            errorMessage = ""
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        label = { Text(text = "Enter your Password") },
                                        visualTransformation = PasswordVisualTransformation()
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Asking for password confirmation
                                    Text(
                                        text = "Confirm Password",
                                        fontSize = 24.sp,
                                        modifier = Modifier
                                            .padding(start = 6.dp)
                                            .fillMaxWidth()
                                            .align(Alignment.Start)
                                    )
                                    TextField(
                                        value = confirmPassword,
                                        onValueChange = { newPassword ->
                                            confirmPassword = newPassword
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        label = { Text(text = "Enter your Password") }
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // DOB Picker
                                    Text(
                                        text = "Enter D.O.B",
                                        fontSize = 24.sp,
                                        modifier = Modifier
                                            .padding(start = 6.dp)
                                            .fillMaxWidth()
                                            .align(Alignment.Start)
                                    )
                                    TextField(
                                        value = dob,
                                        onValueChange = { newDob ->
                                            dob = newDob
                                            errorMessage = ""
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        label = { Text(text = "dd/mm/yyyy") }
                                    )

                                    if (errorMessage.isNotEmpty()) {
                                        Text(
                                            text = errorMessage,
                                            color = Color.Red,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }

                                    // vertical spacing
                                    Spacer(modifier = Modifier.height(24.dp))

                                    // sign up button
                                    Button(
                                        onClick = {
                                            if (name.isEmpty()){
                                                errorMessage = "Please enter the name"
                                            }
                                            else if (password.isEmpty()){
                                                errorMessage = "Password cannot be empty"
                                            }
                                            else if (confirmPassword != password) {
                                                errorMessage = "Please enter the same password in both sections"
                                            }
                                            else{
                                                pageState.value=1
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(text = "Sign Up")
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
