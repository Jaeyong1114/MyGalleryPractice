package com.example.mygallerypractice

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.example.mygallerypractice.databinding.ItemImageBinding
import com.example.mygallerypractice.databinding.ItemLoadMoreBinding

class ImageAdpater(private val itemClickListener: ItemClickListener) : ListAdapter<ImageItems, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<ImageItems>() { // 데이터가 변경됐다는걸 체크하기위해 필수 구현
        override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem === newItem  // 같은값 참조하는지
        }

        override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem == newItem  // equal
        }

    }
){
//맨마지막에 아이템으로 LoadMore을 넣어줄거임 데이터를 가져왔을때 데이터가 하나라도있으면 그뒤에 하나 더붙음

    override fun getItemCount(): Int {
        val originSize = currentList.size
        return if(originSize == 0 ) 0 else originSize.inc()
    }

    override fun getItemViewType(position: Int): Int {
        return if(itemCount.dec() == position) ITEM_LOAD_MORE else ITEM_IMAGE
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater



        return when(viewType){
            ITEM_IMAGE -> {
                val binding = ItemImageBinding.inflate(inflater,parent,false)
                ImageViewHolder(binding)

            }
            else -> {
                val binding = ItemLoadMoreBinding.inflate(inflater,parent,false)
                LoadMoreViewHolder(binding)


            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ImageViewHolder -> {
                holder.bind(currentList[position] as ImageItems.Image)
            }
            is LoadMoreViewHolder -> {
                holder.bind(itemClickListener)

            }
        }

    }

    interface ItemClickListener {
       fun onLoadMoreClick()
    }

    companion object {
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORE = 1
    }
}

sealed class ImageItems { // ImageItmes 를 선언했을때 자식으로 Image 와 LoadMore 이있는걸 인지
    data class Image(
        var uri: Uri,
    ) : ImageItems()

    object LoadMore : ImageItems()
}

class ImageViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item: ImageItems.Image){
        binding.previewImageView.setImageURI(item.uri)
    }

}

class LoadMoreViewHolder(binding: ItemLoadMoreBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(itemClickListener: ImageAdpater.ItemClickListener){

        itemView.setOnClickListener{
            itemClickListener.onLoadMoreClick()
        }

    }
}
