package com.bkcreate.top10downloader

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.bkcreate.top10downloader.Adapter.FeedAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.properties.Delegates

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val downloadData by lazy {DownloadData(this,xmlListView)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called")

        //RSS Feed Link
        var appleUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"
        appleUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topTvEpisodes/xml"
        //val DownloadData = DownloadData(this, xmlListView)
        downloadData.execute(appleUrl)
        Log.d(TAG,"OnCreate: done")
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData.cancel(true)
    }

    companion object {
        private class DownloadData(context: Context, listView: ListView): AsyncTask<String, Void, String>(){

            private val TAG = "DownloadData"

            var propcontext : Context by Delegates.notNull()
            var propListView : ListView by Delegates.notNull()

            init {
                propcontext = context
                propListView = listView
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                //Log.d(TAG, "onPostExecute: parameter is $result")

                val parseApplications = ParseApplications()
                parseApplications.parse(result)


                //val arrayAdapter = ArrayAdapter<FeedEntry>(propcontext, R.layout.list_item, parserApplications.applications)
                //propListView.adapter = arrayAdapter
                val feedAdapter = FeedAdapter(propcontext, R.layout.list_record , parseApplications.applications)
                propListView.adapter = feedAdapter

            }
            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground: starts with ${url[0]}")
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()){
                    Log.e(TAG,"doInBackground: Error downloading")
                }
                return rssFeed
            }

            private fun downloadXML(urlPath: String?):String{
                return URL(urlPath).readText()
            }

            /*
            private fun downloadXML(urlPath: String?): String{
                val xmlResult = StringBuilder()
                try {
                    val url = URL(urlPath)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    val response = connection.responseCode
                    Log.d(com.bkcreate.top10downloader.TAG, "downloadXML: The response code was $response")

                    connection.inputStream.buffered().reader().use{ xmlResult.append(it.readText()) }

                    Log.d(TAG,"Received ${xmlResult.length} bytes")
                    return xmlResult.toString()
                } catch(e: Exception){
                    val exception: String = when (e) {
                        is MalformedURLException -> "downloadXML: URL Format ${e.message}"
                        is IOException -> "downloadXML: IO Exception reading data: ${e.message}"
                        is SecurityException -> {
                            e.printStackTrace()
                            "downloadXML: Security exception. No internet permission given? ${e.message}"
                        }
                        else -> "Unknown Error: ${e.message}"
                    }
                    Log.e(TAG, exception)
                }
                return "" //Wenn hier, dann ist Fehler aufgetreten.
            }
            */

        }
    }

}

class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String= ""

    /*
    override fun toString(): String {
        return """
            name = $name
            artist = $artist
            releaseDate = $releaseDate
            imageURL = $imageURL
        """.trimIndent()
    }*/
}