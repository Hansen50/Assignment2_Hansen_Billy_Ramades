package com.example.assignment2_hansenbillyramades.fragments

import android.app.ActivityOptions
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment2_hansenbillyramades.data.DiaryDatabase
import com.example.assignment2_hansenbillyramades.data.DiaryEntity
import com.example.assignment2_hansenbillyramades.ItemDiaryListener
import com.example.assignment2_hansenbillyramades.R
import com.example.assignment2_hansenbillyramades.activity.DiaryDetailActivity
import com.example.assignment2_hansenbillyramades.adapters.ItemDiaryAdapter
import com.example.assignment2_hansenbillyramades.data.GreetingTextData
import com.example.assignment2_hansenbillyramades.databinding.CustomBottomSheetBinding
import com.example.assignment2_hansenbillyramades.databinding.FormCreateDiaryBinding
import com.example.assignment2_hansenbillyramades.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), ItemDiaryListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: DiaryDatabase
    private lateinit var adapter: ItemDiaryAdapter
    private var selectedSortId: Int = R.id.rb_all
    // nilai default serta selectedSort ID untuk menyimpan ID dari metode pengurutan yang dipilih.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = DiaryDatabase.getDatabase(requireContext())

        lifecycleScope.launch {
            loadDiaries()
        }

        binding.fabAdd.setOnClickListener {
            showDiaryDialog()
        }

        binding.ivSortBy.setOnClickListener {
            showSortBy()
        }

        val greetingText = GreetingTextData(
            messageTemplate = "Halo, %s!",
            name = "June",
            highlightColor = ContextCompat.getColor(requireContext(), R.color.blue)
        )

        val formattedText = String.format(greetingText.messageTemplate, greetingText.name)
        val spannableTextGreetings = SpannableString(formattedText)

        val startIndex = formattedText.indexOf(greetingText.name)
        val endIndex = startIndex + greetingText.name.length

        spannableTextGreetings.setSpan(
            ForegroundColorSpan(greetingText.highlightColor),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableTextGreetings.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvGreetingsUser.text = spannableTextGreetings

    }

    private suspend fun loadDiaries() {
        adapter = ItemDiaryAdapter(emptyList(), this, true)
        binding.rvDiary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDiary.adapter = adapter
        val diaries = db.diaryDao().getDiary()
        if (diaries.isNotEmpty()) {
            binding.rvDiary.isVisible = true
            binding.tvNoDiary.isVisible = false
        } else {
            binding.rvDiary.isVisible = false
            binding.tvNoDiary.isVisible = true
        }
        adapter.updateData(diaries)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(diary: DiaryEntity) {
        val intent = Intent(requireContext(), DiaryDetailActivity::class.java).apply {
            putExtra("diary", diary)
        }
        startActivity(
            intent, ActivityOptions.makeCustomAnimation(
                requireContext(),
                R.anim.slide_right_enter, R.anim.slide_right_exit
            ).toBundle()
        )
    }


    override fun onDelete(diary: DiaryEntity) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete Diary")
        alertDialog.setMessage("Are you sure want to delete '${diary.title}'?")

        alertDialog.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
            lifecycleScope.launch {
                db.diaryDao().deleteDiary(diary)
                loadDiaries()
            }
            Toast.makeText(context, "Data '${diary.title}' has been deleted.", Toast.LENGTH_SHORT)
                .show()
            dialog.dismiss()
        }

        alertDialog.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onEdit(diary: DiaryEntity) {
        showDiaryDialog(diary)
    }


    // fun untuk membinding radio button sorting
    private fun showSortBy() {
        val bottomSheetBinding = CustomBottomSheetBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        // Mengatur radio button yang sesuai dengan selectedSortId agar terpilih saat dialog muncul.
        bottomSheetBinding.rgRadioGroupSort.check(selectedSortId)


        // Menambahkan listener pada RadioGroup yang menangani perubahan pilihan jika ada,
        // lalu menyimpan nya ke variabel selectedSortId
        bottomSheetBinding.rgRadioGroupSort.setOnCheckedChangeListener { group, checkedRadioButton ->
            selectedSortId = checkedRadioButton
        }

        bottomSheetBinding.btnApply.setOnClickListener {
            bottomDialogSorting(selectedSortId)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    // fun unttuk menampilkan dan memperbarui adapter recyclerview jika ingin melakukan sorting
    private fun bottomDialogSorting(selectedSortId: Int) {
        lifecycleScope.launch {
            val diaries = when (selectedSortId) {
                R.id.rb_latest -> {
                    binding.tvAll.text = "Lastest"
                    db.diaryDao().getDiaryLatest()
                }

                R.id.rb_oldest -> {
                    binding.tvAll.text = "Oldest"
                    db.diaryDao().getDiaryOldest()
                }

                R.id.rb_all -> {
                    binding.tvAll.text = "All"
                    db.diaryDao().getDiary()
                }

                else -> throw IllegalStateException("Invalid Radio Position $selectedSortId")
                //db.diaryDao().getDiary()
            }
            adapter.updateData(diaries)
        }
    }


    // fun untuk menampilkan dialog create dan edit lalu menyimpan dalam ke database
    private fun showDiaryDialog(diary: DiaryEntity? = null) {
        val dialogBinding = FormCreateDiaryBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext()).create()

        dialog.setView(dialogBinding.root)
        dialog.setCancelable(false)
        dialog.show()

        dialog.window?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )

        if (diary != null) {
            dialogBinding.etTitleDiary.setText(diary.title)
            dialogBinding.etDiaryDescription.setText(diary.description)
            dialogBinding.tvCreateDiary.text = "Edit Diary"
            dialogBinding.btnSave.text = "Save"
        }

        dialogBinding.ibClose.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSave.setOnClickListener {
            val title = dialogBinding.etTitleDiary.text.toString().trim()
            val description = dialogBinding.etDiaryDescription.text.toString().trim()

            // jika field nya kosong maka akan tampil toast message
            if (title.isNotEmpty() && description.isNotEmpty()) {
                lifecycleScope.launch {
                    // logic untuk menghadle create dan edit
                    if (diary == null) {
                        db.diaryDao().insertDiary(
                            DiaryEntity(
                                0,
                                title,
                                description,
                                date = System.currentTimeMillis()
                            )
                        )
                        Toast.makeText(context, "Diary added", Toast.LENGTH_SHORT).show()
                    } else {
                        db.diaryDao().updateDiary(
                            DiaryEntity(
                                diary.id,
                                title,
                                description,
                                date = System.currentTimeMillis()
                            )
                        )
                        Toast.makeText(context, "Diary updated", Toast.LENGTH_SHORT).show()
                    }
                    // apabila telah create dan edit maka akan memanggil fun load diares untuk meyinpam ke database dan mengupdate adapter recyclerview
                    loadDiaries()
                    dialog.dismiss()
                }
            } else {
                Toast.makeText(context, "You have to fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
