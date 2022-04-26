package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var rname : EditText
    private lateinit var rstep : EditText
    private lateinit var rmat : EditText
    private lateinit var btnsv : Button
    private lateinit var listrcp : ListView
    private lateinit var ref : DatabaseReference
    private lateinit var rcpList: MutableList<recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ref = FirebaseDatabase.getInstance().getReference(  "recipe" )

        rname = findViewById(R.id.r_name)
        rstep = findViewById(R.id.r_step)
        rmat = findViewById(R.id.r_mat)
        btnsv = findViewById(R.id.btn_sv)
        listrcp = findViewById(R.id.lv_rcp)
        btnsv.setOnClickListener(this)

        rcpList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    rcpList.clear()
                    for (h in p0.children) {
                        val recipe1 = h.getValue(recipe::class.java)
                        if (recipe1 != null) {
                            rcpList.add(recipe1)
                        }
                    }

                    val adapter = recipeAdapter(this@MainActivity, R.layout.item_rcp,rcpList)
                    listrcp.adapter = adapter
                }
            }
        })
    }

    override fun onClick(v: View?){
         saveData()
    }

    private fun saveData(){
        val name : String = rname.text.toString().trim()
        val step : String = rstep.text.toString().trim()
        val mat : String = rmat.text.toString().trim()

        if (name.isEmpty()){
            rname.error = "Please Enter The Recipe Name"
            return
        }

        if (step.isEmpty()){
            rstep.error = "Please Enter the Recipe Step"
            return
        }
        if (mat.isEmpty()){
            rmat.error = "Please Enter the Recipe Step"
            return
        }


        val rpId = ref.push().key

        val rcp = recipe(rpId!!,name,step,mat)

        if (rpId != null){
            ref.child(rpId).setValue(rcp).addOnCompleteListener{
                Toast.makeText(applicationContext, "Recipe Add", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


