package com.example.indoornavigationar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))  //Создаем сплеш скрин и запускаем сразу же активити
        finish() //Завершаем показ сплеш скрина после загрузки мейнАктивити
    }
}