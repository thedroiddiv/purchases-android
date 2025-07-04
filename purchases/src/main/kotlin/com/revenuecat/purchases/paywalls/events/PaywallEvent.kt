package com.revenuecat.purchases.paywalls.events

import com.revenuecat.purchases.InternalRevenueCatAPI
import com.revenuecat.purchases.common.events.FeatureEvent
import com.revenuecat.purchases.utils.serializers.DateSerializer
import com.revenuecat.purchases.utils.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

/**
 * Types of paywall events. Meant for RevenueCatUI use.
 */
@InternalRevenueCatAPI
enum class PaywallEventType(val value: String) {
    /**
     * The paywall was shown to the user.
     */
    IMPRESSION("paywall_impression"),

    /**
     * The user cancelled a purchase.
     */
    CANCEL("paywall_cancel"),

    /**
     * The paywall was dismissed.
     */
    CLOSE("paywall_close"),
}

/**
 * Type representing a paywall event and associated data. Meant for RevenueCatUI use.
 */
@InternalRevenueCatAPI
@Serializable
data class PaywallEvent(
    val creationData: CreationData,
    val data: Data,
    val type: PaywallEventType,
) : FeatureEvent {

    @Serializable
    data class CreationData(
        @Serializable(with = UUIDSerializer::class)
        val id: UUID,
        @Serializable(with = DateSerializer::class)
        val date: Date,
    )

    @Serializable
    data class Data(
        val offeringIdentifier: String,
        val paywallRevision: Int,
        @Serializable(with = UUIDSerializer::class)
        val sessionIdentifier: UUID,
        val displayMode: String, // Refer to PaywallMode in the RevenueCatUI module.
        val localeIdentifier: String,
        val darkMode: Boolean,
    )

    internal fun toPaywallPostReceiptData(): PaywallPostReceiptData {
        return PaywallPostReceiptData(
            sessionID = data.sessionIdentifier.toString(),
            revision = data.paywallRevision,
            displayMode = data.displayMode,
            darkMode = data.darkMode,
            localeIdentifier = data.localeIdentifier,
            offeringId = data.offeringIdentifier,
        )
    }
}
