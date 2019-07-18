package com.bkcreate.top10downloader

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

//private const val TAG = "PARSE_APPLICATIONS"

class ParseApplications {
    private val TAG = "ParseApplications"
    val applications = ArrayList<FeedEntry>()


    fun parse(xmlData: String):Boolean{
        Log.d(TAG, "parse: Called with $xmlData")

        var status = true
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true // was macht das?
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())

            var eventType = xpp.eventType
            var currentRecord = FeedEntry() //Was ist feed entry?
            while (eventType != XmlPullParser.END_DOCUMENT){
                val tagName = xpp.name?.toLowerCase()
                when (eventType){
                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "parse: Starting tag for + $tagName" )
                        if (tagName=="entry"){
                            inEntry = true
                        }
                    }
                    XmlPullParser.TEXT -> textValue = xpp.text
                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "parse: Ending tag for $tagName")
                        if (inEntry) {
                            when (tagName){
                                "entry"-> {
                                    applications.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry() //Create a new Object

                                }
                                "name" -> currentRecord.name = textValue
                                "artist" -> currentRecord.artist = textValue
                                "releasedate" -> currentRecord.releaseDate = textValue
                                "summary" -> {
                                    Log.d(TAG, "Show summary: $textValue")
                                    currentRecord.summary = textValue
                                }
                                "image" -> currentRecord.imageURL = textValue

                            }
                        }
                    }
                }
                eventType = xpp.next()
            }
            for (app in applications){
                Log.d (TAG, "*******************")
                Log.d(TAG, app.toString())
            }
        } catch (e: Exception){
            e.printStackTrace()
            status = false
        }
        return status

    }
}