package com.tunepruner.fingerperc.launchscreen.librarylist

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*

class BillingClientWrapper {
    private lateinit var billingClient: BillingClient
    var latestSkuDetails: SkuDetails? = null
    lateinit var purchaseHistoryRecords: List<PurchaseHistoryRecord>
    var soundpackID: String = ""
    var setupFinished = false
    val TAG = "BillCliWrapper.Class"

    companion object {
        var billingClientWrapperStored: BillingClientWrapper? = null

        fun getInstance(context: Context, soundpackID: String): BillingClientWrapper {
            val clientToReturn: BillingClientWrapper = if (billingClientWrapperStored != null) {
                    billingClientWrapperStored as BillingClientWrapper
                } else {
                    BillingClientWrapper()
                }
            clientToReturn.soundpackID = soundpackID
            clientToReturn.prepareBillingClient(context)
            return clientToReturn
        }
    }

    private fun prepareBillingClient(context: Context) {
        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                val purchasesNotNull = purchases != null
                val responseCode = billingResult.responseCode
                val billingCode = BillingClient.BillingResponseCode.OK
                val billingResponseCodeOK = responseCode == billingCode
                if (billingResponseCodeOK && purchases != null) {
                    for (purchase in purchases) {
                        billingClientWrapperStored?.handleConsumedPurchases(purchase)
                    }
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                    // Handle an error caused by a user cancelling the purchase flow.
                } else {
                    // Handle any other error codes.
                }
            }
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
        initBilling()
    }

    private fun initBilling() {
        billingClient.startConnection(
            object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        querySkuDetails()
                        setupFinished = true
                    }
                }

                override fun onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
    }

    private fun querySkuDetails() {
        val skuList = ArrayList<String>()
        skuList.add(soundpackID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                latestSkuDetails = skuDetailsList[0]
            }
        }
    }

    private fun handleConsumedPurchases(purchase: Purchase) {
        Log.d("TAG_INAPP", "handleConsumablePurchasesAsync foreach it is $purchase")
        val params =
            ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
        billingClient.consumeAsync(params) { billingResult, purchaseToken ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    // Update the appropriate tables/databases to grant user the items
                    Log.d(
                        "TAG_INAPP",
                        " Update the appropriate tables/databases to grant user the items"
                    )
                }
                else -> {
                    Log.w("TAG_INAPP", billingResult.debugMessage)
                }
            }
        }
    }

    private fun handleNonConsumablePurchase(purchase: Purchase) {
        Log.v("TAG_INAPP", "handlePurchase : $purchase")
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken).build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    val billingResponseCode = billingResult.responseCode
                    val billingDebugMessage = billingResult.debugMessage

                    Log.v("TAG_INAPP", "response code: $billingResponseCode")
                    Log.v("TAG_INAPP", "debugMessage : $billingDebugMessage")
                }
            }
        }
    }

    fun flow(activity: Activity) {
        latestSkuDetails.let {
            val billingFlowParams = it?.let { it1 ->
                BillingFlowParams.newBuilder()
                    .setSkuDetails(it1)
                    .build()
            }
            val billingResult =
                billingFlowParams?.let { it1 -> billingClient.launchBillingFlow(activity, it1) }
            val response = billingResult?.responseCode
            val debug = billingResult?.debugMessage
            val testing = ""
        }
    }

    fun checkIfPurchased() {
        billingClient.queryPurchaseHistoryAsync("inapp"
        ) { billingResult, purchaseHistoryRecords ->
            Log.i(TAG, "billing responsecode = ${billingResult.responseCode}")
            Log.i(TAG, "billing responsecode = ${billingResult.debugMessage}")
            Log.i(TAG, purchaseHistoryRecords.toString())

        }
    }
}
