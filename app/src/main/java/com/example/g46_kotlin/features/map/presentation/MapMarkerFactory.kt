package com.example.g46_kotlin.features.map.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.collection.LruCache
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable
import com.example.g46_kotlin.R
import kotlin.math.roundToInt

object MapMarkerFactory {

	private val bitmapCache = LruCache<String, Bitmap>(maxSize = 40)

	fun createMarkerIcon(context: Context, price: String): BitmapDrawable {
		val cached = bitmapCache[price]
		if (cached != null) {
			return cached.toDrawable(context.resources)
		}

		val bitmap = createMarkerBitmap(context, price)
		bitmapCache.put(price, bitmap)
		return createMarkerBitmap(context, price).toDrawable(context.resources)
	}

	fun createMarkerBitmap(context: Context, price: String): Bitmap {
		val markerView = inflateMarkerView(context, price)
		val base = renderViewToBitmap(markerView)

		return addSoftShadow(
			source = base,
			blur = 5f,
			dy = 2f,
			shadowColor = 0x2A000000
		)
	}

	private fun inflateMarkerView(context: Context, price: String): View {
		val root = FrameLayout(context)
		val view = LayoutInflater.from(context).inflate(R.layout.view_map_marker, root, false)
		view.findViewById<TextView>(R.id.tvMarkerPrice).text = price
		return view
	}

	private fun renderViewToBitmap(view: View): Bitmap {
		view.measure(
			View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
			View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
		)
		view.layout(0, 0, view.measuredWidth, view.measuredHeight)

		val bitmap = createBitmap(view.measuredWidth, view.measuredHeight)
		val canvas = Canvas(bitmap)
		view.draw(canvas)
		return bitmap
	}

	private fun addSoftShadow(
		source: Bitmap,
		blur: Float = 6f,
		dx: Float = 0f,
		dy: Float = 2f,
		shadowColor: Int = 0x33000000
	): Bitmap {
		val pad = (blur * 2).roundToInt()
		val out = createBitmap(source.width + pad * 2, source.height + pad * 2)
		val canvas = Canvas(out)

		val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = shadowColor
			maskFilter = BlurMaskFilter(blur, BlurMaskFilter.Blur.NORMAL)
		}

		val offset = IntArray(2)
		val alpha = source.extractAlpha(shadowPaint, offset)

		canvas.drawBitmap(
			alpha,
			pad + offset[0] + dx,
			pad + offset[1] + dy,
			shadowPaint
		)
		canvas.drawBitmap(source, pad.toFloat(), pad.toFloat(), null)

		alpha.recycle()
		return out
	}
}