package dev.yash.keymanager.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import app.yash.keymanager.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class NewKeyDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        alertDialogBuilder.setTitle(R.string.create_ssh)
        alertDialogBuilder.setView(R.layout.create_dialog)
        alertDialogBuilder.setPositiveButton(getString(R.string.create_button), null)
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel)) { _, _ -> dismiss() }
        val dialog = alertDialogBuilder.create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val editTextView = dialog.findViewById<TextInputEditText>(R.id.ssh_key_value)
                if (!editTextView?.text.isNullOrEmpty()) {
                    setFragmentResult(
                        "new_ssh_key",
                        bundleOf("ssh_key" to editTextView?.text.toString())
                    )
                    dialog.cancel()
                    Toast.makeText(requireContext(), "Adding key...Please wait", Toast.LENGTH_LONG)
                        .show()
                } else {
                    editTextView?.error = "This field is required"
                }
            }
        }
        return dialog
    }

    companion object {
        fun newInstance(): NewKeyDialogFragment = NewKeyDialogFragment()
    }
}
