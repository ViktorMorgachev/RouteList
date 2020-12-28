package com.sedi.routelist.network

import com.google.gson.GsonBuilder
import com.huawei.hms.maps.model.LatLng
import com.sedi.routelist.MyApplication
import com.sedi.routelist.commons.LOG_LEVEL
import com.sedi.routelist.commons.log
import com.sedi.routelist.enums.GeoCodingType
import com.sedi.routelist.enums.RouteType
import com.sedi.routelist.models.Address
import com.sedi.routelist.network.result.geocode.reverse.huawei.GeocodeModelHuawei
import com.sedi.routelist.network.result.geocode.reverse.osm.AddressesOSM
import com.sedi.routelist.network.result.road.huawei.DirectionModel
import com.sedi.routelist.interfaces.IActionResult
import com.sedi.routelist.network.result.road.huawei.ErrorResponseHuawei
import com.sedi.routelist.network.result.road.osm.RoadResponseOSRM
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


object NetService : IServices {

    private var requestReverseGeocodingHuawei: (() -> Unit)? = null
    private var requestReverseGeocodingOSRM: (() -> Unit)? = null
    private var lastReverseType: ReverseRequestType = ReverseRequestType.HUAWEI


    override fun getDirection(
        currentGeoCodingType: GeoCodingType,
        routeType: RouteType,
        latLngFrom: LatLng,
        latLngTo: LatLng,
        iActionResult: IActionResult
    ) {

        if (currentGeoCodingType == GeoCodingType.HUAWEI) {
            val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
            val urlBuilder =
                StringBuilder("https://mapapi.cloud.huawei.com/mapApi/v1/routeService/")
            urlBuilder.append(routeType.HuaweiApi).append("?key=${MyApplication.api_key}")

            val jsonObject = JSONObject().apply {
                put("origin", JSONObject().apply {
                    put("lng", latLngFrom.longitude)
                    put("lat", latLngFrom.latitude)
                })
                put("destination", JSONObject().apply {
                    put("lng", latLngTo.longitude)
                    put("lat", latLngTo.latitude)
                })

            }

            val body: RequestBody = jsonObject.toString().toRequestBody(JSON)
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url(urlBuilder.toString())
                .post(body)
                .build()

            log("Request: URL $urlBuilder data: ${jsonObject.toString()}")
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    log("getRoute: $e", LOG_LEVEL.ERROR)
                    iActionResult.result(null, e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        try {
                            val result = response.body?.string()
                            log("Error: $result")
                            val gson = GsonBuilder().create()
                            val data = gson.fromJson(result, ErrorResponseHuawei::class.java)
                            iActionResult.result(data, null)
                        } catch (e: Exception) {
                            iActionResult.result(null, e)
                        }
                        return
                    }
                    try {
                        val result = response.body?.string()
                        log("Result: $result")
                        val gson = GsonBuilder().create()
                        val data = gson.fromJson(result, DirectionModel::class.java)
                        iActionResult.result(data, null)
                    } catch (e: Exception) {
                        iActionResult.result(null, e)
                    }

                }
            })
        } else {
            val urlBuilder = StringBuilder("http://osrm1.sedi.ru:5005/route/v1/")
            urlBuilder.append(routeType.OSRMApi).append("/").append(latLngFrom.latitude).append(";")
                .append(latLngTo.longitude)
                .append("?overview=full&geometries=polyline&alternatives=true")

            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url(urlBuilder.toString())
                .get()
                .build()

            log("Request: URL $urlBuilder")
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    log("OSRMGetRoad: $e", LOG_LEVEL.ERROR)
                    iActionResult.result(null, e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val result = response.body?.string()
                        log("Result: $result")
                        val gson = GsonBuilder().create()
                        val road =
                            gson.fromJson(result, RoadResponseOSRM::class.java) as RoadResponseOSRM
                        iActionResult.result(road, null)
                    } catch (e: Exception) {
                        iActionResult.result(null, e)
                    }

                }
            })

        }
    }

    override fun changeReverseGeocodingType() {
        lastReverseType =
            if (lastReverseType == ReverseRequestType.HUAWEI) ReverseRequestType.OpenStreetMap else ReverseRequestType.HUAWEI
    }


    override fun getAddress(
        geoCodingType: GeoCodingType,
        latLng: LatLng,
        iActionResult: IActionResult
    ) {
        if (geoCodingType == GeoCodingType.HUAWEI) {
            requestReverseGeocodingHuawei = { reverseHuaweiGeocoding(latLng, iActionResult) }
            lastReverseType = ReverseRequestType.HUAWEI
            requestReverseGeocodingHuawei?.invoke()
        } else if (geoCodingType == GeoCodingType.OpenStreetMap) {
            requestReverseGeocodingOSRM = { reverseOSMGeocoding(latLng, iActionResult) }
            lastReverseType = ReverseRequestType.OpenStreetMap
            requestReverseGeocodingOSRM?.invoke()
        }
    }


    private fun reverseOSMGeocoding(latLng: LatLng, iActionResult: IActionResult) {
        val url =
            "https://nominatim.openstreetmap.org/search?format=json&accept-language=${MyApplication.language}&q=${latLng.latitude},${latLng.longitude}&poligon=1&addressdetails=1"
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()

        log("Request: URL $url |")
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
                    val addresses = gson.fromJson(result, AddressesOSM::class.java) as AddressesOSM
                    if (addresses.isNotEmpty()) {
                        val address: Address = Address().apply {
                            address = addresses[0].display_name
                            location =
                                LatLng(addresses[0].lat.toDouble(), addresses[0].lon.toDouble())
                        }
                        if ((address.location != null) && address.address.isNotEmpty())
                            iActionResult.result(address, null)
                    }
                } catch (e: Exception) {
                    iActionResult.result(null, e)
                }

            }
        })
    }

    private fun reverseHuaweiGeocoding(latLng: LatLng, iActionResult: IActionResult) {
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
                if (!response.isSuccessful) {
                    try {
                        val result = response.body?.string()
                        log("Error: $result")
                        val gson = GsonBuilder().create()
                        val data = gson.fromJson(result, ErrorResponseHuawei::class.java)
                        iActionResult.result(data, null)
                    } catch (e: Exception) {
                        iActionResult.result(null, e)
                    }
                    return
                }
                try {
                    val result = response.body?.string()
                    log("Result: $result")
                    val gson = GsonBuilder().create()
                    val data = gson.fromJson(result, GeocodeModelHuawei::class.java)
                    iActionResult.result(data, null)
                } catch (e: Exception) {
                    iActionResult.result(null, e)
                }

            }
        })
    }

    override fun repeatGetAddress() {
        when (lastReverseType) {
            ReverseRequestType.HUAWEI -> requestReverseGeocodingOSRM?.invoke()
            ReverseRequestType.OpenStreetMap -> requestReverseGeocodingHuawei?.invoke()
        }
    }

    enum class ReverseRequestType {
        OpenStreetMap, HUAWEI
    }

}



