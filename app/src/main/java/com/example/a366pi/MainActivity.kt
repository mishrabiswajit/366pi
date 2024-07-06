package com.example.a366pi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.a366pi.ui.theme._366piTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _366piTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    login()
                }
            }
        }
    }
}

@Composable
fun login(modifier: Modifier = Modifier) {
    val usernameState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
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
        Spacer(modifier = Modifier.height(16.dp))
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
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Handle Login */ },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Login")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { /* Handle Sign Up */ },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Sign Up")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    _366piTheme {
        login()
    }
}
