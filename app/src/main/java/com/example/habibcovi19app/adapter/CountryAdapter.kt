package com.example.habibcovi19app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Habibcovid19app.network.Countries
import com.example.habibcovi19app.R
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CountryAdapter(val country: ArrayList<Countries>, val clickListener: (Countries) -> Unit):


    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(),

    Filterable {

    var countryFilterList = ArrayList<Countries>()
    init {
        countryFilterList = country
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CountryAdapter.CountryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_country, parent, false))

    override fun getItemCount() = countryFilterList.size

    override fun onBindViewHolder(holder: CountryAdapter.CountryViewHolder, position: Int) {
        holder.bindItem(countryFilterList[position], clickListener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                countryFilterList = if (charSearch.isEmpty()){
                    country
                } else{
                    val resultList = ArrayList<Countries>()
                    for (row in country){
                        if (row.Country.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(
                                Locale.ROOT))){
                            resultList.add(row)
                        }
                    }
                    resultList
                }

                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<Countries>
                notifyDataSetChanged()
            }

        }
    }

    class CountryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val tvCountry = itemView.findViewById<TextView>(R.id.txt_country_name)
        val tvTotalCase = itemView.findViewById<TextView>(R.id.txt_total_cases)
        val tvTotalRecovered = itemView.findViewById<TextView>(R.id.txt_total_recovered)
        val tvTotalDeath = itemView.findViewById<TextView>(R.id.txt_total_deaths)
        val imgFlag = itemView.findViewById<ImageView>(R.id.img_flag_country)

        fun bindItem(countries: Countries, clickListener: (Countries) -> Unit){
            tvCountry.text = countries.Country

            val formatter: NumberFormat = DecimalFormat("#,###")
            tvTotalCase.text = formatter.format(countries.TotalConfirmed.toDouble())
            tvTotalRecovered.text = formatter.format(countries.TotalRecovered.toDouble())
            tvTotalDeath.text = formatter.format(countries.TotalDeaths.toDouble())
            itemView.setOnClickListener { clickListener(countries) }

            Glide.with(itemView.context)
                .load("https://www.countryflags.io/" + countries.CountryCode + "/flat/16.png")
                .into(imgFlag)
            itemView.setOnClickListener { clickListener(countries) }
        }
    }
}