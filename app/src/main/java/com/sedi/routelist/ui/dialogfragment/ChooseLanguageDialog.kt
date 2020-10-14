package com.sedi.routelist.ui.dialogfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sedi.routelist.R
import com.sedi.routelist.models.Language
import ru.sedi.customerclient.adapters.LanguageAdapter

class ChooseLanguageDialog(
    val items: ArrayList<Language>,
    val clickCallback: LanguageAdapter.ClickCallback
) : DialogFragment() {
    var recyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.circle_corner_accent);
        val view: View = inflater.inflate(R.layout.dialog_shoose_language, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        mLayoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager = mLayoutManager

        recyclerView?.adapter = LanguageAdapter(requireContext(), items, object :
            LanguageAdapter.ClickCallback {
            override fun onClicked(language: String) {
                clickCallback.onClicked(language)
                dismiss()
            }
        })

        return inflater.inflate(R.layout.dialog_shoose_language, container, false)
    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}