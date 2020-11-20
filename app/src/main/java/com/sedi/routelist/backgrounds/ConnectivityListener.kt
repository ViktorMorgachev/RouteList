package com.sedi.routelist.backgrounds

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import com.sedi.routelist.commons.LOG_LEVEL
import com.sedi.routelist.commons.log


open class ConnectivityListener(private val context: Context) {

    companion object {
        private const val RESTRICT_BACKGROUND_STATUS_DISABLED =
            1 // ConnectivityManager.RESTRICT_BACKGROUND_STATUS_DISABLED
    }

    val restrictBackgroundStatus: RestrictBackgroundStatus
        get() {
            return RESTRICT_BACKGROUND_STATUS_DISABLED.run restrictBackgroundRun@{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    this@ConnectivityListener.connectivityManager.apply {
                        if (this.isActiveNetworkMetered) {
                            return@restrictBackgroundRun this.restrictBackgroundStatus
                        }
                    }
                }
                this
            }.run {
                when (this) {
                    ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED -> RestrictBackgroundStatus.ENABLED
                    ConnectivityManager.RESTRICT_BACKGROUND_STATUS_WHITELISTED -> RestrictBackgroundStatus.WHITELISTED
                    ConnectivityManager.RESTRICT_BACKGROUND_STATUS_DISABLED -> RestrictBackgroundStatus.DISABLED
                    else -> RestrictBackgroundStatus.OTHER
                }
            }
        }

    protected val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var onConnectivityInformationChangedListener: OnConnectivityInformationChangedListener? =
        null

    private val networkBroadcastReceiver = NetWorkBroadCastReceiver()

    fun setListener(onConnectivityInformationChangedListener: OnConnectivityInformationChangedListener) {
        this.onConnectivityInformationChangedListener = onConnectivityInformationChangedListener
        this.onConnectivityInformationChangedListener?.onConnectivityInformationChanged(
            getNetworkType()
        )
    }

    fun register() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            registerBroadcastListenerOnLollipopAndAbove()
        } else {
            registerBroadcastReceiverUnderLolipop()
        }
        log("Successfully registered")
    }

    fun unregister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            unregisterBroadcastListenerLollipopAndAbove()
        } else {
            unregisterBroadcastReceiverUnderLolipop()
        }
        log("Successfully unregistered")
        this.onConnectivityInformationChangedListener = null
    }

    fun getConnectionInformation(): ConnectivityInformation {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var capabilities: NetworkCapabilities? = null
            connectivityManager.allNetworks.forEach { network ->
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                if (networkCapabilities != null) {
                    if (capabilities == null && networkCapabilities.isConnected()) {
                        capabilities = networkCapabilities
                    }
                }
            }
            getNetworkType(connectivityManager.activeNetworkInfo, capabilities).run {
                onConnectivityInformationChangedListener?.onConnectivityInformationChanged(this)
                this
            }
        } else {
            getNetworkType(connectivityManager.activeNetworkInfo).run {
                onConnectivityInformationChangedListener?.onConnectivityInformationChanged(this)
                this
            }
        }
    }

    private fun registerBroadcastReceiverUnderLolipop() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkBroadcastReceiver, filter)
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private fun unregisterBroadcastReceiverUnderLolipop() {
        context.unregisterReceiver(networkBroadcastReceiver)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun registerBroadcastListenerOnLollipopAndAbove() {
        val builder = NetworkRequest.Builder()
        networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                onConnectivityInformationChangedListener?.onConnectivityInformationChanged(
                    ConnectivityInformation.CONNECTED
                )
            }

            override fun onLost(network: Network) {
                connectivityManager.getNetworkCapabilities(network)
                onConnectivityInformationChangedListener?.onConnectivityInformationChanged(
                    ConnectivityInformation.DISCONNECTED
                )
            }

            override fun onUnavailable() {
                onConnectivityInformationChangedListener?.onConnectivityInformationChanged(
                    getNetworkType(
                        connectivityManager.activeNetworkInfo
                    )
                )
            }
        }

        connectivityManager.registerNetworkCallback(
            builder.build(),
            networkCallback as ConnectivityManager.NetworkCallback
        )
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun unregisterBroadcastListenerLollipopAndAbove() {
        networkCallback?.let { connectivityManager.unregisterNetworkCallback(it) }
    }

    private fun getNetworkType(activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo): ConnectivityInformation {
        return if (activeNetworkInfo == null || !activeNetworkInfo.isConnected) {
            ConnectivityInformation.DISCONNECTED
        } else {
            when (activeNetworkInfo.type) {
                ConnectivityManager.TYPE_WIFI -> ConnectivityInformation.WIFI
                ConnectivityManager.TYPE_MOBILE -> ConnectivityInformation.MOBILE
                else -> ConnectivityInformation.OTHER
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun NetworkCapabilities.isConnected(): Boolean {
        return hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        ))
                && (Build.VERSION.SDK_INT < Build.VERSION_CODES.P || hasCapability(
            NetworkCapabilities.NET_CAPABILITY_FOREGROUND
        ))
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getNetworkType(
        activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo,
        networkCapabilities: NetworkCapabilities?
    ): ConnectivityInformation {
        return if ((networkCapabilities?.isConnected() == true).not()) {
            ConnectivityInformation.DISCONNECTED
        } else {
            when (activeNetworkInfo?.type) {
                ConnectivityManager.TYPE_WIFI -> ConnectivityInformation.WIFI
                ConnectivityManager.TYPE_MOBILE -> ConnectivityInformation.MOBILE
                else -> ConnectivityInformation.OTHER
            }
        }

    }

    inner class NetWorkBroadCastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.extras != null) {
                val activeNetwork =
                    intent.extras?.get(ConnectivityManager.EXTRA_NETWORK_INFO) as NetworkInfo
                onConnectivityInformationChangedListener?.onConnectivityInformationChanged(
                    getNetworkType(activeNetwork)
                )
            }
        }

    }
}

enum class ConnectivityInformation(val isConnected: Boolean) {
    WIFI(true),
    MOBILE(true),
    OTHER(true),
    DISCONNECTED(false),
    CONNECTED(true)
}

enum class RestrictBackgroundStatus(val isRestricted: Boolean) {
    ENABLED(true),
    WHITELISTED(false),
    DISABLED(false),
    OTHER(false)
}

interface OnConnectivityInformationChangedListener {
    fun onConnectivityInformationChanged(connectivityInformation: ConnectivityInformation)
}