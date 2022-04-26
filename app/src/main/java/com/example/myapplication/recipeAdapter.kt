package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class recipeAdapter (val mCtx : Context, val layoutResId : Int, val rcpList : List<recipe>) :ArrayAdapter<recipe> (mCtx, layoutResId, rcpList ) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        val view: View = layoutInflater.inflate(layoutResId, null)

        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvStep: TextView = view.findViewById(R.id.tv_step)
        val tvMat: TextView = view.findViewById(R.id.tv_mat)
        val tvEdit: TextView = view.findViewById(R.id.tv_edit)


        val recipe1: recipe = rcpList[position]

        tvEdit.setOnClickListener{
            showUpdateDialog(recipe1)
        }


        tvName.text = recipe1.name
        tvStep.text = recipe1.step
        tvMat.text = recipe1.mat

        return view
    }

    fun showUpdateDialog(recipe1: recipe) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Edit Data")

        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.update_dialog, null)

        val rname = view.findViewById<EditText>(R.id.r_name)
        val rstep = view.findViewById<EditText>(R.id.r_step)
        val rmat = view.findViewById<EditText>(R.id.r_mat)

        rname.setText(recipe1.name)
        rstep.setText(recipe1.step)
        rmat.setText(recipe1.mat)

        builder.setView(view)

        builder.setPositiveButton("Update"){p0,p1 ->
            val dbMhs = FirebaseDatabase.getInstance().getReference("recipe")

            val name = rname.text.toString().trim()
            val step = rstep.text.toString().trim()
            val mat = rmat.text.toString().trim()
            if (name.isEmpty()){
                rname.error = "Please Enter Recipe Name"
                rname.requestFocus()
                return@setPositiveButton
            }
            if (step.isEmpty()){
                rstep.error = "Please Enter Recipe Step"
                rstep.requestFocus()
                return@setPositiveButton
            }
            if (mat.isEmpty()){
                rmat.error = "Please Enter Recipe Step"
                rmat.requestFocus()
                return@setPositiveButton
            }

            val recipe1 = recipe(recipe1.id, name, step, mat)

            dbMhs.child(recipe1.id!!).setValue(recipe1)

            Toast.makeText(mCtx, "Update Complete", Toast.LENGTH_SHORT).show()

        }

        builder.setNeutralButton("Delete"){p0,p1 ->

            val dbMhs = FirebaseDatabase.getInstance().getReference("recipe").child(recipe1.id)

            dbMhs.removeValue()

            Toast.makeText(mCtx, "Recipe Delete", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("No"){p0,p1 ->

        }

        val alert = builder.create()
        alert.show()
    }
}