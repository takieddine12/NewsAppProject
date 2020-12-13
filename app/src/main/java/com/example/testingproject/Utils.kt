package com.example.testingproject

import android.content.Context
import android.net.ConnectivityManager
import es.dmoral.toasty.Toasty


object Utils {

    const val API_KEY = "a9105f91876947b1b3b70761813fd4f9"
    const val NETWORK_PAGE_SIZE = 10
    const val CACHE_PAGE_SIZE = 10

    fun checkConnectivity(context: Context) : Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netWorkInfo = connectivityManager.activeNetworkInfo
        return netWorkInfo != null
    }
}

fun Context.showToast(msg : String) = Toasty.success(this,msg,Toasty.LENGTH_SHORT).show()
fun Context.showErrorToast(msg : String) = Toasty.error(this,msg,Toasty.LENGTH_SHORT).show()
fun Context.showWarningToast(msg : String) = Toasty.info(this,msg,Toasty.LENGTH_SHORT).show()
fun Context.showNormalToast(msg : String) = android.widget.Toast.makeText(this,msg, android.widget.Toast.LENGTH_SHORT).show()

