package dev.yash.keymanager.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Key
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavDestinations(val title: String, val icon: ImageVector? = null, val route: String) {
    object AuthScreen : NavDestinations("Authentication", null, "authentication")
    object HomeScreen : NavDestinations("Home", null, "home")
    object SshScreen : NavDestinations("SSH", Icons.TwoTone.Key, "ssh")
    object GpgScreen : NavDestinations("GPG", Icons.TwoTone.Lock, "gpg")
}
