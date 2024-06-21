package com.salya.savorcapstone.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.salya.savorcapstone.R
import com.salya.savorcapstone.helper.ObjectDetectorHelper
import com.salya.savorcapstone.helper.ObjectDetectorHelper.DetectorListener
import com.salya.savorcapstone.data.response.CreateFridgeItemRequest
import com.salya.savorcapstone.data.response.CreateFridgeItemResponse
import com.salya.savorcapstone.data.api.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class InputFromGalleryActivity : AppCompatActivity(), DetectorListener {

    private lateinit var imageView: ImageView
    private lateinit var objectDetectorHelper: ObjectDetectorHelper
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2
    private val REQUEST_IMAGE_FROM_CAMERA_ACTIVITY = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_from_gallery)

        imageView = findViewById(R.id.imageView5)

        objectDetectorHelper = ObjectDetectorHelper(context = this, detectorListener = this)

        setupButtons()
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.button9)?.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivityForResult(intent, REQUEST_IMAGE_FROM_CAMERA_ACTIVITY)
        }

        findViewById<Button>(R.id.button10)?.setOnClickListener {
            val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_GALLERY)
        }

        findViewById<Button>(R.id.button11)?.setOnClickListener {
            imageView.drawable?.let { drawable ->
                val bitmap = (drawable as BitmapDrawable).bitmap
                objectDetectorHelper.detectImage(bitmap)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_GALLERY -> {
                    val imageUri: Uri? = data?.data
                    imageUri?.let {
                        val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, it)
                        imageView.setImageBitmap(imageBitmap)
                    }
                }
                REQUEST_IMAGE_FROM_CAMERA_ACTIVITY -> {
                    val imagePath = data?.getStringExtra("image_path")
                    imagePath?.let {
                        val imageFile = File(it)
                        if (imageFile.exists()) {
                            val imageBitmap = BitmapFactory.decodeFile(it)
                            imageView.setImageBitmap(imageBitmap)
                        }
                    }
                }
            }
        }
    }

    override fun onResults(detectedObject: String?, inferenceTime: Long) {
        detectedObject?.let {
            sendDetectedObjectToBackend(it)
        }
    }

    override fun onError(error: String) {
        showAlert("Error", error)
    }

    private fun sendDetectedObjectToBackend(detectedObject: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://us-central1-savor-be.cloudfunctions.net/api") // Replace with your actual base URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(ApiService::class.java)
                val request = CreateFridgeItemRequest(detectedObject)
                val token = "Your_Auth_Token" // Replace with actual token
                val response = apiService.createFridgeItem(token, request)

                withContext(Dispatchers.Main) {
                    navigateToDisplayResultActivity(response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showAlert("Error", e.message ?: "Unknown error")
                }
            }
        }
    }

    private fun navigateToDisplayResultActivity(response: CreateFridgeItemResponse) {
        val intent = Intent(this, ViewActivity::class.java).apply {
            putExtra("id", response.id)
            putExtra("category", response.category)
            putExtra("daysCountExpire", response.daysCountExpire)
            putExtra("userId", response.userId)
            putExtra("createdAt", response.createdAt)
        }
        startActivity(intent)
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}

