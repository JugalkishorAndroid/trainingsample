package com.jugal.trainingsample.app.ui.main.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.transition.TransitionManager
import com.jugal.trainingsample.R
import com.jugal.trainingsample.app.MainActivity
import com.jugal.trainingsample.app.viewmodel.PeopleViewModel
import com.jugal.trainingsample.data.event.NetworkEvent
import com.jugal.trainingsample.service.CountDownService
import kotlinx.android.synthetic.main.fragment_people_details.*
import kotlinx.android.synthetic.main.fragment_people_details.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt


class PeopleDetailsFragment : DialogFragment() {

    private val viewModel by viewModel<PeopleViewModel>()

    private lateinit var peopleUrl: String

    private val dialogWidth by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            320f,
            this.resources.displayMetrics
        ).roundToInt()
    }

    private val dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            peopleUrl = arguments?.getString(ARG_PEOPLE_URL)
                ?: throw IllegalStateException("No url passed")
        } catch (e: IllegalStateException) {
            Log.e(TAG, "onAttach: ", e)
            Toast.makeText(context, R.string.error_generic, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (dialog.window != null)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.window?.attributes?.windowAnimations = R.style.CardDialogAnimation
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_people_details, container, true)
        dialog?.requestWindowFeature(STYLE_NO_TITLE)
        isCancelable = false

        if (activity is MainActivity) {
            (activity as MainActivity).apply {
                startService(Intent(this, CountDownService::class.java))
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        initWindowSize()
        subscribeToPeople()
        subscribeToNetworkEvent()

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            onBroadCastReceiver,
            IntentFilter("broadCast")
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(onBroadCastReceiver)
        super.onPause()
    }

    private val onBroadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.extras != null) {
                val countDownTimer = intent.extras?.getString("remaining_time")

                people_details_timer.post {
                    people_details_timer.text = getString(
                        R.string.people_details_timer_template,
                        countDownTimer
                    )
                }
//                Log.e("countDownTimer","___"+countDownTimer)


                if (countDownTimer == "0") {
                    if (activity is MainActivity) {
                        (activity as MainActivity).apply {
                            dismiss()
                            stopService(Intent(this, CountDownService::class.java))
                        }
                    }
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (activity is MainActivity) {
            (activity as MainActivity).apply {
                stopService(Intent(this, CountDownService::class.java))
                dismiss()
            }
        }
    }

    private fun initWindowSize() {
        val params = dialog?.window?.attributes
        params?.width = dialogWidth
        params?.height = dialogHeight
        dialog?.window?.attributes = params
    }

    private fun subscribeToPeople() {
        viewModel.getPeopleDetails(peopleUrl).observe(this) {
            people_details_root_layout.apply {
                this.people_details_name.text = it.people.getFullName()
                this.people_details_user_id.text = getString(
                    R.string.people_details_user_id_template,
                    it.people.id.toString()
                )
                this.people_details_email.text = getString(
                    R.string.people_details_email_template,
                    it.people.email
                )
                this.people_details_company.text = getString(
                    R.string.people_details_company_detail_template,
                    it.companyDetail.company
                )
            }
        }
    }

    private fun subscribeToNetworkEvent() {
        viewModel.networkEvents.observe(viewLifecycleOwner) {
            when (it) {
                NetworkEvent.Loading -> showLoading(true)
                NetworkEvent.Success -> showLoading(false)
                is NetworkEvent.Failure -> {
                    showLoading(false)
                    it.getContentIfNotHandled()?.run {
                        showMessage(it.res)
                    }
                }
            }
        }
    }

    private fun showMessage(message: Int) {
        context?.run {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        TransitionManager.beginDelayedTransition(people_details_root_layout)
        people_details_progress_bar.visibility = if (isLoading) View.VISIBLE else View.GONE
        TransitionManager.endTransitions(people_details_root_layout)
    }

    companion object {
        private const val TAG: String = "PeopleDetailsFragment"
        const val ARG_PEOPLE_URL = "arg_people_url"
    }

}
