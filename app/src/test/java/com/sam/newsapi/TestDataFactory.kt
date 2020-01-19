package com.sam.newsapi

import com.google.gson.Gson
import com.sam.newsapi.data.newsapi.model.NewsModel

object TestDataFactory{

    val remotejson = "{\n" +
            "  \"status\": \"ok\",\n" +
            "  \"totalResults\": 38,\n" +
            "  \"articles\": [\n" +
            "    {\n" +
            "      \"source\": {\n" +
            "        \"id\": \"nbc-news\",\n" +
            "        \"name\": \"NBC News\"\n" +
            "      },\n" +
            "      \"author\": \"Phil Helsel\",\n" +
            "      \"title\": \"Ex-Guantanamo Bay commander convicted of hindering inquiry into man's death - NBC News\",\n" +
            "      \"description\": \"The former commander of Naval Station Guantanamo Bay was convicted Friday of hindering an investigation into the death of a loss prevention manager at the facility.\",\n" +
            "      \"url\": \"https://www.nbcnews.com/news/us-news/ex-guantanamo-bay-commander-convicted-hindering-inquiry-man-s-death-n1118266\",\n" +
            "      \"urlToImage\": \"https://media1.s-nbcnews.com/j/newscms/2020_03/858756/150122-capt-nettleon-guantanamo-inline_b82914c318e26e16f46dda35d6823fb8.nbcnews-fp-1200-630.jpg\",\n" +
            "      \"publishedAt\": \"2020-01-18T04:41:00Z\",\n" +
            "      \"content\": \"The former commander of Naval Station Guantanamo Bay was convicted Friday of hindering an inquiry into the 2015 death of a civilian who had confronted the commander about a possible extramarital affair with his wife.\\r\\nJohn Nettleton, 54, of Jacksonville, Flor… [+2624 chars]\"\n" +
            "    }\n" +
            "  ]\n" +
            "}"

    fun makeNewsModel(): NewsModel {
        return Gson().fromJson(remotejson, NewsModel::class.java)
    }

}