
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*


import com.example.todolistmedfungerandegit.ui.theme.TodoListMedFungerandeGITTheme



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
    val painter = painterResource(id = R.drawable.bakgrund2)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),


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

                        ) {
                            Text(task.name)
                        }
                    },

                    supportingContent = {
                        Box(
                            modifier = Modifier

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
    var errorMessages by remember { mutableStateOf(emptyList<String>()) }


    fun validateInput(name: String, details: String): List<String> {
        val errors = mutableListOf<String>()
        if (name.length < 3) {
            errors.add("Task name must be at least 3 characters long.")
        }

        if (details.length > 120) {
            errors.add("Task details must be at most 120 characters long.")
        }
        return errors
    }

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
            // Display error messages
            if (errorMessages.isNotEmpty()) {
                for (error in errorMessages) {
                    Text(error, color = Color.Red)
                }
            }

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
                    val errors = validateInput(name, details)
                    if (errors.isEmpty()) {
                        taskList.add(Task(id = taskList.size + 1, name = name, details = details))
                        navController.popBackStack()
                    } else {
                        errorMessages = errors
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
    var errorMessages by remember { mutableStateOf(emptyList<String>()) }


    fun validateInput(name: String, details: String): List<String> {
        val errors = mutableListOf<String>()
        if (name.length < 3) {
            errors.add("Task name must be at least 3 characters long.")
        }

        if (details.length > 120) {
            errors.add("Task details must be at most 120 characters long.")
        }
        return errors
    }

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
            // Display error messages
            if (errorMessages.isNotEmpty()) {
                for (error in errorMessages) {
                    Text(error, color = Color.Red)
                }
            }

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
                val errors = validateInput(name, details)
                if (errors.isEmpty()) {
                    task.name = name
                    task.details = details
                    navController.popBackStack()
                } else {
                    errorMessages = errors
                }
            }) {
                Text("Save Changes")
            }
        }
    }
}


