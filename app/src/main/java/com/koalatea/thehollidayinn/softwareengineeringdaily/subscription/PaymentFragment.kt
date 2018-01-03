package com.koalatea.thehollidayinn.softwareengineeringdaily.subscription

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import com.koalatea.thehollidayinn.softwareengineeringdaily.R
import com.koalatea.thehollidayinn.softwareengineeringdaily.app.SDEApp
import com.koalatea.thehollidayinn.softwareengineeringdaily.data.remote.APIInterface
import com.koalatea.thehollidayinn.softwareengineeringdaily.data.remote.ApiUtils
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Token
import com.stripe.android.view.CardInputWidget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class PaymentFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null
    private var type: String = ""
    private lateinit var mCardInputWidget: CardInputWidget

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_payment, container, false)

        val button: Button = rootView.findViewById(R.id.paymentButton)
        button.setOnClickListener({ pay() })

        mCardInputWidget = rootView.findViewById(R.id.card_input_widget)

        return rootView
    }

    private fun pay() {
        val cardToSave = mCardInputWidget.card
        if (cardToSave == null) {
            Toast.makeText(SDEApp.component.context(),
                "Invalid Card Data",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val stripe = Stripe(SDEApp.component.context(), "pk_live_Cfttsv5i5ZG5IBfrmllzNoSA")
        stripe.createToken(
            cardToSave,
            object: TokenCallback {
                override fun onSuccess(token: Token) {
                    createSubscription(token)
                }

                override fun onError(error: Exception) {
                    Toast.makeText(SDEApp.component.context(),
                            error.toString(),
                            Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    private fun createSubscription(token: Token) {
        val service: APIInterface = ApiUtils.getKibbleService(activity)
        service.createSubscription(token.id, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableObserver<Void>() {
                override fun onComplete() {
                    Toast.makeText(SDEApp.component.context(),
                            "Success!",
                            Toast.LENGTH_LONG
                    ).show()
                    mListener?.paymentSuccess();
                }

                override fun onError(e: Throwable ) {
//                        Log.v(TAG, e.toString());
                }

                override fun onNext(t: Void) {

                }
            })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun paymentSuccess()
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        fun newInstance(type: String): PaymentFragment {
            val fragment = PaymentFragment()
            fragment.type = type
            return fragment
        }
    }
}// Required empty public constructor
