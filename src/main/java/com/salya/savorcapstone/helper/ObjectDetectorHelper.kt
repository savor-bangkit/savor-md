import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import com.salya.savorcapstone.R
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class ObjectDetectorHelper(
    private var threshold: Float = 0.5f,
    private var maxResults: Int = 5,
    private val modelName: String = "best_float32.tflite",
    private val context: Context,
    private val detectorListener: DetectorListener?
) {
    private var objectDetector: ObjectDetector? = null

    init {
        loadModel()
    }

    private fun loadModel() {
        try {
            val optionsBuilder = ObjectDetector.ObjectDetectorOptions.builder()
                .setScoreThreshold(threshold)
                .setMaxResults(maxResults)
            val baseOptionsBuilder = BaseOptions.builder()
                .setNumThreads(4)
            optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

            objectDetector = ObjectDetector.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: Exception) {
            detectorListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, "Failed to load object detector model", e)
        }
    }

    fun detectImage(bitmap: Bitmap) {
        if (objectDetector == null) {
            loadModel()
            return
        }

        val tensorImage = TensorImage.fromBitmap(bitmap)

        val inferenceTime = SystemClock.uptimeMillis()
        try {
            val results = objectDetector?.detect(tensorImage)
            val detectionTime = SystemClock.uptimeMillis() - inferenceTime
            val detectedObjectName = results?.firstOrNull()?.categories?.firstOrNull()?.label
            detectorListener?.onResults(detectedObjectName, detectionTime)
        } catch (e: Exception) {
            Log.e(TAG, "Object detection error", e)
            detectorListener?.onError("Object detection error: ${e.message}")
        }
    }

    interface DetectorListener {
        fun onError(error: String)
        fun onResults(detectedObject: String?, inferenceTime: Long)
    }

    companion object {
        private const val TAG = "ObjectDetectorHelper"
    }
}


