package com.example.karaokescore

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_score_edit.*
import java.lang.IllegalArgumentException
//import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import  android.text.format.DateFormat

class ScoreEditActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    var save:Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_edit)
        realm = Realm.getDefaultInstance()

        val scoreId = intent?.getLongExtra("score_id", -1L)
        if(scoreId != -1L){
            val  score = realm.where<Score>()
                .equalTo("id",scoreId).findFirst()
            dateEdit.setText(DateFormat.format("yyyy/MM/dd", score?.date))
            titleEdit.setText(score?.title)
            singerEdit.setText(score?.singer)
            scoreEdit.setText(score?.Score)
            delete.visibility = View.VISIBLE
        }else{
            delete.visibility = View.INVISIBLE
        }

        save = findViewById(R.id.save) as Button
        //「保存」ボタンが押された時の処理
        save!!.setOnClickListener{
            when(scoreId){-1L->
                {

                    realm.executeTransaction{db: Realm ->
                        val maxId = db.where<Score>().max("id")
                        val nextId = (maxId?.toLong() ?: 0L) + 1
                        val score = db.createObject<Score>(nextId)
                        val date = dateEdit.text.toString().toDate("yyyy/MM/dd")
                        if(date != null){
                            score.date = date
                        }
                        score.title = titleEdit.text.toString()
                        score.singer = singerEdit.text.toString()
                        score.Score = scoreEdit.text.toString()
                    }
                    Snackbar.make(save!!, "追加しました", Snackbar.LENGTH_SHORT)
                        .setAction("戻る") { finish() }
                        .setActionTextColor(Color.YELLOW)
                        .show()
                }
                else->{
                    realm.executeTransaction{db: Realm ->
                        val score = db.where<Score>()
                            .equalTo("id",scoreId).findFirst()
                        val date = dateEdit.text.toString()
                            .toDate("yyyy/MM/dd")
                        if(date != null) score?.date = date
                        score?.title = titleEdit.text.toString()
                        score?.singer = singerEdit.text.toString()
                        score?.Score = scoreEdit.text.toString()
                    }
                    Snackbar.make(save!!,"修正しました", Snackbar.LENGTH_SHORT)
                        .setAction("戻る"){finish()}
                        .setActionTextColor(Color.YELLOW)
                        .show()
                }

            }

            val post:AsyncHttp
            val date= dateEdit.text.toString()
            val title: String= titleEdit.text.toString()
            val singer: String = singerEdit.text.toString()
            val score :String = scoreEdit.text.toString()

            post = AsyncHttp(date,title,singer,score)
            post.execute()

            Snackbar.make(save!!,"追加しましたしました",Snackbar.LENGTH_SHORT)
                .setAction("戻る") {finish()}
                .setActionTextColor(Color.YELLOW)
                .show()
        }

        delete.setOnClickListener {
            realm.executeTransaction { db: Realm ->
                db.where<Score>().equalTo("id", scoreId)
                    ?.findFirst()
                    ?.deleteFromRealm()
            }
            Snackbar.make(delete, "削除しました", Snackbar.LENGTH_SHORT)
                .setAction("戻る") { finish() }
                .setActionTextColor(Color.YELLOW)
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    //文字列からDate型に変換するメソッド
    private fun String.toDate(parttern: String = "yyyy/MM/dd") : Date?{
        return try{
            SimpleDateFormat(parttern).parse(this)
        } catch (e:IllegalArgumentException){
            return null
        } catch (e:ParseException){
            return null
        }
    }
}
