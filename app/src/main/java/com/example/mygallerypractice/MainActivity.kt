package com.example.mygallerypractice

import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.mygallerypractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val imageLoadLuncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){
        uriList -> updateImages(uriList)

    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageAdapter :ImageAdpater
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadImageButton.setOnClickListener{
            checkPermission()
        }
        initRecyclerView()

    }

    private fun initRecyclerView(){
        imageAdapter = ImageAdpater(object: ImageAdpater.ItemClickListener {
            override fun onLoadMoreClick() {
                checkPermission()
            }

        })
        
        binding.imageRecyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(context,2)
        }
        
    }


    private fun checkPermission(){
        when {
            ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadImage()
            }
            shouldShowRequestPermissionRationale( //사용자가 권한을 명시적으로 거부한 경우
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showPermissionInfoDialog()
                Log.d("permission","거부 했을때")
            }
            else -> {
                requestReadExternalStorage()
                Log.d("permission","아무것도 아닐때")
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
        imageLoadLuncher.launch("image/*") //이미지타입으로 된 모든 파일 가져옴


    }

    private fun updateImages(uriList: List<Uri>){
        Log.i("updateImages","$uriList")
        val images = uriList.map {ImageItems.Image(it)}
        val updatedImages = imageAdapter.currentList.toMutableList().apply { addAll(images)}
        imageAdapter.submitList(updatedImages) //데이터변경 , 쓰레드관련 처리

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            REQUEST_READ_EXTERNAL_STORAGE -> {
                val resultCode = grantResults.firstOrNull() ?: PackageManager.PERMISSION_DENIED
                if(resultCode == PackageManager.PERMISSION_GRANTED){
                    loadImage()
                }

            }
        }
    }


    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 100
    }
}