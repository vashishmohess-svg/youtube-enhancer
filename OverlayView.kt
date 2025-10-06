package com.example.youtubeenhancer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView

class OverlayView(context: Context) : LinearLayout(context) {
    private val paint = Paint()
    private var tintAlpha = 0
    private var tintColor = 0x990000FF.toInt()
    private var showControls = false
    private var vignette = 0

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.overlay_controls, this, true)
        val sb = findViewById<SeekBar>(R.id.seek_tint)
        val sbV = findViewById<SeekBar>(R.id.seek_vignette)
        val txt = findViewById<TextView>(R.id.txt_status)

        sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener { 
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { tintAlpha = (p1 * 255)/100; invalidate() }
            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })
        sbV.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener { 
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { vignette = p1; invalidate() }
            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })
        txt.setOnClickListener { 
            // quick preset cycle
            tintAlpha = if (tintAlpha==0) 80 else 0
            invalidate()
        }
        visibility = GONE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // draw tint overlay
        if (tintAlpha>0) {
            paint.color = tintColor and 0x00FFFFFF or (tintAlpha shl 24)
            paint.style = Paint.Style.FILL
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }
        // cinematic bars (letterbox)
        val barHeight = (height * (vignette/100f) * 0.12f)
        if (barHeight>0) {
            paint.color = 0xFF000000.toInt()
            canvas.drawRect(0f, 0f, width.toFloat(), barHeight, paint)
            canvas.drawRect(0f, height - barHeight, width.toFloat(), height.toFloat(), paint)
        }
    }

    fun toggleControls() {
        showControls = !showControls
        visibility = if (showControls) VISIBLE else GONE
    }
}
