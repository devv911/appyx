package com.bumble.appyx.navmodel.tiles.operation

import com.bumble.appyx.core.navigation.RoutingElements
import com.bumble.appyx.core.navigation.RoutingKey
import com.bumble.appyx.navmodel.tiles.Tiles
import com.bumble.appyx.navmodel.tiles.TilesElement
import com.bumble.appyx.navmodel.tiles.TilesElements
import com.bumble.appyx.navmodel.tiles.Tiles.TransitionState.CREATED
import com.bumble.appyx.navmodel.tiles.Tiles.TransitionState.STANDARD
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Add<T : Any>(
    private val element: @RawValue T
) : TilesOperation<T> {

    override fun isApplicable(elements: TilesElements<T>): Boolean = true

    override fun invoke(
        elements: TilesElements<T>,
    ): RoutingElements<T, Tiles.TransitionState> =
        elements + TilesElement(
            key = RoutingKey(element),
            fromState = CREATED,
            targetState = STANDARD,
            operation = this
        )
}

fun <T : Any> Tiles<T>.add(element: T) {
    accept(Add(element))
}
