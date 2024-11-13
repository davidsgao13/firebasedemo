package com.example.firebasesignin

import android.app.Application
import com.google.firebase.FirebaseApp

class FirebaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}