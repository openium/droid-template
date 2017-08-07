package fr.openium.template

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by t.coulange on 07/04/2017.
 */
class ActivityTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

}