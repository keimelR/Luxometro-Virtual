package com.example.luxometromovil.data.sensor

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LightSensor @Inject constructor(
    private val context: Context
): SensorEventListener {
    private val doesSensorExist: Boolean
        get() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT)
    private var sensorManager: SensorManager = context.getSystemService(SensorManager::class.java) as SensorManager
    private var lightSensor: Sensor? = null

    private val _currentLux = MutableStateFlow(-1f)
    val currentLux = _currentLux.asStateFlow()

    fun onStartListening() {
        if (!doesSensorExist) {
            return
        }
        if(doesSensorExist) {
            Log.d("LightSensor", "Iniciando el LightSensor")
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        }
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun onStopListening() {
        if (!doesSensorExist) {
            return
        }
        Log.d("LightSensor", "Deteniendo el LightSensor")

        lightSensor?.let {
            sensorManager.unregisterListener(this)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.values?.let {
            _currentLux.value = it[0]
        }
    }

    private fun getNameSensor(): String = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!!.name
    private fun getMaximunRangeSensor(): String = "${sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)?.maximumRange}"
    private fun getVersionSensor(): String = "${sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)?.version}"
    private fun getIdSensor(): String = "${sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)?.id}"
    private fun getVendorSensor(): String = "${sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)?.vendor}"
    private fun getResolutionSensor(): String = "${sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)?.resolution}"

    fun getInformationSensor(): Map<Int, String> {
        return mapOf(
            0 to getNameSensor(),
            1 to getVendorSensor(),
            2 to getResolutionSensor(),
            3 to getMaximunRangeSensor()
        )
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit
}