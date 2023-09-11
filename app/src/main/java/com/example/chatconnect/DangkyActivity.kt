package com.example.chatconnect

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class DangkyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dangky = findViewById<Button>(R.id.button_dangky)
        dangky.setOnClickListener {
            quanly()
        }
        val taikhoan = findViewById<TextView>(R.id.dangnhap_cotaikhoan).setOnClickListener {
            Log.d("Activity", "Hien thi cac hoat dong")

            //chuyen toi class dang nhap
            val intent = Intent(this,DangnhapActivity::class.java)
            startActivity(intent)
        }
        //chon hinh dai dien
        val avt = findViewById<Button>(R.id.chon_avt_dangky).setOnClickListener{
            Log.d("avtActivity", "Hien thi hinh anh")

            //chon anh dai dien
            val intent = Intent(Intent.ACTION_PICK) //y dinh
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }
    var selectedPhotoUri: Uri? = null
    //tim anh da chon tu thiet bi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data == null){
            //kiem tra hinh anh da chon la gi
            Log.d("avtActivity", "Hinh anh da duoc chon")

            selectedPhotoUri = data?.data //hien thi vi tri noi hinh anh duoc luu
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            val chonAvt = findViewById<Button>(R.id.chon_avt_dangky).setBackgroundDrawable(bitmapDrawable) //hien thi avt len
        }
    }
    private fun quanly (){
        //xu ly xu kien tren view, cap quyen truy truy cap
        val email = findViewById<EditText>(R.id.dangnhap_email_text).text.toString()
        val matkhau = findViewById<EditText>(R.id.dangnhap_matkhau_text).text.toString()

        //kiem tra neu chuoi rong thi quay lai
        if (email.isEmpty() || matkhau.isEmpty()){ //hien thi dong thong bao yeu cau nhap mail va mk
            Toast.makeText(this, "Vui lòng nhập Email và mật khẩu", Toast.LENGTH_LONG).show()
            return
        }

        //xem cac gia tri ben trong Logcat o duoi
        Log.d("Activity", "Email is: " +email)
        Log.d("Activity", "Mat khau: $matkhau")

        //xac thuc Firebase de tao nguoi dung bang email va mat khau
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, matkhau)
            .addOnCompleteListener {    //them ham hoan thanh
                if(!it.isSuccessful) return@addOnCompleteListener //neu ko thanh cong thi quay lai
                //else if thanh cong
                Log.d("dangkyActivity", "Tao thanh cong voi ID: ${it.result.user?.uid}")
                UploadImageToFirebaseStorage()
            }
            //xu ly ngoai le khi nhap email
            .addOnFailureListener{
                Log.d("dangkyActivity", "Tao tai khoan that bai: ${it.message}")
                Toast.makeText(this, "Tạo tài khoản thất bại", Toast.LENGTH_LONG).show()
            }
    }
    //tai hinh avt len firebase storage
    private fun UploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("dangkyActivity", "Hinh anh da tai len thanh cong ${it.metadata?.path}")
            }
    }
}
