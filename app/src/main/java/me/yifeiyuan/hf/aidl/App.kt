package me.yifeiyuan.hf.aidl

import android.app.Application

/**
 * Created by 程序亦非猿 on 2021/1/5.
 */
class App : Application() {

    companion object {
        lateinit var app: Application
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}