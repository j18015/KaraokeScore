package com.example.karaokescore

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


/**
 * Created by shiozaki on 2017/02/06.
 */
class AsyncHttp(var date: String, var title: String, var singer: String, var score: String) : AsyncTask<String?, Int?, Boolean>() {
    var urlConnection: HttpURLConnection? = null
    var flg = false
    //非同期処理ここから
    protected override fun doInBackground(vararg params: String?): Boolean? {
        val urlinput = "http://192.168.0.7/upload_kadai/post.php"
        try {
            val url = URL(urlinput)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection!!.requestMethod = "POST"
            urlConnection!!.doOutput = true
            //POST用パラメータ
            val postDataSample = "date=$date&title=$title&singer=$singer&score=$score"
            //POSTパラメータ設定
            val out = urlConnection!!.outputStream
            out.write(postDataSample.toByteArray())
            out.flush()
            out.close()
            Log.d("test", postDataSample)
            //レスポンスを受け取る
            urlConnection!!.inputStream
            flg = true
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return flg
    }

}