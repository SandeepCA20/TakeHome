package com.android.takehome.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.takehome.models.User
import com.android.takehome.models.UserRepos
import com.android.takehome.repository.GitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: GitRepository): ViewModel() {

    val usersLiveData: LiveData<User>
    get() = repository.users

    val reposLiveData: LiveData<List<UserRepos>>
    get() = repository.repos

    val errorMessage: LiveData<String>
    get() = repository.errorMessage
    fun getUsersData(searchName: String){
        viewModelScope.launch {
            repository.getUsers(searchName)
        }
    }

    fun getUsersRepo(searchName: String){
        viewModelScope.launch {
            repository.getRepos(searchName)
        }
    }
}