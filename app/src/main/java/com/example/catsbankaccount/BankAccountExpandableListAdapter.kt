package com.example.catsbankaccount

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.catsbankaccount.model.DataPreview

class BankAccountExpandableListAdapter(
    private val context: Context,
    private val bankNameList: List<DataPreview>,
    private val dataList: HashMap<String, List<DataPreview>>
) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataList[this.bankNameList[listPosition].name]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

//    fais une mmethode qui prend en entre une liste de banquee et qui retourne une hashmmap avec le nom de la banque en tant que clé et une liste dee data preview

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val account = getChild(listPosition, expandedListPosition) as DataPreview
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_bank, null)
        }
        val expandedListAccountHolderTextView = convertView!!.findViewById<TextView>(R.id.listView)
        val expandedListAccountBalanceTextView = convertView.findViewById<TextView>(R.id.balanceTv)
        expandedListAccountHolderTextView.text = account.name
        expandedListAccountBalanceTextView.text = "${account.balance} €"
        val layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.weight = 1f
        layoutParams.setMargins(30, 0, 0, 0)
        expandedListAccountHolderTextView.layoutParams = layoutParams
        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[this.bankNameList[listPosition].name]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.bankNameList[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.bankNameList.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val dataPreview = getGroup(listPosition) as DataPreview
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_bank, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listView)
        listTitleTextView.text = dataPreview.name
        val groupIndicator = convertView!!.findViewById<ImageView>(R.id.groupIndicator)
        val listAccountBalanceTextView = convertView.findViewById<TextView>(R.id.balanceTv)

        listAccountBalanceTextView.text = "${dataPreview.balance} €"

        if (isExpanded){
            groupIndicator.setImageResource(R.drawable.expand_more_24px)
        }else {
            groupIndicator.setImageResource(R.drawable.chevron_right_24px)

        }
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}