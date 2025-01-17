package com.bumble.appyx.navmodel.spotlight

import com.bumble.appyx.core.navigation.Operation
import com.bumble.appyx.core.navigation.RoutingKey
import com.bumble.appyx.navmodel.spotlight.Spotlight.TransitionState
import com.bumble.appyx.navmodel.spotlight.operation.Routing

internal fun <T : Routing> spotlightElement(
    element: T,
    key: RoutingKey<T> = RoutingKey(routing = element),
    fromState: TransitionState,
    targetState: TransitionState,
    operation: Operation<T, TransitionState> = Operation.Noop()
) = SpotlightElement(
    key = key,
    fromState = fromState,
    targetState = targetState,
    operation = operation
)
