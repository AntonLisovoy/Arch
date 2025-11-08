package ru.otus.arch.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.CommonStateMachine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.kodein.mock.Mock
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.net.SessionManager
import ru.otus.arch.net.usecase.AddUser
import ru.otus.arch.net.usecase.DeleteUser
import ru.otus.arch.net.usecase.LoadUserProfile
import ru.otus.arch.net.usecase.LoadUsers
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
@UsesMocks(
    AppStateFactory::class,
    CommonStateMachine::class,
    SessionManager::class,
    LoadUsers::class,
    LoadUserProfile::class,
    AddUser::class,
    DeleteUser::class
)
internal abstract class BaseStateTest : TestsWithMocks() {
    override fun setUpMocks() {
        mocker.injectMocks(this)
    }

    @Mock
    lateinit var stateFactory: AppStateFactory
    @Mock
    lateinit var stateMachine: CommonStateMachine<AppGesture, AppUiState>

    // MockMp injects unpredictable when working with base classes
    // So all mocks initialized here
    @Mock
    lateinit var sessionManager: SessionManager
    @Mock
    lateinit var loadUsers: LoadUsers
    @Mock
    lateinit var loadProfile: LoadUserProfile
    @Mock
    lateinit var addUser: AddUser
    @Mock
    lateinit var deleteUser: DeleteUser


    protected lateinit var context: AppContext
    protected lateinit var nextState: CommonMachineState<AppGesture, AppUiState>
    protected lateinit var dispatcher: TestDispatcher

    @BeforeTest
    fun init() {
        every { stateMachine.setUiState(isAny()) } returns Unit
        every { stateMachine.setMachineState(isAny()) } returns Unit

        context = object : AppContext {
            override val factory: AppStateFactory = stateFactory
        }

        nextState = object : CommonMachineState<AppGesture, AppUiState>() { }
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)

        doInit()
    }

    protected fun doInit() = Unit

    @AfterTest
    fun deinit() {
        Dispatchers.resetMain()
    }

    protected fun test(testBody: suspend TestScope.() -> Unit) = runTest(dispatcher) {
        testBody()
    }
}