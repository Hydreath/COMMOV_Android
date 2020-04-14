package com.commov.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


class ShakeDetector : SensorEventListener {
    private var mListener: OnShakeListener? = null
    fun setOnShakeListener(listener: OnShakeListener?) {
        mListener = listener
    }

    interface OnShakeListener {
        fun onShake()
    }

    override fun onAccuracyChanged(
        sensor: Sensor,
        accuracy: Int
    ) { // ignore
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (mListener != null) {
            val x = event.values[0]
            if (x > THRESHOLD || x < -THRESHOLD) {
                mListener!!.onShake()
            }
        }
    }

    companion object {
        private const val THRESHOLD:Int = 2
    }
}