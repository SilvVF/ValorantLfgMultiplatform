package io.vallfg.valorantlfgmultiplatform.nav

import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription

sealed interface NavigationCommand {
    object NavigateUp : NavigationCommand
}


sealed interface DestinationsNavigationCommand : NavigationCommand {
    data class Push(val item: Screen): DestinationsNavigationCommand

    data class Replace(val item: Screen): DestinationsNavigationCommand

    data class ReplaceAll(val item: Screen): DestinationsNavigationCommand

    data class ReplaceList(val items: List<Screen>): DestinationsNavigationCommand

    object Pop: DestinationsNavigationCommand

    object PopAll: DestinationsNavigationCommand

    data class PopUntil(val predicate: (Screen) -> Boolean): DestinationsNavigationCommand

    data class NavigateToRoute(val route: Screen) : DestinationsNavigationCommand

    data class NavigateAndClearBackstack(val route: Screen): DestinationsNavigationCommand

    data class PopUpToRoute(val route: Screen, val inclusive: Boolean) :
        DestinationsNavigationCommand
}

abstract class Navigator {
    val navigationCommands = MutableSharedFlow<NavigationCommand>(extraBufferCapacity = Int.MAX_VALUE)

    // We use a StateFlow here to allow ViewModels to start observing navigation results before the initial composition,
    // and still get the navigation result later
    val navControllerFlow = MutableStateFlow<cafe.adriel.voyager.navigator.Navigator?>(null)

    fun navigateUp() {
        navigationCommands.tryEmit(NavigationCommand.NavigateUp)
    }
}

abstract class DestinationsNavigator : Navigator() {
    abstract fun navigate(route: Screen)

    abstract fun popUpTo(route: Screen, inclusive: Boolean)

    abstract fun navigateAndClearBackStack(route: Screen)

    abstract fun pop()

    abstract fun push(item: Screen)

    abstract fun push(items: List<Screen>)

    abstract fun replace(item: Screen)

    abstract fun replaceAll(item: Screen)

    abstract fun replaceAll(items: List<Screen>)

    abstract fun popAll()

    abstract fun popUntil(predicate: (Screen) -> Boolean)

    suspend fun handleNavigationCommands(navigator: cafe.adriel.voyager.navigator.Navigator) {
        navigationCommands
            .onSubscription { this@DestinationsNavigator.navControllerFlow.value = navigator  }
            .onCompletion { this@DestinationsNavigator.navControllerFlow.value = null }
            .collect { navigator.handleComposeNavigationCommand(it) }
    }

    private fun cafe.adriel.voyager.navigator.Navigator.handleComposeNavigationCommand(navigationCommand: NavigationCommand) {
        when (navigationCommand) {
            is DestinationsNavigationCommand.NavigateToRoute -> {
                 this.push(navigationCommand.route)
            }
            NavigationCommand.NavigateUp -> this.pop()
            is DestinationsNavigationCommand.PopUpToRoute -> this.popUntil {
                it == navigationCommand.route
            }
            is DestinationsNavigationCommand.NavigateAndClearBackstack -> this.replaceAll(navigationCommand.route)
            is DestinationsNavigationCommand.Push -> this.push(navigationCommand.item)
            DestinationsNavigationCommand.Pop -> this.pop()
            DestinationsNavigationCommand.PopAll -> this.popAll()
            is DestinationsNavigationCommand.PopUntil -> this.popUntil { navigationCommand.predicate(it) }
            is DestinationsNavigationCommand.Replace -> this.replace(navigationCommand.item)
            is DestinationsNavigationCommand.ReplaceAll -> this.replaceAll(navigationCommand.item)
            is DestinationsNavigationCommand.ReplaceList -> this.replaceAll(navigationCommand.items)
        }
    }

    fun canNavUp(stack: cafe.adriel.voyager.navigator.Navigator): Boolean = !stack.isEmpty
}

class LfgAppComposeNavigator : DestinationsNavigator() {

    override fun navigate(route: Screen) {
        navigationCommands.tryEmit(DestinationsNavigationCommand.NavigateToRoute(route))
    }

    override fun navigateAndClearBackStack(route: Screen) {
        navigationCommands.tryEmit(
            DestinationsNavigationCommand.NavigateAndClearBackstack(route)
        )
    }

    override fun pop() {
        navigationCommands.tryEmit(
            DestinationsNavigationCommand.Pop
        )
    }

    override fun push(item: Screen) {
        navigationCommands.tryEmit(
            DestinationsNavigationCommand.Push(item)
        )
    }

    override fun push(items: List<Screen>) {
        items.forEach { screen ->
            navigationCommands.tryEmit(
                DestinationsNavigationCommand.Push(screen)
            )
        }
    }

    override fun replace(item: Screen) {
        navigationCommands.tryEmit(
            DestinationsNavigationCommand.Replace(item)
        )
    }

    override fun replaceAll(item: Screen) {
        navigationCommands.tryEmit(
            DestinationsNavigationCommand.ReplaceAll(item)
        )
    }

    override fun replaceAll(items: List<Screen>) {
        navigationCommands.tryEmit(
            DestinationsNavigationCommand.ReplaceList(items)
        )
    }

    override fun popAll() {
        navigationCommands.tryEmit(
            DestinationsNavigationCommand.PopAll
        )
    }

    override fun popUntil(predicate: (Screen) -> Boolean) {
        navigationCommands.tryEmit(
            DestinationsNavigationCommand.PopUntil(predicate)
        )
    }

    override fun popUpTo(route: Screen, inclusive: Boolean) {
        navigationCommands.tryEmit(DestinationsNavigationCommand.PopUpToRoute(route, inclusive))
    }
}