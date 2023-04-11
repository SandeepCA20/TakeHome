package com.android.takehome

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.takehome.adapter.UserAdapter
import com.android.takehome.databinding.ActivityMainBinding
import com.android.takehome.models.UserRepos
import com.android.takehome.utils.showCustomDialog
import com.android.takehome.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var userRepoData: List<UserRepos>
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }
    //initialize view
    private fun init(){
        mainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        mainViewModel= ViewModelProvider(this)[MainViewModel::class.java]
        mainBinding.recyclerView.layoutManager= LinearLayoutManager(this)
        mainBinding.mainViewModel=mainViewModel
        mainBinding.lifecycleOwner=this
        onKeyboardSearchClick(mainBinding.editTextTextPersonName)
        updateUsers()
        updateRepo()
        handleError()
    }
    //Observe data for search users from api
    private fun updateUsers(){
        mainViewModel.usersLiveData.observe(this) {
            showOrHideView(true)
            mainBinding.usernameTxtView.text = it.name
            Picasso.get().load(it.avatar_url).fit().into(mainBinding.userImg)
        }
    }

    //Handle api error if any
    private fun handleError(){
        mainViewModel.errorMessage.observe(this) {
            mainBinding.usernameTxtView.text = it
        }
    }
    //Observe Repos for search users from api
    private fun updateRepo(){
        mainViewModel.reposLiveData.observe(this) {
            userRepoData = it
            showOrHideView(true)
            adapter = UserAdapter(it)
            mainBinding.recyclerView.adapter = adapter

            ///Handle repository onItem Click
            adapter.onItemClick = { position ->
                val message = "Updated at - ${userRepoData[position].updated_at}\nRating - ${
                    userRepoData[position].stargazers_count
                }\nForked - " + userRepoData[position].forks
                showCustomDialog(this, userRepoData[position].name, message)

            }
        }
    }
    ///Handle search click
    fun onSearchClick(view: View){
        if(TextUtils.isEmpty(mainBinding.editTextTextPersonName.text.trim())){
            mainBinding.editTextTextPersonName.error = getString(R.string.error_enter_github_id)
            return
        }
        hideKeybaord(view)
        showOrHideView(false)
        mainViewModel.getUsersData(mainBinding.editTextTextPersonName.text.toString())
        mainViewModel.getUsersRepo(mainBinding.editTextTextPersonName.text.toString())
    }

    //hide and show view on basis of response
    private fun showOrHideView(isShow: Boolean){
        if(!isShow){
            mainBinding.recyclerView.visibility=View.GONE
            mainBinding.userImg.visibility=View.GONE
        }else{
            mainBinding.recyclerView.visibility=View.VISIBLE
            mainBinding.userImg.visibility=View.VISIBLE
        }
    }

    //hide keyboard on search button click
    private fun hideKeybaord(v: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }

    ///handle keyboard search click
    private fun onKeyboardSearchClick(search: EditText){
        search.setOnEditorActionListener(TextView.OnEditorActionListener{ _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearchClick(search)
                return@OnEditorActionListener true
            }
            false
        })

    }
}