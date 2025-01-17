package com.bumble.appyx.navmodel.spotlight

import com.bumble.appyx.core.navigation.BaseNavModel
import com.bumble.appyx.core.navigation.backpresshandlerstrategies.BackPressHandlerStrategy
import com.bumble.appyx.core.navigation.onscreen.OnScreenStateResolver
import com.bumble.appyx.core.navigation.operationstrategies.ExecuteImmediately
import com.bumble.appyx.core.navigation.operationstrategies.OperationStrategy
import com.bumble.appyx.core.state.SavedStateMap
import com.bumble.appyx.navmodel.spotlight.Spotlight.TransitionState
import com.bumble.appyx.navmodel.spotlight.backpresshandler.GoToDefault
import com.bumble.appyx.navmodel.spotlight.operation.toSpotlightElements

class Spotlight<Routing : Any>(
    items: List<Routing>,
    initialActiveIndex: Int = 0,
    savedStateMap: SavedStateMap?,
    key: String = KEY_NAV_MODEL,
    backPressHandler: BackPressHandlerStrategy<Routing, TransitionState> = GoToDefault(
        initialActiveIndex
    ),
    operationStrategy: OperationStrategy<Routing, TransitionState> = ExecuteImmediately(),
    screenResolver: OnScreenStateResolver<TransitionState> = SpotlightOnScreenResolver
) : BaseNavModel<Routing, TransitionState>(
    backPressHandler = backPressHandler,
    operationStrategy = operationStrategy,
    screenResolver = screenResolver,
    finalState = null,
    savedStateMap = savedStateMap,
    key = key,
) {

    enum class TransitionState {
        INACTIVE_BEFORE, ACTIVE, INACTIVE_AFTER;
    }

    override val initialElements = items.toSpotlightElements(initialActiveIndex)

}
