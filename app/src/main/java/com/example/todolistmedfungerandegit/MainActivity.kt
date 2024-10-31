
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.todolistmedfungerandegit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.todolistmedfungerandegit.ui.theme.TodoListMedFungerandeGITTheme
import androidx.compose.foundation.background


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListMedFungerandeGITTheme {
                TaskManagerApp()
            }
        }
    }
}

data class Task(
    val id: Int,
    var name: String,
    var details: String,
    var isChecked: MutableState<Boolean> = mutableStateOf(false)
)
@Composable
fun BackgroundImage() {
    val painter = painterResource(id = R.drawable.bakgrunden) // Ensure this resource is correct
    Box(
        modifier = Modifier.fillMaxSize() // Fill the entire screen
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(), // Ensure the image fills the box
            contentScale = ContentScale.Crop // Adjust image scaling
        )
    }
}


@Composable
fun TaskManagerApp() {
    val navController = rememberNavController()
    val taskList = remember { mutableStateListOf<Task>() }
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()


        NavHost(navController = navController, startDestination = "taskList") {
            composable("taskList") { TaskListScreen(navController, taskList) }
            composable("createTask") { CreateTaskScreen(navController, taskList) }
            composable("editTask/{taskId}") { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
                val task = taskList.find { it.id == taskId }
                task?.let { TaskEditScreen(navController, it) }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, taskList: MutableList<Task>) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Tasks")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Blue // Change to your desired color
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("createTask") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(taskList) { task ->
                ListItem(
                    headlineContent = {
                        Box(
                            modifier = Modifier
                                .background(Color.Yellow) // Set your desired background color here
                                .padding(8.dp) // Optional: Add some padding
                        ) {
                            Text(task.name)
                        }
                    },
                    supportingContent = {
                        Box(
                            modifier = Modifier
                                .background(Color.LightGray) // Set background color for details
                                .padding(8.dp) // Optional: Add padding
                        ) {
                            Text(task.details)
                        }
                    },
                    leadingContent = {
                        Checkbox(
                            checked = task.isChecked.value,
                            onCheckedChange = { isChecked ->
                                task.isChecked.value = isChecked
                            }
                        )
                    },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { navController.navigate("editTask/${task.id}") }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Edit Task")
                            }
                            IconButton(onClick = { taskList.remove(task) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete Task")
                            }
                        }
                    }
                )
                Divider()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(navController: NavHostController, taskList: MutableList<Task>) {
    var name by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("New Task") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Task Name") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = details,
                onValueChange = { details = it },
                label = { Text("Task Details") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (name.isNotBlank() && details.isNotBlank()) {
                        taskList.add(Task(id = taskList.size, name = name, details = details))
                        navController.popBackStack() // Navigate back after adding
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Task")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(navController: NavController, task: Task) {
    var name by remember { mutableStateOf(task.name) }
    var details by remember { mutableStateOf(task.details) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Task") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Task Name") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = details,
                onValueChange = { details = it },
                label = { Text("Task Details") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (name.isNotBlank() && details.isNotBlank()) {
                    task.name = name
                    task.details = details
                    navController.popBackStack()
                }
            }) {
                Text("Save Changes")
            }
        }
    }
}

