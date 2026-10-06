package com.example.groceryguard

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot

class BackendEngine : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun addGroceryItemToDatabase(itemName: String, itemPrice: Double) {
        val newItem = GroceryItem(
            name = itemName,
            price = itemPrice,
            category = "General",
            isPurchased = false
        )

        db.collection("shopping_list")
            .add(newItem)
            .addOnSuccessListener {
                updateBudgetCalculations(itemPrice)
            }
    }

    private fun updateBudgetCalculations(newPrice: Double) {
        val budgetDocument = db.collection("budgets").document("September_2026")

        // Explicitly typing document as DocumentSnapshot removes ambiguity errors
        budgetDocument.get().addOnSuccessListener { document: DocumentSnapshot? ->
            if (document != null && document.exists()) {
                val oldTotal = document.getDouble("totalSpent") ?: 0.0
                val budgetCap = document.getDouble("budgetCap") ?: 2500.0

                val newTotal = oldTotal + newPrice
                budgetDocument.update("totalSpent", newTotal)

                checkAlerts(newTotal, budgetCap)
            }
        }
    }

    private fun checkAlerts(spent: Double, cap: Double) {
        val percentage = if (cap > 0) (spent / cap) * 100 else 0.0

        if (percentage >= 90) {
            // Critical budget warning
        } else if (percentage >= 75) {
            // Standard budget warning
        }
    }
}

// Function to listen for real-time updates from the shopping list collection

private val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
fun fetchShoppingList(onUpdate: (List<GroceryItem>) -> Unit, onError: (Exception) -> Unit) {
    db.collection("shopping_list")
        .addSnapshotListener { snapshot: com.google.firebase.firestore.QuerySnapshot?, exception: com.google.firebase.firestore.FirebaseFirestoreException? ->
            if (exception != null) {
                onError(exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val itemList = snapshot.documents.mapNotNull { document ->
                    val name = document.getString("name") ?: ""
                    val price = document.getDouble("price") ?: 0.0
                    val category = document.getString("category") ?: "General"
                    val isPurchased = document.getBoolean("isPurchased") ?: false

                    GroceryItem(name = name, price = price, category = category, isPurchased = isPurchased)
                }
                onUpdate(itemList)
            }
        }
}
