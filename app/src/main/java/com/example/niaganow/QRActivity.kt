package com.example.niaganow

import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

class QRActivity: AppCompatActivity() {

    private lateinit var qrCodeIV: ImageView
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        qrCodeIV = findViewById(R.id.idIVQrcode)
        backButton = findViewById(R.id.backBtn)

        backButton.setOnClickListener {
            finish()
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
    }

    private fun generateQRCode(text: String, merchantName: String) {
        val qrSize = 800
        val cardPadding = 60
        val captionHeight = 140
        val merchantNameHeight = 100
        val cornerRadius = 80f
        val pinkColor = android.graphics.Color.parseColor("#ED267C")

        try {
            val barcodeEncoder = BarcodeEncoder()
            val baseQR = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, qrSize, qrSize)

            // Create pink QR bitmap by replacing black with pink
            val pinkQR = Bitmap.createBitmap(qrSize, qrSize, Bitmap.Config.ARGB_8888)
            for (x in 0 until qrSize) {
                for (y in 0 until qrSize) {
                    pinkQR.setPixel(x, y,
                        if (baseQR.getPixel(x, y) == android.graphics.Color.BLACK) pinkColor
                        else android.graphics.Color.WHITE
                    )
                }
            }

            val totalHeight = qrSize + cardPadding * 2 + merchantNameHeight + captionHeight
            val totalWidth = qrSize + cardPadding * 2
            val finalBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(finalBitmap)
            val paint = android.graphics.Paint().apply { isAntiAlias = true }

            // Draw pink full background
            canvas.drawColor(pinkColor)

            // Draw white rounded card for QR
            paint.color = android.graphics.Color.WHITE
            val cardRect = android.graphics.RectF(
                cardPadding.toFloat(), cardPadding.toFloat(),
                (cardPadding + qrSize).toFloat(), (cardPadding + qrSize).toFloat()
            )
            canvas.drawRoundRect(cardRect, cornerRadius, cornerRadius, paint)

            // Draw pink QR code centered on card
            canvas.drawBitmap(pinkQR, cardPadding.toFloat(), cardPadding.toFloat(), null)

            // Draw Merchant Name (below QR)
            paint.color = android.graphics.Color.BLACK
            paint.textAlign = android.graphics.Paint.Align.CENTER
            paint.textSize = 50f
            paint.isFakeBoldText = true
            canvas.drawText(
                merchantName,
                (totalWidth / 2).toFloat(),
                (cardPadding + qrSize + 60).toFloat(),
                paint
            )

            // Draw bottom caption
            paint.color = android.graphics.Color.WHITE
            paint.textSize = 42f
            paint.isFakeBoldText = false
            canvas.drawText(
                "MALAYSIA NATIONAL QR",
                (totalWidth / 2).toFloat(),
                (totalHeight - captionHeight / 2 + 10).toFloat(),
                paint
            )

            // Set image to ImageView
            qrCodeIV.setImageBitmap(finalBitmap)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }


    private fun generateFakeDuitNowQR(
        payeeName: String,
        merchantId: String,
        amount: Double,
        reference: String)
    : String {
        return """
            DUITNOW|NAME:$payeeName
            MERCHANT_ID:$merchantId
            AMOUNT:%.2f
            REF:$reference
            TYPE:Dynamic
        """.trimIndent().format(amount)
    }
}