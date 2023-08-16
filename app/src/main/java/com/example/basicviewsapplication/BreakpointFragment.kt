package com.example.basicviewsapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.basicviewsapplication.databinding.FragmentBreakpointDemoBinding
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BreakpointFragment : Fragment() {

    private var _binding: FragmentBreakpointDemoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private val refreshRunnable = object : Runnable {
        override fun run() {
            refreshScreen()

            // Re-post the Runnable after 1 milliseconds
            handler.postDelayed(this, 1000) // 1 milliseconds
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreakpointDemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // region Sort button click handling
        binding.sort.setOnClickListener {
            val randomValues = MutableList(15) {
                Random.nextInt(1, 10001)
            }
            println("Unsorted values: $randomValues")

            sortList(randomValues)

            println("Sorted values: $randomValues")
        }
        // endregion

        // region Auto-refresh functionality
        handler.post(refreshRunnable)
        // endregion

        // region Refresh screen click handling
        binding.refreshScreen.setOnClickListener {
            refreshScreen()
        }
        // endregion

        // region Load image click handling
        binding.loadImage.setOnClickListener {
            loadImage(binding.imageView)
        }
        // endregion
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(refreshRunnable)
        _binding = null
    }

    private fun sortList(arr: MutableList<Int>): List<Int> {
        val n = arr.size
        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                if (arr[j] > arr[j + 1]) {
                    val temp = arr[j]
                    arr[j] = arr[j + 1]
                    arr[j + 1] = temp
                }
            }
        }
        return arr
    }

    private fun refreshScreen(): Boolean {
        Log.d("MainFragment", "Refreshing screen")
        return true
    }

    private fun loadImage(imageView: ImageView) {
        val imageUrl = ImageUrl.IMAGE_4_3.url // Replace with your image URL

        Glide.with(this)
            .load(imageUrl)
            .into(imageView)
    }

    enum class ImageUrl(val url: String) {
        IMAGE_16_9("https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Aspect_ratio_-_16x9.svg/320px-Aspect_ratio_-_16x9.svg.png"),
        IMAGE_4_3("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Aspect_ratio_-_4x3.svg/1200px-Aspect_ratio_-_4x3.svg.png"),
        IMAGE_3_2("https://upload.wikimedia.org/wikipedia/commons/d/de/Aspect_ratio_-_3x2.svg"),
    }
}