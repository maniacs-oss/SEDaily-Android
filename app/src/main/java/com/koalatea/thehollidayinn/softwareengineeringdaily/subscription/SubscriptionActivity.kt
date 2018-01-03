package com.koalatea.thehollidayinn.softwareengineeringdaily.subscription

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.koalatea.thehollidayinn.softwareengineeringdaily.MainActivity
import com.koalatea.thehollidayinn.softwareengineeringdaily.R

import kotlinx.android.synthetic.main.activity_subscription.*

class SubscriptionActivity : AppCompatActivity(),
        PaymentFragment.OnFragmentInteractionListener,
        PlanFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)
        setSupportActionBar(toolbar)

        showPlanOptions()
    }

    private fun showPlanOptions () {
        // @TODO: save instance?
        val planFragment = PlanFragment.newInstance()
        this.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, planFragment)
                .commit()
    }

    private fun showPayment (type: String) {
        // @TODO: save instance?
        val fragment = PaymentFragment.newInstance(type)
        this.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    override fun paymentSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onPlanSelected(type: String) {
        showPayment(type);
    }
}
