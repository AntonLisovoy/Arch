package ru.otus.arch.data

import kotlin.js.JsExport

@JsExport
sealed class AppGesture {
    object Back : AppGesture()
    object Action : AppGesture()

    data class UserSelected(val userId: Int) : AppGesture()

    data object Login : AppGesture()
    data object Logout : AppGesture()
    data object AddUser : AppGesture()

    sealed class AddUserForm : AppGesture() {
        data class NameChanged(val name: String) : AppGesture()
        data class AgeChanged(val age: Int) : AppGesture()
        data class InterestsChanged(val interests: String) : AppGesture()
    }

    data object DeleteUser : AppGesture()

    data class UsernameChanged(val username: String) : AppGesture()
    data class PasswordChanged(val password: String) : AppGesture()
}