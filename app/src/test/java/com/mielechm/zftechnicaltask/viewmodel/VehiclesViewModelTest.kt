package com.mielechm.zftechnicaltask.viewmodel

import app.cash.turbine.test
import com.mielechm.zftechnicaltask.data.model.VehicleDetailsItem
import com.mielechm.zftechnicaltask.data.model.VehicleListItem
import com.mielechm.zftechnicaltask.repositories.VehiclesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class VehiclesViewModelTest {

    lateinit var viewModel: VehiclesViewModel

    @Mock
    lateinit var repository: VehiclesRepository

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = VehiclesViewModel(repository)
        viewModel.stopUpdatesForList()
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `running getVehicles should get list of vehicles`() = runTest {
        viewModel.getVehicles()
        viewModel.vehicles.test {
            val emission = awaitItem()
            assertEquals(listOf<VehicleListItem>(), emission)
        }
    }

    @Test
    fun `running getVehicles should change state of isLoading`() = runTest {
        viewModel.getVehicles()
        viewModel.isLoading.test {
            val emission = awaitItem()
            assertEquals(true, emission)
        }
    }

    @Test
    fun `running getVehicles successfully should get empty message for loadError`() = runTest {
        viewModel.getVehicles()
        viewModel.loadError.test {
            val emission = awaitItem()
            assertEquals("", emission)
        }
    }

    @Test
    fun `running getVehicleDetails should get vehicle details`() = runTest {
        viewModel.getVehicleDetails("x")
        viewModel.vehicleDetails.test {
            val emission = awaitItem()
            assertEquals(VehicleDetailsItem(), emission)
        }
        viewModel.stopUpdatesForDetails()
    }

    @Test
    fun `running getVehicleDetails should change state of isLoading`() = runTest {
        viewModel.getVehicleDetails("x")
        viewModel.isLoading.test {
            val emission = awaitItem()
            assertEquals(true, emission)
        }
        viewModel.stopUpdatesForDetails()
    }

    @Test
    fun `running getVehicleDetails successfully should get empty message for loadError`() =
        runTest {
            viewModel.getVehicleDetails("")
            viewModel.loadError.test {
                val emission = awaitItem()
                assertEquals("", emission)
            }
            viewModel.stopUpdatesForDetails()
        }

}