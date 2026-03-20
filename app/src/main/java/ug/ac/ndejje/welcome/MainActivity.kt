package ug.ac.ndejje.welcome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.welcome.ui.theme.Ndejje_Welcome_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ndejje_Welcome_AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StudentDirectory()
                }
            }
        }
    }
}

@Composable
fun StudentInfo(student: Student) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(student.profileImageId),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = student.name,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = student.regNumber,
            color = Color.Red
        )
        if (student.isVerified) {
            Text("Verified Student",color = Color(0xFF4CAF50))
        }
    }
}

@Composable
fun StudentIdCard(student: Student) {
    //Attendance logic State for button text and border
    var isPresent by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            //Use Modifier.border because ElevatedCard doesn't have a border parameter
            .border(
                width = if (isPresent) 3.dp else 0.dp,
                color = if (isPresent) Color.Green else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isPresent) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surfaceVariant.copy(0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StudentInfo(student)

            Spacer(modifier = Modifier.height(8.dp))

            //Button text and color change logic
            Button(
                onClick = { isPresent = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPresent) Color.Green else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = if (isPresent) "Present" else "Mark Present")
            }
        }
    }
}

@Composable
fun StudentDirectory() {
    //Search functionality State
    var searchQuery by remember { mutableStateOf("") }

    // Logic to filter the students based on the search input
    val filteredStudents = StudentProvider.studentList.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        //TextField for search at the top
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Student Name...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        LazyColumn(
            contentPadding =PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)// Use start, top, end, bottom if you want different values for specific sides
        ) {
            items(filteredStudents) { student ->
                StudentIdCard(student = student)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomePreview() {
    Ndejje_Welcome_AppTheme {
        Surface {
            StudentDirectory()
        }
    }
}