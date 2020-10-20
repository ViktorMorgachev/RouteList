package com.sedi.routelist.ui.dialogfragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sedi.routelist.R
import com.sedi.routelist.commons.LOG_LEVEL
import com.sedi.routelist.commons.log
import com.sedi.routelist.models.Language
import ru.sedi.customerclient.adapters.LanguageAdapter

class ChooseLanguageDialog(
    val items: ArrayList<Language>,
    val clickCallback: LanguageAdapter.ClickCallback
) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       return SelectLanguageDialog(requireContext(), items, clickCallback)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    inner class SelectLanguageDialog(
        context: Context,
        private val items: ArrayList<Language>,
        val clickCallback: LanguageAdapter.ClickCallback
    ) : Dialog(context), View.OnClickListener {


        var recyclerView: RecyclerView? = null
        private var mLayoutManager: RecyclerView.LayoutManager? = null


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.dialog_shoose_language)
            updateDialogWindow()
            recyclerView = findViewById(R.id.recycler_view)
            mLayoutManager = LinearLayoutManager(context)
            recyclerView?.layoutManager = mLayoutManager

            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            val mLayoutManager = LinearLayoutManager(context)
            recyclerView?.layoutManager = mLayoutManager
            recyclerView?.adapter = LanguageAdapter(context, items, object :
                LanguageAdapter.ClickCallback {
                override fun onClicked(language: String) {
                    clickCallback.onClicked(language)
                    dismiss()
                }
            })

        }

        private fun updateDialogWindow() {
            val attributes = window!!.attributes
            attributes.width = WindowManager.LayoutParams.WRAP_CONTENT
            window!!.attributes = attributes
        }

        override fun onClick(v: View?) {
            dismiss()
        }

    }

    override fun onDestroy() {
        log(LOG_LEVEL.INFO, "onDestroy()")
        clickCallback.onClicked("")
        super.onDestroy()
    }
}