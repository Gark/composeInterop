package com.example.composeandroidview

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composeandroidview.ui.theme.ComposeAndroidViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAndroidViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainView()
                }
            }
        }
    }
}

@Composable
fun MainView() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController) }
    )
    { BottomBarMain(navController) }
}

sealed class Screen(val route: String, val title: String, @DrawableRes val icon: Int) {
    object One : Screen(route = "one", title = "one", icon = R.drawable.ic_launcher_foreground)
    object Two : Screen(route = "two", title = "two", icon = R.drawable.ic_launcher_foreground)
}

@Composable
fun BottomBarMain(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.One.route) {
        composable(Screen.One.route) {
            ScreenOne()
        }
        composable(Screen.Two.route) {
            ScreenTwo()
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(Screen.One, Screen.Two)

    BottomNavigation(elevation = 5.dp) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.map {
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = it.title
                    )
                },
                label = {
                    Text(
                        text = it.title,
                    )
                },
                selected = currentRoute == it.route,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(alpha = 0.4f),
                onClick = {
                    navController.navigate(it.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun ScreenOne() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = Screen.One.title,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.align(Alignment.Center),
            fontSize = 20.sp
        )
    }
}

@Composable
fun ScreenTwo() {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                MyCustomView(context)
            },
        )
    }
}

class MyCustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {
    init {
        text = "MyCustomView"
    }
}