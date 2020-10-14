package ru.sedi.customerclient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sedi.routelist.R
import com.sedi.routelist.models.Language

class LanguageAdapter(
    private val context: Context,
    private val items: ArrayList<Language>,
    private val clickCallback: ClickCallback
) : RecyclerView.Adapter<LanguageAdapter.LanguageHolder>() {


    inner class LanguageHolder : RecyclerView.ViewHolder {

        var Describe: TextView? = null
        var Flag: ImageView? = null
        var LL_Language: LinearLayout? = null


        constructor(itemView: View) : super(itemView) {
            Describe = itemView.findViewById(R.id.tv_flag_name)
            Flag = itemView.findViewById(R.id.iv_flag)
            LL_Language = itemView.findViewById(R.id.ll_language)
        }

    }

    interface ClickCallback {
        fun onClicked(language: String);
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
                context.resources.getDrawable(
                    items[position].iconID,
                    context.theme
                )
            )
        } else{
            holder.Flag?.setImageDrawable(
                context.resources.getDrawable(
                    items[position].iconID
                )
            )
        }

        holder.Describe?.setOnClickListener {
            clickCallback.onClicked(items[position].languageTag)
        }
        holder.Flag?.setOnClickListener {
            clickCallback.onClicked(items[position].languageTag)
        }

        holder.LL_Language?.setOnClickListener {
            clickCallback.onClicked(items[position].languageTag)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}