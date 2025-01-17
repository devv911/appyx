package com.bumble.appyx.navmodel.promoter.navmodel.operation

import com.bumble.appyx.navmodel.promoter.navmodel.Promoter
import com.bumble.appyx.navmodel.promoter.navmodel.PromoterElements
import com.bumble.appyx.core.navigation.RoutingElements
import kotlinx.parcelize.Parcelize

@Parcelize
class PromoteAll<T : Any> : PromoterOperation<T> {

    override fun isApplicable(elements: PromoterElements<T>): Boolean =
        true

    override fun invoke(
        elements: PromoterElements<T>,
    ): RoutingElements<T, Promoter.TransitionState> =
        elements.map {
            it.transitionTo(
                newTargetState = it.targetState.next(),
                operation = this
            )
        }
}

fun <T : Any> Promoter<T>.promoteAll() {
    accept(PromoteAll())
}
