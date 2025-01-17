package com.bumble.appyx.navmodel.promoter.navmodel

import com.bumble.appyx.navmodel.promoter.navmodel.Promoter.TransitionState
import com.bumble.appyx.navmodel.promoter.navmodel.Promoter.TransitionState.CREATED
import com.bumble.appyx.navmodel.promoter.navmodel.Promoter.TransitionState.DESTROYED
import com.bumble.appyx.navmodel.promoter.navmodel.Promoter.TransitionState.STAGE1
import com.bumble.appyx.core.navigation.BaseNavModel
import com.bumble.appyx.core.navigation.Operation.Noop
import com.bumble.appyx.core.navigation.RoutingKey

class Promoter<T : Any>(
    initialItems: List<T> = listOf(),
) : BaseNavModel<T, TransitionState>(
    screenResolver = PromoterOnScreenResolver,
    finalState = DESTROYED,
    savedStateMap = null
) {

    enum class TransitionState {
        CREATED, STAGE1, STAGE2, STAGE3, STAGE4, SELECTED, DESTROYED;

        fun next(): TransitionState =
            when (this) {
                CREATED -> STAGE1
                STAGE1 -> STAGE2
                STAGE2 -> STAGE3
                STAGE3 -> STAGE4
                STAGE4 -> SELECTED
                SELECTED -> DESTROYED
                DESTROYED -> DESTROYED
            }
    }

    override val initialElements = initialItems.map {
        PromoterElement(
            key = RoutingKey(it),
            fromState = CREATED,
            targetState = STAGE1,
            operation = Noop()
        )
    }
}
