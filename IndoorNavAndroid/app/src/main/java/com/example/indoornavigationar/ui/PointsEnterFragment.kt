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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.indoornavigationar.databinding.PointsEnterFragmentBinding
import com.example.indoornavigationar.viewmodels.PointsEnterViewModel
import com.example.indoornavigationar.viewmodels.SharedViewModel
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.AndroidEntryPoint
import ovh.plrapps.mapcompose.ui.MapUI

@AndroidEntryPoint
class PointsEnterFragment : Fragment() {
    private val pointsEnterViewModel: PointsEnterViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args: PointsEnterFragmentArgs by navArgs()

    private var isWhichButtonPressed: Int? = null
    private var isOpenFromMap: Boolean = true

    private var searchFirstButtonText: String? = null
    private var searchSecondButtonText: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = PointsEnterFragmentBinding.inflate(inflater, container, false)

        val navController = findNavController()

        val mapContainer = binding.composeView
        mapContainer.setContent {
            AppCompatTheme {
                MapContainer(viewModel = pointsEnterViewModel)
            }
        }

        if (isOpenFromMap) {
            if (args.pointPositionInDatabase != -1) {
                pointsEnterViewModel.getItemFromLocations(args.pointPositionInDatabase)

                pointsEnterViewModel.searchedItem.observe(viewLifecycleOwner) { item ->
                    binding.secondSearchButton.text = item.name
                    searchSecondButtonText = item.name

                    item.name?.let {
                        pointsEnterViewModel
                            .addSearchedMapPointToList(item.centerX, item.centerY, it, 2)
                    }
                }
            } else {
                sharedViewModel.getMapPoint()?.let { pointsEnterViewModel.addMapPointToList(it) }
                sharedViewModel.getLocationId()?.let { pointsEnterViewModel.getItemFromLocations(it) }

                pointsEnterViewModel.searchedItem.observe(viewLifecycleOwner) { item ->
                    binding.secondSearchButton.text = item.name
                    searchSecondButtonText = item.name
                }
            }
        } else {
            navController
                .currentBackStackEntry
                ?.savedStateHandle
                ?.getLiveData<Int>("locId")
                ?.observe(viewLifecycleOwner) { result ->
                    pointsEnterViewModel.getItemFromLocations(result)
                    pointsEnterViewModel.searchedItem.observe(viewLifecycleOwner) { item ->
                        if (isWhichButtonPressed == 1) {
                            item.name?.let {
                                pointsEnterViewModel
                                    .addSearchedMapPointToList(item.centerX, item.centerY, it, 1)
                            }

                            binding.firstSearchButton.text = item.name
                            searchFirstButtonText = item.name

                            if (searchSecondButtonText != null)
                                binding.secondSearchButton.text = searchSecondButtonText

                        } else if (isWhichButtonPressed == 2) {
                            item.name?.let {
                                pointsEnterViewModel
                                    .addSearchedMapPointToList(item.centerX, item.centerY, it, 2)
                            }

                            binding.secondSearchButton.text = item.name
                            searchSecondButtonText = item.name

                            if (searchFirstButtonText != null)
                                binding.firstSearchButton.text = searchFirstButtonText
                        }
                    }
                }
        }

        binding.backButton.setOnClickListener {
            val action =
                PointsEnterFragmentDirections.actionPointsEnterToNavigationMap()
            view?.findNavController()?.navigate(action)
        }

        binding.secondSearchButton.setOnClickListener {
            val action =
                PointsEnterFragmentDirections.actionPointsEnterToSearchNavigation(false)
            view?.findNavController()?.navigate(action)

            isWhichButtonPressed = 2
            isOpenFromMap = false
        }

        binding.firstSearchButton.setOnClickListener {
            val action =
                PointsEnterFragmentDirections.actionPointsEnterToSearchNavigation(false)
            view?.findNavController()?.navigate(action)

            isWhichButtonPressed = 1
            isOpenFromMap = false
        }

        pointsEnterViewModel.dotsCount.observe(viewLifecycleOwner) { item ->
            if (item == 2) {
                binding.startRouteCard.visibility = View.VISIBLE
            }
        }

        binding.startButton.setOnClickListener {
            binding.secondSearchButton.visibility = View.GONE
            binding.firstSearchButton.visibility = View.GONE
            binding.startRouteCard.visibility = View.GONE
            binding.imageUser.visibility = View.GONE
            binding.imageMapPoint.visibility = View.GONE

            pointsEnterViewModel.createPath()
        }

        return binding.root
    }
}

@Composable
private fun MapContainer(
    modifier: Modifier = Modifier,
    viewModel: PointsEnterViewModel
) {
    MapUI(modifier, state = viewModel.state)
}