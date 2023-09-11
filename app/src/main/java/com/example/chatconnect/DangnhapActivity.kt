package com.example.chatconnect

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

//lop con
class DangnhapActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dangnhap)

        val dangnhap = findViewById<TextView>(R.id.button_dangnhap).setOnClickListener {
            val email = findViewById<TextView>(R.id.dangnhaptk_email_text).text.toString()
            val matkhau = findViewById<TextView>(R.id.dangnhaptk_matkhau_text).text.toString()

            Log.d("Login", "Dang nhap bang email/pw: $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, matkhau)
        }
        val trove = findViewById<TextView>(R.id.trove_dangky).setOnClickListener {
            finish() //hoan thanh va tro ve trang dang ky
        }

    }
}