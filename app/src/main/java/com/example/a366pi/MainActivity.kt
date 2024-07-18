package com.example.a366pi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a366pi.ui.theme._366piTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
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
                    var passwordVisible by remember { mutableStateOf(false) }
                    var name by remember { mutableStateOf("") }
                    var confirmPassword by remember { mutableStateOf("") }
                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()
                    val db = DatabaseBuilder.getInstance(context)
                    val userDao = db.userDao()
                    var errorMessage by remember { mutableStateOf("") }
                    val namePattern = Regex("^[a-zA-Z ]*$")


                    LaunchedEffect(errorMessage) {
                        if (errorMessage.isNotEmpty()) {
                            delay(3000)
                            errorMessage = ""
                        }
                    }

                    when (pageState.intValue) {

                        // LoginPage
                        0 -> {
                            var passwordChecker by remember { mutableStateOf("") }
                            var usernameChecker by remember { mutableStateOf("") }

                            // loginPage - column
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {

                                // loginPage - column - welcomeText
                                Text(text = "Welcome to", fontSize = 45.sp)
                                Text(
                                    text = "366pi",
                                    fontSize = 45.sp,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(28.dp))

                                // loginPage - column - username
                                OutlinedTextField(
                                    value = usernameChecker,
                                    onValueChange = { newUsername ->
                                        usernameChecker = newUsername
                                        errorMessage = ""
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text(text = "Enter your Username") }
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                // loginPage - column - password
                                OutlinedTextField(
                                    value = passwordChecker,
                                    onValueChange = { newPassword ->
                                        passwordChecker = newPassword
                                        errorMessage = ""
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text(text = "Enter your Password") },
                                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    trailingIcon = {
                                        val image =
                                            if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                        val description =
                                            if (passwordVisible) "Hide password" else "Show password"

                                        IconButton(onClick = {
                                            passwordVisible = !passwordVisible
                                        }) {
                                            Icon(
                                                imageVector = image,
                                                contentDescription = description
                                            )
                                        }
                                    }
                                )

                                // loginPage - column - errorMessage
                                if (errorMessage.isNotEmpty()) {
                                    Text(
                                        text = errorMessage,
                                        color = Color.Red,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(24.dp))

                                // loginPage - column - row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    // loginPage - column - row - loginButton
                                    Button(
                                        onClick = {
                                            if (usernameChecker.isEmpty()) {
                                                errorMessage = "Please enter your username"
                                            } else if (passwordChecker.isEmpty()) {
                                                errorMessage = "Please enter your password"
                                            } else {
                                                scope.launch {
                                                    val user =
                                                        userDao.getUserByUsername(usernameChecker)
                                                    if (user == null) {
                                                        errorMessage = "User does not exist"
                                                    } else if (user.password != passwordChecker) {
                                                        errorMessage = "Incorrect password"
                                                    } else {
                                                        username = user.username
                                                        name = user.name
                                                        pageState.intValue = 1
                                                    }
                                                }
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(text = "Login")
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))

                                    // loginPage - column - row - signupButton
                                    Button(
                                        onClick = { pageState.intValue = 2 },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(text = "Sign Up")
                                    }
                                }
                            }
                        }

                        // Welcome Screen
                        1 -> {

                            // welcome - column
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                // welcome - column - row1 - text
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                                ) {
                                    Text(
                                        text = "Hello, $name!",
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontFamily = FontFamily.Monospace,
                                            color = MaterialTheme.colorScheme.primary
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(24.dp))

                                // welcome - column - row2
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {

                                    // welcome - column - row2 - button1
                                    Button(
                                        onClick = { },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(8.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        elevation = ButtonDefaults.buttonElevation(6.dp)
                                    ) {
                                        Text(text = "My Profile")
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))

                                    // welcome - column - row2 - button2
                                    Button(
                                        onClick = { },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(8.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        elevation = ButtonDefaults.buttonElevation(6.dp)
                                    ) {
                                        Text(text = "@$username")
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))

                                // welcome - column - row3
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {

                                    // welcome - column - row3 - logoutButton
                                    Button(
                                        onClick = {pageState.intValue = 0},
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start=8.dp, end = 8.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                                        elevation = ButtonDefaults.buttonElevation(6.dp)
                                    ) {
                                        Text(text = "Logout", color = Color.White)
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))

                                // welcome - column - card
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    colors = CardDefaults.cardColors(Color(0xFFFFE5B4)) // Peach color
                                ) {

                                    // welcome - column - card - box [notes section]
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.padding(16.dp)
                                    ) {

                                        // welcome - column - card - box - text
                                        Text(
                                            text = "Write Your Important Notes Here",
                                            color = Color(0xFF4A4A4A),
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                                        )
                                    }
                                }
                            }
                        }

                        2 -> {

                            // Signup
                            Scaffold(

                                // Signup - topBar
                                topBar = {
                                    TopAppBar(
                                        title = {

                                            // Signup - topBar - row
                                            Row(verticalAlignment = Alignment.CenterVertically) {

                                                // Signup - topBar - row - icon
                                                Icon(
                                                    painter = painterResource(id = R.drawable.logo),
                                                    contentDescription = "App Logo",
                                                    tint = Color.Unspecified,
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .clip(CircleShape)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))

                                                // Signup - topBar - row - text
                                                Text("366pi", color = Color.White)
                                            }
                                        },
                                        colors = TopAppBarDefaults.topAppBarColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            titleContentColor = Color.White
                                        )
                                    )
                                }
                            ) { paddingValues ->
                                // Signup - column
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(paddingValues)
                                ) {
                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Signup - column - row
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    ) {

                                        // Signup - column - row - askName
                                        OutlinedTextField(
                                            value = name,
                                            onValueChange = {
                                                scope.launch {
                                                    if (namePattern.matches(it)) {
                                                        name = it
                                                    } else {
                                                        errorMessage =
                                                            "Name cannot contain special characters or numbers"
                                                    }
                                                }
                                            },
                                            label = { Text("Full Name") },
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))

                                        // Signup - column - row - askUsername
                                        OutlinedTextField(
                                            value = username,
                                            onValueChange = { newUsername ->
                                                username = newUsername
                                            },
                                            label = { Text("Username") },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Signup - column - askPassword
                                    OutlinedTextField(
                                        value = password,
                                        onValueChange = { newPassword -> password = newPassword },
                                        label = { Text("Password") },
                                        visualTransformation = PasswordVisualTransformation(),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Signup - column - askConfirmPassword
                                    OutlinedTextField(
                                        value = confirmPassword,
                                        onValueChange = { newConfirmPassword ->
                                            confirmPassword = newConfirmPassword
                                        },
                                        label = { Text("Confirm Password") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Signup - column - errorMessage
                                    if (errorMessage.isNotEmpty()) {
                                        Text(
                                            text = errorMessage,
                                            color = Color.Red,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))

                                    // Signup - column - box
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        // Signup - column - box - row
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            // Signup - column - box - row - box1
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(end = 8.dp) // Adjust the padding as needed
                                            ) {

                                                // Signup - column - box - row - box1 - loginButton
                                                Button(
                                                    onClick = {
                                                        pageState.intValue = 0
                                                    },
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(text = "Login")
                                                }
                                            }

                                            // Signup - column - box - row - box2
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(start = 8.dp)
                                            ) {

                                                // Signup - column - box - row - box2 - signupButton
                                                Button(
                                                    onClick = {
                                                        scope.launch {
                                                            if (name.isEmpty()) {
                                                                errorMessage = "Name can't be empty"
                                                                return@launch
                                                            }
                                                            else if (username.isEmpty()) {
                                                                errorMessage = "Username can't be empty"
                                                                return@launch
                                                            }
                                                            else if (password.isEmpty()) {
                                                                errorMessage = "Password can't be empty"
                                                                return@launch
                                                            }
                                                            else if (password != confirmPassword) {
                                                                errorMessage =
                                                                    "Passwords do not match"
                                                                return@launch
                                                            }

                                                            val newUser = User(
                                                                username = username,
                                                                name = name,
                                                                password = password
                                                            )
                                                            try {
                                                                userDao.insertUser(newUser)
                                                                pageState.intValue = 1
                                                                errorMessage = ""
                                                            } catch (e: Exception) {
                                                                errorMessage =
                                                                    "Username already exists"
                                                            }
                                                        }
                                                    },
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text("Sign Up")
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
        }
    }
}
