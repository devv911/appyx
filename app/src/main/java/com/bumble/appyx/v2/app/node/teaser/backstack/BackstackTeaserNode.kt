package com.bumble.appyx.v2.app.node.teaser.backstack

import android.os.Parcelable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.lifecycle.coroutineScope
import com.bumble.appyx.v2.app.node.child.GenericChildNode
import com.bumble.appyx.v2.app.node.teaser.backstack.BackstackTeaserNode.Routing
import com.bumble.appyx.v2.app.node.teaser.backstack.transitionhandler.rememberCustomHandler
import com.bumble.appyx.v2.core.composable.Children
import com.bumble.appyx.v2.core.modality.BuildContext
import com.bumble.appyx.v2.core.node.Node
import com.bumble.appyx.v2.core.node.ParentNode
import com.bumble.appyx.v2.core.routing.source.backstack.BackStack
import com.bumble.appyx.v2.core.routing.source.backstack.operation.pop
import com.bumble.appyx.v2.core.routing.source.backstack.operation.push
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@ExperimentalUnitApi
class BackstackTeaserNode(
    buildContext: BuildContext,
    private val backStack: BackStack<Routing> = BackStack(
        initialElement = Routing.Child(100),
        savedStateMap = buildContext.savedStateMap
    ),
) : ParentNode<Routing>(
    buildContext = buildContext,
    routingSource = backStack
) {

    init {
        lifecycle.coroutineScope.launch {
            delay(1000)
            repeat(4) {
                backStack.push(Routing.Child((it + 2) * 100))
                delay(400)
            }
            delay(500)
            repeat(4) {
                backStack.pop()
                delay(150)
            }
            delay(1000)
            finish()
        }
    }

    sealed class Routing : Parcelable {
        @Parcelize
        data class Child(val int: Int = Random.nextInt(1000)) : Routing()
    }

    override fun resolve(routing: Routing, buildContext: BuildContext): Node =
        when (routing) {
            is Routing.Child -> GenericChildNode(buildContext, routing.int)
        }

    @Composable
    override fun View(modifier: Modifier) {
        Children(
            modifier = Modifier.fillMaxSize(),
            routingSource = backStack,
            transitionHandler = rememberCustomHandler()
        )
    }
}
