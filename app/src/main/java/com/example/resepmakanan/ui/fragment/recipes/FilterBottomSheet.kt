package com.example.resepmakanan.ui.fragment.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.resepmakanan.R
import com.google.android.material.button.MaterialButton

class FilterBottomSheetFragment(
    private val onFilterSelected: (String?) -> Unit // callback ke RecipesFragment (opsional)
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate layout yang tadi dibuat
        val view = inflater.inflate(R.layout.bottom_sheet_filter, container, false)

        val radioGroup = view.findViewById<RadioGroup>(R.id.filter_radio_group)
        val btnApply = view.findViewById<MaterialButton>(R.id.btn_apply)

        btnApply.setOnClickListener {
            // dapatkan id yang terpilih (atau -1 kalau ga ada)
            val selectedId = radioGroup.checkedRadioButtonId
            val selectedText: String? = if (selectedId != -1) {
                val selectedRadio = view.findViewById<RadioButton>(selectedId)
                selectedRadio?.text?.toString()
            } else null

            // mapping teks RadioButton ke value query yang kamu pakai di loadRecipes()
            val filterValue = when (selectedText) {
                "Semua" -> null
                "Vegetarian" -> "vegetarian"
                "Vegan" -> "vegan"
                "Minuman" -> "drink"
                "Makanan Utama" -> "main course"
                else -> null
            }

            onFilterSelected(filterValue)
            dismiss()
        }

        return view
    }
}
