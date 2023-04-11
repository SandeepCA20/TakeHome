package com.android.takehome.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.takehome.models.User
import com.android.takehome.models.UserRepos
import com.android.takehome.retrofit.GitApi
import org.json.JSONObject
import javax.inject.Inject

class GitRepository @Inject constructor(private val gitApi: GitApi) {
    private val _users=MutableLiveData<User>()
    val users: LiveData<User>
    get() = _users

    private val _repos=MutableLiveData<List<UserRepos>>()
    val repos: LiveData<List<UserRepos>>
    get() = _repos

    private val _errorMessage= MutableLiveData<String>()
    val errorMessage: LiveData<String>
    get() = _errorMessage

    suspend fun getUsers(searchRepoByUser: String){
        val result= gitApi.getUser(searchRepoByUser)
        if(result.isSuccessful && result.body()!=null){
            _users.value= result.body()
        }else if(result.errorBody()!=null){
            val jsonObj = JSONObject(result.errorBody()!!.charStream().readText())
            val message=jsonObj.getString("message")
            _errorMessage.value=message
        }
    }

    suspend fun getRepos(searchRepoByUser: String){
        val result=gitApi.getRepos(searchRepoByUser)
        if(result.isSuccessful && result.body()!=null){
            _repos.value= result.body()
        }else if(result.errorBody()!=null){
            val jsonObj = JSONObject(result.errorBody()!!.charStream().readText())
            val message=jsonObj.getString("message")
            _errorMessage.value=message
        }
    }
}