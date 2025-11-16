package com.example.myapplication.ui1

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TisenseHeader(onMenuClick: () -> Unit) {
    val currentDate = remember {
        SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Home", fontSize = 34.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))

                Image(
                    painter = painterResource(id = R.drawable.tisense_icon_2),
                    contentDescription = "Logo",
                    modifier = Modifier.size(92.dp)
                )
            }
            Text(text = currentDate, fontSize = 14.sp, color = Color.Gray)
        }
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TisenseTopAppBar() {
    var menuExpanded by remember { mutableStateOf(false) }
    val menuItems = listOf(
        DropdownItem("Focus", backgroundColor = Color(0xFFE8F5E9), textColor = Color(0xFF2E7D32)),
        DropdownItem("Breakroom", backgroundColor = Color(0xFFE3F2FD), textColor = Color(0xFF1565C0)),
        DropdownItem("Insights", backgroundColor = Color(0xFFFFF8E1), textColor = Color(0xFFF9A825)),
        DropdownItem("Planner", backgroundColor = Color(0xFFF3E5F5), textColor = Color(0xFF6A1B9A)),
        DropdownItem("Home", backgroundColor = Color(0xFFFFEBEE), textColor = Color(0xFFC62828)),
        DropdownItem("Menu", backgroundColor = Color(0xFFECEFF1), textColor = Color(0xFF37474F))
    )

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Tisense",
                    style = MaterialTheme.typography.headlineMedium
                )
                Icon(
                    painter = painterResource(id = R.drawable.tisense_icon_2),
                    contentDescription = "App Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(54.dp)
                )
            }
        },
        actions = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.size(28.dp)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(item.backgroundColor)
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(item.text, color = item.textColor)
                                }
                            },
                            onClick = {
                                Log.d("LogScreen", "${item.text} clicked")
                                menuExpanded = false
                            }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}
