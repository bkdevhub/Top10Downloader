package com.bkcreate.top10downloader.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bkcreate.top10downloader.FeedEntry
import com.bkcreate.top10downloader.R
import kotlinx.android.synthetic.main.list_record.view.*

class FeedAdapter(context: Context, val resource: Int, val applications: List<FeedEntry>): ArrayAdapter<FeedEntry>(context, resource) {
    private val TAG = "FeedAdapter"
    private val inflater = LayoutInflater.from(context)


    override fun getCount(): Int {
        Log.d(TAG, "Called getCount()")
        return applications.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d(TAG, "getView() called")
        val view = inflater.inflate(resource, parent, false)

        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvArtist: TextView = view.findViewById(R.id.tvArtist)
        val tvSummary: TextView = view.findViewById(R.id.tvSummary)

        val currentApp = applications[position]

        tvName.text = currentApp.name
        tvArtist.text = currentApp.artist
        tvSummary.text = currentApp.summary


        Log.d(TAG, "Artist: ${currentApp.artist}")
        Log.d(TAG, "Summary : ${currentApp.summary}")

        return view
    }
}