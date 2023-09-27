package com.example.mygallerypractice

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.mygallerypractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadImageButton.setOnClickListener{
            checkPermission()
        }

    }

    private fun checkPermission(){
        when {
            ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadImage()
            }
            shouldShowRequestPermissionRationale(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showPermissionInfoDialog()
            }
            else -> {
                requestReadExternalStorage()
            }
        }




    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this).apply{
            setMessage("이미지를 가져오기 위해서 외부 저장소 읽기 권한이 필요합니다.")
            setNegativeButton("취소", null)
            setPositiveButton("동의"){_,_ ->
                requestReadExternalStorage()
            }
        }.show()
    }

    private fun requestReadExternalStorage(){
        ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_READ_EXTERNAL_STORAGE) //requestCode 는 100번으로 요청한 코드에 대해서 확인을 하겠다

    }


    private fun loadImage(){
        Toast.makeText(this,"이미지가져올예정",Toast.LENGTH_SHORT).show()

    }

    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 100
    }
}