package com.example.facebookpractice.authentication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facebookpractice.authentication.data.state.SignUpState
import com.example.facebookpractice.authentication.domain.repository.AuthRepository
import com.example.facebookpractice.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _signUpState = Channel<SignUpState>()
    val signUpState = _signUpState.receiveAsFlow()

    fun registerUser(
        email: String,
        password: String,
        name: String,
        username: String,
        surname: String
    ) = viewModelScope.launch {
        repository.registerUser(email, password, name, username, surname).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _signUpState.send(
                        SignUpState(
                            isSuccess = "You have successfully login into account"
                        )
                    )
                }

                is Resource.Error -> {
                    _signUpState.send(SignUpState(isError = result.message.toString()))
                }

                is Resource.Loading -> {
                    _signUpState.send(SignUpState(isLoading = true))
                }
            }

        }
    }
}