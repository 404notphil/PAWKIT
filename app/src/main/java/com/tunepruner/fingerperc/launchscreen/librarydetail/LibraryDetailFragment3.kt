package com.tunepruner.fingerperc.launchscreen.librarydetail

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android.billingclient.api.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.tunepruner.fingerperc.InstrumentActivity
import com.tunepruner.fingerperc.databinding.FragmentLibraryDetail3Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LibraryDetailFragment3 : Fragment() {
    private lateinit var skuDetails: SkuDetails
    private val logTag = "LibraryDetailFragment.Class"
    private val args: LibraryDetailFragment3Args by navArgs()
    private lateinit var binding: FragmentLibraryDetail3Binding
    private var stopRequested = false
    private lateinit var purchasesUpdatedListener: PurchasesUpdatedListener
    private lateinit var billingClient: BillingClient
    private val TAG = "LibraryDetailFragment3.class"

    override fun onResume() {
        super.onResume()
        binding.titleOfLibraryDetail.text = args.libraryname

        binding.button.text = if (args.ispurchased) "Play now!" else "$0.99"

        binding.button.setOnClickListener {
            if (args.ispurchased) {
                val progressBarLength = 1000
                showLoadingInstrument(progressBarLength / 100, 100)
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    if (!stopRequested) {
                        val intent =
                            Intent(requireActivity(), InstrumentActivity::class.java).apply {
                                putExtra("libraryID", args.libraryid)
                            }
                        startActivity(intent)
                    }
                }, progressBarLength.toLong())
            } else {
                flow()
            }
//
        }

        val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView
        youTubePlayerView.enterFullScreen()

        Log.i(logTag, "Resuming")

        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(
            object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo("UlaJpGEDFVM", 0F)
                    youTubePlayerView.alpha = 1F
                }
            })

        purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            val purchasesNotNull = purchases != null
            val responseCode = billingResult.responseCode
            val billingCode = BillingClient.BillingResponseCode.OK
            val billingResponseCodeOK = responseCode == billingCode
            if (billingResponseCodeOK && purchases != null) {
                for (purchase in purchases) {
                    handleConsumedPurchases(purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                val testing = ""
            } else {
                // Handle any other error codes.
                val testing = ""
            }
        }

        billingClient = BillingClient.newBuilder(requireActivity())
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        initBilling()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryDetail3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRequested = true
    }

    private fun showLoadingInstrument(intervalLength: Int, amountOfIntervals: Int) {
        binding.loadingStatusText.text = "Loading..."
        binding.progressBar2.alpha = 1F
        binding.loadingStatusText.alpha = 1F

        val handler = Handler(Looper.getMainLooper())

        var intervalsAccumulated = intervalLength
        for (i in 0..amountOfIntervals) {
            handler.postDelayed(
                {
                    if (!stopRequested) {
                        binding.progressBar2.progress += amountOfIntervals / 100
                        Log.i(logTag, "progressing")
                    }
                }, intervalsAccumulated.toLong()
            )
            intervalsAccumulated += intervalLength
        }

        handler.postDelayed({
            if (!stopRequested) {
                binding.loadingStatusText.text = "Opening..."
            }
        }, (intervalLength * amountOfIntervals).toLong() - 200)

        handler.postDelayed({
            if (!stopRequested) {
                binding.progressBar2.alpha = 0F
                binding.progressBar2.progress = 0
                binding.loadingStatusText.alpha = 0F
            }
        }, intervalsAccumulated.toLong())
    }

    private fun initBilling() {
        billingClient.startConnection(
            object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        querySkuDetails()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                    val testing = ""
                }
            })
    }

    fun querySkuDetails() {
        val skuList = ArrayList<String>()
        skuList.add("drumkit_soundpack_1")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

//        withContext(Dispatchers.IO) {

        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                skuDetails = skuDetailsList[0]
            }
//        }
        }
    }

    private fun flow() {
        skuDetails.let {
            val billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(it)
                .build()
            val billingResult = billingClient.launchBillingFlow(requireActivity(), billingFlowParams)
            val response = billingResult.responseCode
            val debug = billingResult.debugMessage
            val testing  = ""
        }

        Log.i(TAG, skuDetails.title)
    }

    private fun handleConsumedPurchases(purchase: Purchase) {
        Log.d("TAG_INAPP", "handleConsumablePurchasesAsync foreach it is $purchase")
        val params =
            ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
        billingClient?.consumeAsync(params) { billingResult, purchaseToken ->
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

    private fun handleNonConcumablePurchase(purchase: Purchase) {
        Log.v("TAG_INAPP","handlePurchase : ${purchase}")
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken).build()
                billingClient?.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    val billingResponseCode = billingResult.responseCode
                    val billingDebugMessage = billingResult.debugMessage

                    Log.v("TAG_INAPP","response code: $billingResponseCode")
                    Log.v("TAG_INAPP","debugMessage : $billingDebugMessage")

                }
            }
        }
    }

    private fun updateUiWithResults(skuDetails: SkuDetails) {
        binding.button.text = skuDetails.title
    }
}
