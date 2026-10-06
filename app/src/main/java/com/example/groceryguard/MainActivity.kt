package com.example.groceryguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.groceryguard.ui.theme.GroceryGuardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Call the function here so it runs when the app launches
        initializeDefaultBudget()

        enableEdgeToEdge()
        setContent {
            GroceryGuardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(paddingValues = innerPadding)
                    )
                }
            }
        }
    }

    // 2. Define the function here, outside onCreate but inside MainActivity
    private fun initializeDefaultBudget() {
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

        val initialBudget = mapOf(
            "budgetCap" to 2500.0,
            "totalSpent" to 0.0,
            "monthYear" to "September 2026"
        )

        db.collection("budgets").document("September_2026")
            .set(initialBudget)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GroceryGuardTheme {
        Greeting("Android")
    }
}