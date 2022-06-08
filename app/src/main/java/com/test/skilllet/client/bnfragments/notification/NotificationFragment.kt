package com.test.skilllet.client.bnfragments.notification

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.skilllet.R
import com.test.skilllet.databinding.NotificationFragmentBinding

class NotificationFragment() : Fragment() {
    lateinit var binding: NotificationFragmentBinding
    var list = ArrayList<String>()
    var listIcons = ArrayList<Drawable>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotificationFragmentBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        populateAdapter()
    }

    private fun populateAdapter() {
        list.add("Mr.Hamza will arrive at venue at 9am on 13/3/20222")
        list.add("MR. Akram has accepted you request for cleaning service.")
        list.add("You have requested a cleaning service")

        activity?.resources?.getDrawable(R.drawable.service_provider)?.let { listIcons.add(it) }

        activity?.resources?.getDrawable(R.drawable.service_provider)?.let {
            listIcons.add(it)
        }

        activity?.resources?.getDrawable(R.drawable.profile_charachter)?.let {
            listIcons.add(it)
        }

        var adapter=TempNotificationAdapter(list,listIcons)
        binding.rv.adapter = adapter
    }
}
