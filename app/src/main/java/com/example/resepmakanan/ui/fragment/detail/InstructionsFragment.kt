package com.example.resepmakanan.ui.fragment.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.resepmakanan.R

class InstructionsFragment : Fragment() {

    companion object {
        private const val ARG_SOURCE_URL = "sourceUrl"

        fun newInstance(sourceUrl: String): InstructionsFragment {
            val fragment = InstructionsFragment()
            val args = Bundle()
            args.putString(ARG_SOURCE_URL, sourceUrl)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 🔹 Gunakan layout yang sudah kamu buat di atas
        return inflater.inflate(R.layout.fragment_instructions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sourceUrl = arguments?.getString(ARG_SOURCE_URL)
        val linkTextView = view.findViewById<TextView>(R.id.linkTextView)
        val instructionsTextView = view.findViewById<TextView>(R.id.instructionsTextView)

        // Kalau sourceUrl tidak kosong, tampilkan link
        if (!sourceUrl.isNullOrEmpty()) {
            linkTextView.visibility = View.VISIBLE
            linkTextView.text = "Lihat Sumber Resep"
            linkTextView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sourceUrl))
                startActivity(intent)
            }
        } else {
            linkTextView.visibility = View.GONE
            instructionsTextView.text = "Tidak ada link sumber resep."
        }
    }
}
