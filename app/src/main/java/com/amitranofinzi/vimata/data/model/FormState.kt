package com.amitranofinzi.vimata.data.model

data class FormState (
    var name: String = "",
    var surname: String = "",
    //var username: String = "",
    var userType: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var emailError: Boolean = false,
    var passwordError: Boolean = false,
    var confirmPasswordError: Boolean = false,
    var emailErrorMessage: String = "",
    var passwordErrorMessage: String = ""
)