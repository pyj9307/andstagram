package com.example.ggestagram.message

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.ggestagram.LoginActivity
import com.example.ggestagram.databinding.ActivityMain1Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMain1Binding
    lateinit var adapter: UserAdapter

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    private lateinit var userList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView (this, com.example.ggestagram.R.layout.activity_main1)
        //인증 초기화
        mAuth = Firebase.auth

        //db 초기화
        mDbRef = Firebase.database.reference

        //리스트 초기화
        userList = ArrayList()

        adapter = UserAdapter(this, userList)

//        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.userRecyclerView.adapter = adapter

//        val recyclerViewNews = binding.userRecyclerView
//        val largeNews = DummyData.getDummyData(this)
//        val newsAdapter = MessageAdapter(largeNews)
//
//        recyclerViewNews.adapter = newsAdapter
//        recyclerViewNews.layoutManager = LinearLayoutManager(this)
//        recyclerViewNews.setHasFixedSize(true)


        //사용자 정보 가져오기
        mDbRef.child("user").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    //유저 정보
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if(mAuth.currentUser?.uid != currentUser?.uId){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //실패 시 실행
            }
        })
    }//onCreate

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.ggestagram.R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == com.example.ggestagram.R.id.log_out){
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return true
    }
}