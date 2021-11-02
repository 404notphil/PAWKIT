package com.tunepruner.fingerperc.store
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
    val LOG_TAG = "BillCliWrapper.Class"

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
            clientToReturn.prepareBillingClient(context)
            clientToReturn.repoListener = billingClientListener
            return clientToReturn
        }
    }

    private fun prepareBillingClient(context: Context) {
        Log.d(LOG_TAG, "prepareBillingClient() called with: context = $context")
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
                    // TODO Handle any other error codes.
                }
            }
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
        initBilling()
    }

    private fun initBilling() {
        Log.d(LOG_TAG, "initBilling() called")
        billingClient.startConnection(
            object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.i(LOG_TAG, "clientSetupFinished! billingResult.responseCode == ok!")
                        clientSetupFinished = true
                        repoListener.onClientReady()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    Log.i(LOG_TAG, "onBillingServiceDisconnected: ")
                    //initBilling() /*maybe?*/
                }
            })
    }

    fun queryPurchases() {
        Log.i(LOG_TAG, "queryPurchases started")
        val purchasesResultObject = billingClient.queryPurchases(BillingClient.SkuType.INAPP)

        val listNotNull: ArrayList<Purchase> = ArrayList()
        Log.i(LOG_TAG, "listNotNull = $listNotNull")

        purchasesResultObject.purchasesList.let { purchasesList ->
            Log.i(LOG_TAG, "purchasesResult isn't null.: ")
            if (purchasesList != null) {
                Log.i(LOG_TAG, "purchasesList isn't null: ")
                for (purchase in purchasesList) {
                    listNotNull.add(purchase)
                    Log.i(LOG_TAG, "purchase sku: ${purchase.sku}")
                }
                repoListener.onPurchasesQueried(listNotNull)
                Log.i(LOG_TAG, "listNotNull = $listNotNull")
            }
        }

    }

    fun querySkuDetails(activity: Activity, soundpackID: String) {
        Log.i(LOG_TAG, "startingQuery")
        Log.i(LOG_TAG, "soundpackID =  $soundpackID")
        val skuList = ArrayList<String>()
        skuList.add(soundpackID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        var skuDetails: SkuDetails? = null

        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                skuDetails = skuDetailsList[0]
                Log.i(LOG_TAG, "skuDetails: $skuDetails")
                flow(activity, skuDetails)
            }
        }
    }


    private fun flow(activity: Activity, skuDetails: SkuDetails?) {
        Log.i(LOG_TAG, "flow")
        Log.i(LOG_TAG, "skuDetails = null?: ${skuDetails == null}")
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