package com.nevermore.ui


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nevermore.R
import kotlinx.android.synthetic.main.fragment_bitmap.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BitmapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bitmap, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.kangla)
        iv_origin.setImageBitmap(bitmap)

//        iv_create.setImageBitmap(createBitmap(bitmap, 5))
        GlobalScope.launch {
            val createBitmap = createBitmap(bitmap, 5)
            launch(Dispatchers.Main) {
                iv_create.setImageBitmap(createBitmap)
            }
        }

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar ?: return
                GlobalScope.launch {
                    printThread()
                    val createBitmap = createBitmap(bitmap, seekBar.progress)
                    launch(Dispatchers.Main) {
                        iv_create.setImageBitmap(createBitmap)
                        printThread()
                    }
                }
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

        })
        printThread()
        //save image
        iv_create.setOnClickListener {

            val drawable = iv_create.drawable
            if (drawable is BitmapDrawable) {
                val bmp = drawable.bitmap

                GlobalScope.launch {
                    context?.let {
                        val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINA)
                        val file =
                            File(it.externalMediaDirs[0], simpleDateFormat.format(Date()) + ".jpg")
                        val outputStream = FileOutputStream(file)
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        //scan
                        MediaScannerConnection.scanFile(it, arrayOf(file.absolutePath), null, null)
                        launch(Dispatchers.Main) {
                            Toast.makeText(it, "save:" + file.absolutePath, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            }

        }
    }

    fun printThread() {
        val name = Thread.currentThread().name
        Log.i("xct", name)
    }

    private suspend fun createBitmap(bitmap: Bitmap?, per: Int = 5): Bitmap? {

        if (per == 0) {
            return bitmap
        }
        bitmap?.apply {
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            val canvas = Canvas(bmp)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.style = Paint.Style.FILL
            for (w in 0 until width) {
                if (w % per == 0) {
                    for (h in 0 until height) {
                        if (h % per == 0) {
                            val pixel = getPixel(w, h)
                            paint.color = pixel
                            canvas.drawCircle(w.toFloat(), h.toFloat(), per / 2f, paint)
                        }
                    }
                }
            }
            return bmp
        }

        return null
    }


}
