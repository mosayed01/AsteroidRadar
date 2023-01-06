package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.util.recycle.ClickListener
import com.udacity.asteroidradar.util.recycle.RecycleAdapter
import com.udacity.asteroidradar.util.FilterEvent

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentMainBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = RecycleAdapter(ClickListener {
            val action = MainFragmentDirections.actionShowDetail(it)
            findNavController().navigate(action)
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_all_menu -> {
                viewModel.setList(FilterEvent.SavedEvent)
                Toast.makeText(context, "All", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.show_buy_menu -> {
                viewModel.setList(FilterEvent.WeekEvent)
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.show_rent_menu -> {
                viewModel.setList(FilterEvent.TodayEvent)
                Toast.makeText(context, "Today", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return false
    }
}
