package ru.otus.arch.state

import ru.otus.arch.data.AppData
import ru.otus.arch.data.Profile

internal interface AppStateFactory {
    fun welcome(): AppState
    fun userList(data: AppData): AppState
    fun userListError(data: AppData, error: Throwable): AppState
    fun userProfile(data: AppData, id: Int): AppState
    fun userProfileError(data: AppData, id: Int, error: Throwable): AppState
    fun userListLogin(data: AppData): AppState
    fun addUserForm(data: AppData, profile: Profile? = null): AppState
    fun addingUser(data: AppData, profile: Profile): AppState
    fun addingUserError(data: AppData, profile: Profile, error: Throwable): AppState
    fun deletingUser(data: AppData, id: Int): AppState
    fun deletingUserError(data: AppData, id: Int, error: Throwable): AppState
    fun terminated(): AppState
}

internal class AppStateFactoryImpl(
    private val createUserListState: UserListState.Factory,
    private val createUserProfileState: UserProfileState.Factory,
    private val createLogin: BasicLoginState.Factory,
    private val createAddingUserState: AddingUserState.Factory,
    private val createDeletingUserState: DeletingUserState.Factory
) : AppStateFactory {

    private val context = object : AppContext {
        override val factory: AppStateFactory = this@AppStateFactoryImpl
    }

    override fun welcome() = WelcomeState(context)

    override fun userList(data: AppData) = createUserListState(context, data)

    override fun userListError(data: AppData, error: Throwable) = when {
        error.isUnauthorized() -> createLogin(
            context,
            error,
            { userList(data) },
            { terminated() }
        )
        else -> ErrorState(
            context,
            error,
            { userList(data) },
            { terminated() }
        )
    }

    override fun userProfile(data: AppData, id: Int) = createUserProfileState(
        context,
        data,
        id
    )

    override fun userProfileError(data: AppData, id: Int, error: Throwable) = when {
        error.isUnauthorized() -> createLogin(
            context,
            error,
            { userProfile(data, id) },
            { userList(data) }
        )
        else -> ErrorState(
            context,
            error,
            { userProfile(data, id) },
            { userList(data) }
        )
    }

    override fun userListLogin(data: AppData): AppState = createLogin(
        context,
        null,
        { userList(data) },
        { userList(data) }
    )

    override fun addUserForm(data: AppData, profile: Profile?) = AddUserFormState(
        context,
        data,
        profile
    )

    override fun addingUser(data: AppData, profile: Profile) = createAddingUserState(
        context,
        data,
        profile
    )

    override fun addingUserError(data: AppData, profile: Profile, error: Throwable) = when {
        error.isUnauthorized() -> createLogin(
            context,
            error,
            { addingUser(data, profile) },
            { addUserForm(data, profile) }
        )
        else -> ErrorState(
            context,
            error,
            { addingUser(data, profile) },
            { addUserForm(data, profile) }
        )
    }

    override fun deletingUser(data: AppData, id: Int) = createDeletingUserState(
        context,
        data,
        id
    )

    override fun deletingUserError(data: AppData, id: Int, error: Throwable) = when {
        error.isUnauthorized() -> createLogin(
            context,
            error,
            { deletingUser(data, id) },
            { userProfile(data, id) }
        )
        else -> ErrorState(
            context,
            error,
            { deletingUser(data, id) },
            { userProfile(data, id) }
        )
    }

    override fun terminated() = TerminatedState()
}