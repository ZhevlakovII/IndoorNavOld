package com.example.indoornavigationar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.indoornavigationar.MainActivity
import com.example.indoornavigationar.databinding.MapFragmentBinding
import com.example.indoornavigationar.viewmodels.MapViewModel
import com.example.indoornavigationar.viewmodels.SharedViewModel
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.AndroidEntryPoint
import ovh.plrapps.mapcompose.ui.MapUI

@AndroidEntryPoint
class MapFragment : Fragment() {
    private val mapViewModel: MapViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = MapFragmentBinding.inflate(inflater, container, false)

        binding.searchButton.setOnClickListener { navigateToSearch() }

        val mapContainer = binding.composeView
        mapContainer.setContent { 
            AppCompatTheme {
                MapContainer(viewModel = mapViewModel)
            }
        }

        mapViewModel.locations.observe(viewLifecycleOwner) { locationsList ->
            if (locationsList.isNotEmpty())
                mapViewModel.setLocationObservable()
        }

        mapViewModel.cardActiveState.observe(viewLifecycleOwner) { item ->
            if (item) {
                binding.navigationCard.visibility = View.VISIBLE
                (activity as MainActivity).hideActivity(item)

                binding.searchedItem = mapViewModel.getLocationName()

                sharedViewModel.setMapPoint(mapViewModel.getCoordinates())
                sharedViewModel.setLocationId(mapViewModel.getLocId())
            }
            else {
                binding.navigationCard.visibility = View.GONE
                (activity as MainActivity).hideActivity(item)
            }
        }

        binding.pickPointButton.setOnClickListener {
            navigateToPointPicker()
        }

        return binding.root
    }

    private fun navigateToSearch() {
        val direction =
            MapFragmentDirections.actionNavigationMapToSearchNavigation(true)
        view?.findNavController()?.navigate(direction)
    }

    private fun navigateToPointPicker() {
        val direction =
            MapFragmentDirections.actionNavigationMapToPointsEnter(true)
        view?.findNavController()?.navigate(direction)
    }
}

@Composable
private fun MapContainer(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel
) {
    MapUI(modifier, state = viewModel.state)
}
