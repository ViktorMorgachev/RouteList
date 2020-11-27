package com.sedi.routelist.ui.dialogfragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sedi.routelist.R
import com.sedi.routelist.models.Language
import com.sedi.routelist.interfaces.IAction
import ru.sedi.customerclient.adapters.LanguageAdapter

class ChooseLanguageDialog(
    private val action: IAction
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return SelectLanguageDialog(
            requireContext(), arrayListOf(
                Language(R.drawable.ic_china, R.string.zh, "zh"),
                Language(R.drawable.ic_germany, R.string.de, "de"),
                Language(R.drawable.ic_russia, R.string.ru, "ru"),
                Language(R.drawable.ic_en, R.string.en, "en"),
                Language(R.drawable.ic_kyrgyzstan, R.string.ky, "ky")
            )
        )
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    inner class SelectLanguageDialog(
        context: Context,
        private val items: ArrayList<Language>
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
            recyclerView?.adapter = LanguageAdapter(context, items, object : IAction {
                override fun action() {
                    action.action()
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
            action.action()
            dismiss()
        }

    }
}