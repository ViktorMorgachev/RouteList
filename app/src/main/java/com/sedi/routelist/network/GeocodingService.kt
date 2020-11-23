package com.sedi.routelist.network

import com.google.gson.GsonBuilder
import com.huawei.hms.maps.model.LatLng
import com.sedi.routelist.MyApplication
import com.sedi.routelist.commons.LOG_LEVEL
import com.sedi.routelist.commons.log
import com.sedi.routelist.network.geocode.reverse.ReverseGeocode
import com.sedi.routelist.presenters.IActionResult
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException


object GeocodingService {

    @Throws(UnsupportedEncodingException::class)
    fun reverseGeocoding(latLng: LatLng, iActionResult: IActionResult) {
        val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull();
        val json = JSONObject()
        val location = JSONObject()
        try {
            location.put("lng", latLng.longitude)
            location.put("lat", latLng.longitude)
            json.put("location", location)
            json.put("language", MyApplication.language)
        } catch (e: JSONException) {
            log(e)
        }


        val body: RequestBody = json.toString().toRequestBody(JSON)
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://siteapi.cloud.huawei.com/mapApi/v1/siteService/reverseGeocode?key=${MyApplication.api_key}")
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                log("ReverseGeocoding: $e", LOG_LEVEL.ERROR)
                iActionResult.result(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                val gson = GsonBuilder().create()
                val data = gson.fromJson(result, ReverseGeocode::class.java)
                iActionResult.result(result, null)
            }
        })
    }
}