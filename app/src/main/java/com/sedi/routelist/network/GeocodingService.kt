package com.sedi.routelist.network

import com.google.gson.GsonBuilder
import com.huawei.hms.maps.model.LatLng
import com.sedi.routelist.MyApplication
import com.sedi.routelist.commons.LOG_LEVEL
import com.sedi.routelist.commons.log
import com.sedi.routelist.models.Address
import com.sedi.routelist.network.geocode.reverse.ReverseGeocode
import com.sedi.routelist.network.geocode.reverse.osm.Addresses
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
    fun reverseGeocoding(
        geoCodingType: GeoCodingType,
        latLng: LatLng,
        iActionResult: IActionResult
    ) {
        if (geoCodingType == GeoCodingType.HUAWEI) {
            reverseHuaweiGeocoding(latLng, iActionResult)
        } else if (geoCodingType == GeoCodingType.OpenStreetMap) {
            reverseOSMGeocoding(latLng, iActionResult)
        }
    }

    fun reverseOSMGeocoding(latLng: LatLng, iActionResult: IActionResult) {
        val url =
            "https://nominatim.openstreetmap.org/search?format=json&accept-language=ru&q=${latLng.latitude},${latLng.longitude}&poligon=1&addressdetails=1"
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()

        log(
            "Request: URL " +
                    "https://nominatim.openstreetmap.org/search?format=json&accept-language=ru&q=${latLng.latitude},${latLng.longitude}&poligon=1&addressdetails=1 | " +
                    "Location: $latLng | Language: $MyApplication.language"
        )
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                log("ReverseGeocoding: $e", LOG_LEVEL.ERROR)
                iActionResult.result(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val result = response.body?.string()
                    log("Result: $result")
                    val gson = GsonBuilder().create()
                    val addresses = gson.fromJson(result, Addresses::class.java) as Addresses
                    if (addresses.isNotEmpty()) {
                        val address: Address = Address().apply {
                            address = addresses[0].display_name
                            location.longitude = addresses[0].lon.toDouble()
                            location.latitude = addresses[0].lat.toDouble()
                        }
                        if ((address.location.latitude != 0.0 && address.location.longitude != 0.0) && address.address.isNotEmpty())
                            iActionResult.result(address, null)
                    }
                } catch (e: Exception) {
                    iActionResult.result(null, e)
                }

            }
        })
    }

    fun reverseHuaweiGeocoding(latLng: LatLng, iActionResult: IActionResult) {
        val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull();
        val json = JSONObject()
        val location = JSONObject()
        try {
            location.put("lng", latLng.longitude)
            location.put("lat", latLng.longitude)
            json.put("location", location)
            json.put("language", MyApplication.language)
            json.put("returnPoi", true)
        } catch (e: JSONException) {
            log(e)
        }


        val body: RequestBody = json.toString().toRequestBody(JSON)
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://siteapi.cloud.huawei.com/mapApi/v1/siteService/reverseGeocode?key=${MyApplication.api_key}")
            .post(body)
            .build()

        log(
            "Request: URL " +
                    "https://siteapi.cloud.huawei.com/mapApi/v1/siteService/reverseGeocode?key=${MyApplication.api_key} | " +
                    "Location: $latLng | Language: $MyApplication.language"
        )
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                log("ReverseGeocoding: $e", LOG_LEVEL.ERROR)
                iActionResult.result(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val result = response.body?.string()
                    log("Result: $result")
                    val gson = GsonBuilder().create()
                    val data = gson.fromJson(result, ReverseGeocode::class.java)
                    iActionResult.result(data, null)
                } catch (e: Exception) {
                    iActionResult.result(null, e)
                }

            }
        })
    }
}

enum class GeoCodingType {
    OpenStreetMap,
    HUAWEI
}