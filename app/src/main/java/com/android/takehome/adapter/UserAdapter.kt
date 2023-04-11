package com.android.takehome.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.takehome.databinding.CustomListBinding
import com.android.takehome.models.UserRepos

class UserAdapter(val userList: List<UserRepos>): RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    var onItemClick:((position: Int) -> Unit)?=null
    class MyViewHolder(binding: CustomListBinding): RecyclerView.ViewHolder(binding.root){
        val binding = binding
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding= CustomListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)

    }
    override fun getItemCount(): Int {
        return userList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.userRepos=userList.get(position)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }


    }
}