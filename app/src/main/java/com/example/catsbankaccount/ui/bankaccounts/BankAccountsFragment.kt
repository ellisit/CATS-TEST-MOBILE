package com.example.catsbankaccount.ui.bankaccounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.catsbankaccount.BankAccountExpandableListAdapter
import com.example.catsbankaccount.R
import com.example.catsbankaccount.databinding.AccountsFragmentBinding
import com.example.catsbankaccount.model.Bank
import com.example.catsbankaccount.model.DataPreview


class BankAccountsFragment : Fragment() {

    private var otherBankElv: ExpandableListView? = null
    private val bankAccountsViewModel: BankAccountsViewModel by lazy {
        ViewModelProvider(this).get(
            BankAccountsViewModel::class.java
        )
    }
    private var caExpandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var binding: AccountsFragmentBinding? = null
    private var banksPreview: List<DataPreview>? = null
    private var accountsPreview: HashMap<String, List<DataPreview>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.accounts_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AccountsFragmentBinding.bind(view)
        bankAccountsViewModel.data.observe(viewLifecycleOwner) { updateUi(it) }
    }

    private fun updateUi(bankList: Pair<List<Bank>, List<Bank>>) {
        handleCaElv(bankList.first)
        handleOtherBankElv(bankList.second)
    }


    private fun handleCaElv(caBank: List<Bank>) {
        caExpandableListView = binding?.elvCaBank
        if (caExpandableListView != null) {
            accountsPreview = bankAccountsViewModel.getAccountPreviewsByBank(caBank)
            banksPreview = bankAccountsViewModel.getBankBalances(accountsPreview!!)
            val accountList = bankAccountsViewModel.getAllAccountsFromBanks(caBank)
            adapter = BankAccountExpandableListAdapter(
                requireContext(),
                banksPreview!!,
                accountsPreview!!
            )
            caExpandableListView!!.setAdapter(adapter)
            caExpandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->


                val account = accountList.get(childPosition)
                val action = BankAccountsFragmentDirections.actionFtAccountsToFtMyAccount(account)
                findNavController().navigate(action)
                false
            }
        }
    }

    private fun handleOtherBankElv(otherBank: List<Bank>) {
        otherBankElv = binding?.elvOtherBank
        if (otherBankElv != null) {
            accountsPreview = bankAccountsViewModel.getAccountPreviewsByBank(otherBank)
            banksPreview = bankAccountsViewModel.getBankBalances(accountsPreview!!)
            val accountList = bankAccountsViewModel.getAllAccountsFromBanks(otherBank)
            adapter = BankAccountExpandableListAdapter(
                requireContext(),
                banksPreview!!,
                accountsPreview!!
            )
            otherBankElv!!.setAdapter(adapter)

            otherBankElv!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->

                val account = accountList.get(childPosition)
                val action = BankAccountsFragmentDirections.actionFtAccountsToFtMyAccount(account)
                findNavController().navigate(action)
                false
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}