package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AgencyTab
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyRoyal

data class NavItem(
    val tab: AgencyTab,
    val icon: ImageVector,
    val label: String
)

@Composable
fun AgencyBottomNav(
    currentTab: AgencyTab,
    onTabSelected: (AgencyTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(AgencyTab.HOME, Icons.Default.Home, "Home"),
        NavItem(AgencyTab.JOBS, Icons.Default.Work, "Jobs"),
        NavItem(AgencyTab.APPLY, Icons.Default.Description, "Apply"),
        NavItem(AgencyTab.TRACK, Icons.Default.TrackChanges, "Track"),
        NavItem(AgencyTab.BOOK, Icons.Default.CalendarMonth, "Book"),
        NavItem(AgencyTab.PAY, Icons.Default.AccountBalanceWallet, "Pay"),
        NavItem(AgencyTab.CONTACT, Icons.Default.ContactPhone, "Contact")
    )

    NavigationBar(
        modifier = modifier,
        containerColor = NavyDeep,
        contentColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = currentTab == item.tab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NavyDeep,
                    selectedTextColor = GoldAccent,
                    indicatorColor = GoldAccent,
                    unselectedIconColor = Color(0xFF94A3B8),
                    unselectedTextColor = Color(0xFF94A3B8)
                ),
                modifier = Modifier.testTag("nav_item_${item.tab.name.lowercase()}")
            )
        }
    }
}
