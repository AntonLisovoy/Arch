package ru.otus.arch.state

import ru.otus.arch.data.AppData
import ru.otus.arch.data.AppGesture
import ru.otus.arch.data.AppUiState
import ru.otus.arch.data.Profile
import kotlin.properties.Delegates

internal class AddUserFormState(
    context: AppContext,
    private val data: AppData,
    profile: Profile?
) : BaseAppState(context) {
    private var profile by Delegates.observable(profile ?: Profile.EMPTY) { _, _, newValue ->
        render()
    }

    private var interestsString by Delegates.observable(profile?.interests?.joinToString().orEmpty()) { _, _, newValue ->
        render()
    }

    override fun doStart() {
        render()
    }

    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            AppGesture.Action -> addUser()
            AppGesture.Back -> setMachineState(factory.userList(data))
            is AppGesture.AddUserForm.NameChanged -> {
                profile = profile.copy(name = gesture.name)
            }
            is AppGesture.AddUserForm.AgeChanged -> {
                profile = profile.copy(age = gesture.age)
            }
            is AppGesture.AddUserForm.InterestsChanged -> {
                interestsString = gesture.interests
            }
            else -> super.doProcess(gesture)
        }
    }

    private fun addUser() {
        val newProfile = profile.copy(interests = interestsString.split(",").map { it.trim() }.toSet())
        if (newProfile.isValid()) {
            setMachineState(factory.addingUser(data, newProfile))
        }
    }

    private fun Profile.isValid() = name.isNotBlank() && age > 0

    private fun render() {
        setUiState(
            AppUiState.AddUserForm(
                name = profile.name,
                age = profile.age,
                interests = interestsString,
                addEnabled = profile.isValid()
            )
        )
    }
}