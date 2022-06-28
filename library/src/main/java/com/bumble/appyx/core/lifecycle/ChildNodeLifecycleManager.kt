package com.bumble.appyx.core.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.coroutineScope
import com.bumble.appyx.core.children.ChildEntryMap
import com.bumble.appyx.core.children.nodeOrNull
import com.bumble.appyx.core.routing.RoutingSource
import com.bumble.appyx.core.withPrevious
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * Hosts [LifecycleRegistry] to manage the current node lifecycle
 * and updates lifecycle of children nodes when updated.
 */
internal class ChildNodeLifecycleManager<Routing>(
    private val lifecycle: Lifecycle,
    private val routingSource: RoutingSource<Routing, *>,
    private val children: StateFlow<ChildEntryMap<Routing>>,
    private val coroutineScope: CoroutineScope = lifecycle.coroutineScope,
) {

    fun launch() {
        updateChildrenWhenDestroyed()
        manageChildrenLifecycle()
        updateRemovedChildren()
    }

    private fun updateChildrenWhenDestroyed() {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                // lifecycle.coroutineScope is closed at this moment
                // need to clean up children as they are out of sync now
                children.value.values.forEach { child ->
                    child.nodeOrNull?.updateLifecycleState(Lifecycle.State.DESTROYED)
                }
            }
        })
    }

    private fun manageChildrenLifecycle() {
        coroutineScope.launch {
            val lifecycleState = lifecycle.asFlow()

            // observe both routingSource and children
            combine(
                lifecycleState,
                routingSource.visibilityState,
                children,
                ::Triple
            ).collect { (state, visibility, children) ->
                visibility.onScreen.forEach { element ->
                    val current = minOf(state, Lifecycle.State.RESUMED)
                    children[element.key]?.nodeOrNull?.updateLifecycleState(current)
                }
                visibility.offScreen.forEach { element ->
                    val current = minOf(state, Lifecycle.State.CREATED)
                    children[element.key]?.nodeOrNull?.updateLifecycleState(current)
                }
            }
        }
    }

    private fun updateRemovedChildren() {
        coroutineScope.launch {
            children
                .withPrevious()
                .collect { value ->
                    if (value.previous != null) {
                        val removedKeys = value.previous.keys - value.current.keys
                        removedKeys.forEach { key ->
                            val removedChild = value.previous[key]
                            removedChild?.nodeOrNull?.updateLifecycleState(Lifecycle.State.DESTROYED)
                        }
                    }
                }
        }
    }

}
