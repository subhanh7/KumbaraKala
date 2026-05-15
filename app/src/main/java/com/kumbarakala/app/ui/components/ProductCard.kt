package com.kumbarakala.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kumbarakala.app.model.Product

@Composable
fun ProductCard(product: Product, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = product.name)
            Text(text = "$\${product.price}")
        }
    }
}
