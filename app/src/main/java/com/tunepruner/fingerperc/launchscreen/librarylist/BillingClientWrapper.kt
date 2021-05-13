package com.tunepruner.fingerperc.launchscreen.librarylist

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*

class BillingClientWrapper {
    private lateinit var billingClient: BillingClient
    var latestSkuDetails: SkuDetails? = null
    var purchaseHistoryRecords1: ArrayList<PurchaseHistoryRecord> = ArrayList()
    lateinit var repoListener: BillingClientListener
    var clientSetupFinished = false
    val TAG = "BillCliWrapper.Class"

    companion object {
        var billingClientWrapperStored: BillingClientWrapper? = null

        fun getInstance(
            billingClientListener: BillingClientListener,
            context: Context
        ): BillingClientWrapper {
            val clientToReturn: BillingClientWrapper = if (billingClientWrapperStored != null) {
                billingClientWrapperStored as BillingClientWrapper
            } else {
                BillingClientWrapper()
            }
//            clientToReturn.soundpackID = soundpackID
            clientToReturn.prepareBillingClient(context)
            clientToReturn.repoListener = billingClientListener
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
                        handleNonConsumablePurchase(purchase)
                        handleConsumedPurchases(purchase)
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
                        clientSetupFinished = true
                        repoListener.onClientReady()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
    }

    fun queryPurchases() {
//        billingClient.queryPurchaseHistoryAsync(
//            "inapp"
//        ) { _, purchaseHistoryRecords ->
//            if (purchaseHistoryRecords != null) {
//                for (element in purchaseHistoryRecords) {
//                    purchaseHistoryRecords1.add(element)
//                }
//            }
//            repoListener.onPurchaseHistoryReady(purchaseHistoryRecords1)
//        }
        Log.i(TAG, "queryPurchases started")
        val purchasesResultObject = billingClient.queryPurchases(BillingClient.SkuType.INAPP)

        val listNotNull: ArrayList<Purchase> = ArrayList()
        Log.i(TAG, "listNotNull = $listNotNull")

        purchasesResultObject.purchasesList.let { purchasesList ->
            Log.i(TAG, "purchasesResult isn't null.: ")
            if (purchasesList != null) {
                Log.i(TAG, "purchasesList isn't null: ")
                for (purchase in purchasesList) {
                    listNotNull.add(purchase)
                    Log.i(TAG, "purchase sku: ${purchase.sku}")
                }
                repoListener.onPurchasesQueried(listNotNull)
                Log.i(TAG, "listNotNull = $listNotNull")

            }
        }

    }

    fun querySkuDetails(activity: Activity, soundpackID: String) {
        Log.i(TAG, "startingQuery")
        Log.i(TAG, "soundpackID =  $soundpackID")
        val skuList = ArrayList<String>()
        skuList.add(soundpackID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        var skuDetails: SkuDetails? = null

        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                skuDetails = skuDetailsList[0]
                Log.i(TAG, "skuDetails: $skuDetails")
                flow(activity, skuDetails)
            }
        }
    }


    private fun flow(activity: Activity, skuDetails: SkuDetails?) {
        Log.i(TAG, "flow")
        Log.i(TAG, "skuDetails = null?: ${skuDetails == null}")
        skuDetails.let {
            val billingFlowParams = it?.let { it1 ->
                BillingFlowParams.newBuilder()
                    .setSkuDetails(it1)
                    .build()
            }
            val billingResult =
                billingFlowParams?.let { it1 -> billingClient.launchBillingFlow(activity, it1) }
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


}


interface BillingClientListener {
    fun onClientReady()
    fun onPurchasesQueried(soundpacksPurchased: ArrayList<Purchase>)
}