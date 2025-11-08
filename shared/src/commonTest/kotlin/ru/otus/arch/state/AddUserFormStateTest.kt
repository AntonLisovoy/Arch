package ru.otus.arch.state

import ru.otus.arch.data.AppData
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.data.Profile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AddUserFormStateTest : BaseStateTest() {

    fun createState() = AddUserFormState(
        context,
        AppData(),
        null
    )

    @Test
    fun rendersEmptyFormOnStart() = test {
        createState().start(stateMachine)

        verify(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(isEqual(
                AppUiState.AddUserForm(
                    name = "",
                    age = 0,
                    interests = "",
                    addEnabled = false
                )
            ))
        }
    }

    @Test
    fun changesUserName() = test {
        val name = "user"

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.AddUserForm.NameChanged(name))

        verify(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(isEqual(
                AppUiState.AddUserForm(
                    name = "",
                    age = 0,
                    interests = "",
                    addEnabled = false
                )
            ))
            stateMachine.setUiState(isEqual(
                AppUiState.AddUserForm(
                    name = name,
                    age = 0,
                    interests = "",
                    addEnabled = false
                )
            ))
        }
    }

    @Test
    fun changesAge() = test {
        val age = 30

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.AddUserForm.AgeChanged(age))

        verify(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(isEqual(
                AppUiState.AddUserForm(
                    name = "",
                    age = 0,
                    interests = "",
                    addEnabled = false
                )
            ))
            stateMachine.setUiState(isEqual(
                AppUiState.AddUserForm(
                    name = "",
                    age = age,
                    interests = "",
                    addEnabled = false
                )
            ))
        }
    }

    @Test
    fun changesInterests() = test {
        val interests = "kotlin, kmp"

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.AddUserForm.InterestsChanged(interests))

        verify(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(isEqual(
                AppUiState.AddUserForm(
                    name = "",
                    age = 0,
                    interests = "",
                    addEnabled = false
                )
            ))
            stateMachine.setUiState(isEqual(
                AppUiState.AddUserForm(
                    name = "",
                    age = 0,
                    interests = interests,
                    addEnabled = false
                )
            ))
        }
    }

    @Test
    fun transfersToAddUserIfValid() = test {
        val name = "user"
        val age = 30

        every { stateFactory.addingUser(isAny(), isAny()) } returns nextState

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.AddUserForm.NameChanged(name))
        state.process(AppGesture.AddUserForm.AgeChanged(age))
        state.process(AppGesture.Action)

        val profiles = mutableListOf<Profile>()
        verify(exhaustive = false, inOrder = true) {
            stateFactory.addingUser(isAny(), isAny(capture = profiles))
            stateMachine.setMachineState(nextState)
        }

        val profile = profiles.single()
        assertEquals(name, profile.name)
        assertEquals(age, profile.age)
    }

    @Test
    fun doesNotTransferToAddUserIfNotValid() = test {
        val name = "user"

        every { stateFactory.addingUser(isAny(), isAny()) } returns nextState

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.AddUserForm.NameChanged(name))

        mocker.clearCalls()
        state.process(AppGesture.Action)
        verify {
            // No calls
        }
    }

    @Test
    fun enablesSubmitWhenNameAndAgeAreSet() = test {
        val name = "user"
        val age = 30

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.AddUserForm.NameChanged(name))
        state.process(AppGesture.AddUserForm.AgeChanged(age))

        verify(exhaustive = false, inOrder = true) {
            stateMachine.setUiState(isEqual(
                AppUiState.AddUserForm(
                    name = "",
                    age = 0,
                    interests = "",
                    addEnabled = false
                )
            ))
            stateMachine.setUiState(isEqual(
                AppUiState.AddUserForm(
                    name = name,
                    age = age,
                    interests = "",
                    addEnabled = true
                )
            ))
        }
    }

    @Test
    fun returnsToListOnBack() = test {
        every { stateFactory.userList(isAny()) } returns nextState

        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.Back)

        verify(exhaustive = false, inOrder = true) {
            stateFactory.userList(isAny())
            stateMachine.setMachineState(nextState)
        }
    }
}