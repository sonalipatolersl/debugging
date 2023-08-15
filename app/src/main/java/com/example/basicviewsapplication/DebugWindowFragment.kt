package com.example.basicviewsapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.basicviewsapplication.databinding.FragmentDebugWindowDemoBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlin.concurrent.thread
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DebugWindowFragment : Fragment() {

    private var _binding: FragmentDebugWindowDemoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var uniqueNumbers = mutableListOf<Int>()
    private val mutex = Mutex()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDebugWindowDemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.multiThreadingDemo.setOnClickListener {
            generateUniqueList()
        }

        binding.traceObjectDemo.setOnClickListener {
            findNavController().navigate(R.id.action_DebugWindowFragment_to_TransactionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generateUniqueList() {
        uniqueNumbers = mutableListOf()
        val thread1 = thread {
            for (i in 1..5) {
                val number = Random.nextInt(1, 5)
                //synchronized(mutex) {
                    addIfNotPresent(number)
               // }
            }
        }

        val thread2 = thread {
            for (i in 1..5) {
                val number = Random.nextInt(1, 5)
                //synchronized(mutex) {
                    addIfNotPresent(number)
               // }
            }
        }

        thread1.join()
        thread2.join()

        Log.d("DebugWindowFragment", "Unique list = $uniqueNumbers") // The expected value is 20000, but due to the race condition, it might be different.
    }

    private fun addIfNotPresent(number: Int) {
        if (!uniqueNumbers.contains(number)) {
            uniqueNumbers.add(number)
        }
    }

    private fun generateUniqueListSynchronously() = runBlocking {
        val thread1 = launch {
            for (i in 1..5) {
                val number = Random.nextInt(100)
                synchronized(mutex) {
                    addIfNotPresent(number)
                }
            }
        }

        val thread2 = launch {
            for (i in 1..5) {
                val number = Random.nextInt(100)
                synchronized(mutex) {
                    addIfNotPresent(number)
                }
            }
        }

        thread1.join()
        thread2.join()

        Log.d("DebugWindowFragment", "Unique list = $uniqueNumbers") // The expected value is 20000, but due to the race condition, it might be different.
    }
}