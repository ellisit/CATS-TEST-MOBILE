package com.example.catsbankaccount.ui.myaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catsbankaccount.R
import com.example.catsbankaccount.databinding.MyaccountFragmentsBinding
import com.example.catsbankaccount.model.Account
import com.example.catsbankaccount.model.Operation

class MyBankAccountFragment : Fragment() {
    private var binding: MyaccountFragmentsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.myaccount_fragments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MyaccountFragmentsBinding.bind(view)
        val activity = (requireActivity() as  AppCompatActivity)
        activity.setSupportActionBar(binding?.toolbarAccount)
        if (activity.supportActionBar != null) {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = "Mes comptes"
        }

        binding?.toolbarAccount?.setNavigationOnClickListener() {
            onBackPressed()
        }

        val account = requireArguments().getParcelable<Account>("account")
        val accountSorted = sortOperations(account!!.operations)
        val accountAdapter = AccountAdapter(accountSorted)
        binding?.rvOrder?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvOrder?.adapter = accountAdapter
        binding?.accountNameTv?.text = account.label
        binding?.balanceTv?.text = "${account.balance} €"
    }

    private fun onBackPressed() {
        findNavController().popBackStack()
    }

    private fun sortOperations(operations: List<Operation>): List<Operation> {
        return operations.sortedWith(compareByDescending<Operation> { it.date }
            .thenBy { it.title })
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}