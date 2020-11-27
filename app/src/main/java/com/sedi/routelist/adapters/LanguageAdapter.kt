package ru.sedi.customerclient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.sedi.routelist.R
import com.sedi.routelist.models.Language
import com.sedi.routelist.models.PrefsManager
import com.sedi.routelist.interfaces.IAction

class LanguageAdapter(
    private val context: Context,
    private val items: ArrayList<Language>,
    private val action: IAction
) : RecyclerView.Adapter<LanguageAdapter.LanguageHolder>() {


    inner class LanguageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var Describe: TextView? = null
        var Flag: ImageView? = null
        var LL_Language: LinearLayout? = null


        init {
            Describe = itemView.findViewById(R.id.tv_flag_name)
            Flag = itemView.findViewById(R.id.iv_flag)
            LL_Language = itemView.findViewById(R.id.ll_language)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageHolder {
        return LanguageHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.language_item,
                parent,
                false
            )
        )
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: LanguageHolder, position: Int) {
        holder.Describe?.text = context.resources.getString(items[position].nameID)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            holder.Flag?.setImageDrawable(
                ResourcesCompat.getDrawable(context.resources,
                    items[position].iconID,
                    context.theme
                )
            )
        } else{
            holder.Flag?.setImageDrawable(
                ResourcesCompat.getDrawable(context.resources,
                    items[position].iconID,
                    context.theme
                )
            )
        }
        holder.Describe?.setOnClickListener {
            saveLanguage(items[position].languageTag)
        }
        holder.Flag?.setOnClickListener {
            saveLanguage(items[position].languageTag)
        }

        holder.LL_Language?.setOnClickListener {
            saveLanguage(items[position].languageTag)
        }
    }

    private fun saveLanguage(lang: String) {
        PrefsManager.getIntance(context)
            .setValue(PrefsManager.PrefsKey.LOCALE, lang)
        action.action()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}