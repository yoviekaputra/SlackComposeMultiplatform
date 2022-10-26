package dev.baseio.slackclone.uionboarding.vmtest

import app.cash.turbine.test
import dev.baseio.slackclone.data.injection.viewModelDelegateModule
import dev.baseio.slackclone.uionboarding.vm.AuthCreateWorkspaceVM
import dev.baseio.slackdata.injection.dataMappersModule
import dev.baseio.slackdata.injection.fakeDataSourceModule
import dev.baseio.slackdata.injection.testDataModule
import dev.baseio.slackdata.injection.testDispatcherModule
import dev.baseio.slackdata.injection.testUseCaseModule
import dev.baseio.slackdomain.CoroutineDispatcherProvider
import dev.baseio.slackdomain.usecases.workspaces.UseCaseCreateWorkspace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.asserter
import kotlin.time.Duration.Companion.seconds

class AuthCreateWorkspaceVMTest : KoinTest {

    private val coroutineDispatcherProvider: CoroutineDispatcherProvider by inject()
    private val useCaseCreateWorkspace: UseCaseCreateWorkspace by inject()
    private var navigated = false
    private val navigateDashboard = {
        navigated = true
    }

    val viewModel by lazy {
        AuthCreateWorkspaceVM(coroutineDispatcherProvider, useCaseCreateWorkspace, navigateDashboard)
    }

    @BeforeTest
    fun setUp() {
        navigated = false
        startKoin {
            modules(
                testDataModule,
                testUseCaseModule,
                viewModelDelegateModule,
                dataMappersModule,
                fakeDataSourceModule,
                testDispatcherModule
            )
        }
        Dispatchers.setMain(coroutineDispatcherProvider.main)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        stopKoin()
    }

    @Test
    fun `viewModel informs the component to navigate after successful authentication`() {
        runTest {
            viewModel.state.apply {
                this.value = this.value.copy(email = "test@email.com", password = "password", "email")
            }
            viewModel.createWorkspace()
            viewModel.state.test(timeout = 5.seconds) {
                awaitItem().apply {
                    asserter.assertTrue(actual = loading, message = "Loading was not true!")
                }
                awaitItem().apply {
                    asserter.assertTrue(actual = !loading, message = "Loading was not false!")
                    asserter.assertTrue(actual = this.error == null, message = "error was not null!")

                }
                asserter.assertTrue({ "Was not navigated" }, navigated)
            }
        }
    }

    @Test
    fun `viewModel state fails with exception`() {
        runTest {
            viewModel.state.apply {
                this.value = this.value.copy(email = "", password = "", "email")
            }
            viewModel.createWorkspace()
            viewModel.state.test(timeout = 5.seconds) {
                awaitItem().apply {
                    asserter.assertTrue(actual = loading, message = "Loading was not true!")
                }
                awaitItem().apply {
                    asserter.assertTrue(actual = !loading, message = "Loading was not false!")
                    asserter.assertTrue(actual = this.error != null, message = "error was null!")
                }
                asserter.assertTrue({ "Was navigated!" }, navigated.not())
            }
        }
    }


}