package com.example.assignment2_hansenbillyramades.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2_hansenbillyramades.data.DiaryEntity
import com.example.assignment2_hansenbillyramades.ItemDiaryListener
import com.example.assignment2_hansenbillyramades.databinding.ListItemDiaryBinding
import java.text.SimpleDateFormat
import java.util.*

class ItemDiaryAdapter(
    private var data: List<DiaryEntity>,
    private val listener: ItemDiaryListener,
    private val showIcons: Boolean,
    //  nilai showIcons adalah Boolean yang menentukan apakah ikon edit dan delete ditampilkan.

    ) : RecyclerView.Adapter<ItemDiaryAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ListItemDiaryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val binding =
            ListItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = data.size
    // Mengembalikan jumlah item dalam daftar diary.

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // position adalah indeks 0, yang akan mentrigger adapter agar sesuai tepat item data
        val item = data[position]

        holder.binding.ivEditCardView.isVisible = showIcons
        holder.binding.ivDeleteCardView.isVisible = showIcons

        holder.binding.tvTitle.text = item.title
        holder.binding.tvContent.text = item.description
        holder.binding.tvDate.text = formatDate(item.date)

        holder.binding.ivEditCardView.setOnClickListener {
            listener.onEdit(item)
        }

        holder.binding.root.setOnClickListener {
            listener.onClick(item)
        }

        holder.binding.ivDeleteCardView.setOnClickListener {
            listener.onDelete(item)
        }
    }

        // fun yntuk memgubah timestamp (waktu dalam milidetik) menjadi string tanggal
    private fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }


    // memperbarui data yang ditampilkan dan memberi tahu RecyclerView untuk memperbarui tampilan.
    fun updateData(newDiaries: List<DiaryEntity>) {
        data = newDiaries
        notifyDataSetChanged()
    }
}
