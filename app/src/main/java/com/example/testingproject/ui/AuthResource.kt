package com.example.testingproject.ui

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class AuthResource<T>(
    var authStatus : AuthStatus? = null,
    var data : T,
    var msg : String? = null

)

fun <T> success(@Nullable data: T): AuthResource<T> {
    return AuthResource(AuthStatus.Success, data, null)
}

fun <T> Error(@NonNull msg: String?, @Nullable data: T) : AuthResource<T>? {
    return AuthResource(AuthStatus.ERROR, data, msg)
}

enum class AuthStatus {
    Success, ERROR
}







