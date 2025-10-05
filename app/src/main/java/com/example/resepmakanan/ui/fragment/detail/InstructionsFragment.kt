package com.example.resepmakanan.ui.fragment.detail

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.resepmakanan.databinding.FragmentInstructionsBinding

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    private var instructions: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ambil argumen dari bundle, bisa null
        instructions = requireArguments().getString("instructions")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Jika instruksi kosong, tampilkan default message
        val htmlInstructions = instructions ?: "No instructions available"

        // Parsing HTML supaya tag tag di-instruksi tampil benar
        binding.instructionsTextView.text = HtmlCompat.fromHtml(htmlInstructions, HtmlCompat.FROM_HTML_MODE_LEGACY)

        // Supaya link di TextView bisa diklik (jika ada)
        binding.instructionsTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(instructions: String?): InstructionsFragment {
            return InstructionsFragment().apply {
                arguments = bundleOf("instructions" to instructions)
            }
        }
    }
}
