package com.example.niaganow.fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.niaganow.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

class QRFragment : Fragment() {

    private lateinit var qrCodeIV: ImageView
    private lateinit var backButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_qr, container, false)

        qrCodeIV = view.findViewById(R.id.idIVQrcode)
        backButton = view.findViewById(R.id.backBtn)

        backButton.setOnClickListener {
            // Optional: Handle fragment pop or activity back
            activity?.onBackPressed()
        }

        generateQRCode(
            generateFakeDuitNowQR(
                payeeName = "Johnson Johnson SDN BHD",
                merchantId = "D1234567890",
                amount = 88.88,
                reference = "TXN" + System.currentTimeMillis().toString()
            ),
            merchantName = "Johnson Johnson SDN BHD"
        )

        return view
    }

    private fun generateQRCode(text: String, merchantName: String) {
        val qrSize = 800
        val cardPadding = 60
        val captionHeight = 140
        val merchantNameHeight = 100
        val cornerRadius = 80f
        val pinkColor = Color.parseColor("#ED267C")

        try {
            val barcodeEncoder = BarcodeEncoder()
            val baseQR = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, qrSize, qrSize)

            val pinkQR = Bitmap.createBitmap(qrSize, qrSize, Bitmap.Config.ARGB_8888)
            for (x in 0 until qrSize) {
                for (y in 0 until qrSize) {
                    pinkQR.setPixel(
                        x, y,
                        if (baseQR.getPixel(x, y) == Color.BLACK) pinkColor
                        else Color.WHITE
                    )
                }
            }

            val totalHeight = qrSize + cardPadding * 2 + merchantNameHeight + captionHeight
            val totalWidth = qrSize + cardPadding * 2
            val finalBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(finalBitmap)
            val paint = Paint().apply { isAntiAlias = true }

            canvas.drawColor(pinkColor)

            paint.color = Color.WHITE
            val cardRect = RectF(
                cardPadding.toFloat(), cardPadding.toFloat(),
                (cardPadding + qrSize).toFloat(), (cardPadding + qrSize).toFloat()
            )
            canvas.drawRoundRect(cardRect, cornerRadius, cornerRadius, paint)

            canvas.drawBitmap(pinkQR, cardPadding.toFloat(), cardPadding.toFloat(), null)

            paint.color = Color.BLACK
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 50f
            paint.isFakeBoldText = true
            canvas.drawText(
                merchantName,
                (totalWidth / 2).toFloat(),
                (cardPadding + qrSize + 60).toFloat(),
                paint
            )

            paint.color = Color.WHITE
            paint.textSize = 42f
            paint.isFakeBoldText = false
            canvas.drawText(
                "MALAYSIA NATIONAL QR",
                (totalWidth / 2).toFloat(),
                (totalHeight - captionHeight / 2 + 10).toFloat(),
                paint
            )

            qrCodeIV.setImageBitmap(finalBitmap)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    private fun generateFakeDuitNowQR(
        payeeName: String,
        merchantId: String,
        amount: Double,
        reference: String
    ): String {
        return """
            DUITNOW|NAME:$payeeName
            MERCHANT_ID:$merchantId
            AMOUNT:%.2f
            REF:$reference
            TYPE:Dynamic
        """.trimIndent().format(amount)
    }
}