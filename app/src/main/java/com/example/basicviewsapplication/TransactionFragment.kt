package com.example.basicviewsapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.basicviewsapplication.databinding.FragmentTransactionBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var currentAccount: BankAccount

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentAccount = openAccount()
        binding.deposit.setOnClickListener {
            currentAccount.deposit(binding.enterAmount.text.toString().toDouble())
        }

        binding.withdraw.setOnClickListener {
            currentAccount.withdraw(binding.enterAmount.text.toString().toDouble())
        }
    }

    private fun openAccount(initialValue: Double = 1000.0): BankAccount {
        return BankAccount().apply { deposit(initialValue) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}