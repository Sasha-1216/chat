package com.example.chat


import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.chat.MyContactAdapter

/**
 * A simple [Fragment] subclass.
 */
class ContactFragmentSearchAdd : Fragment() {

    lateinit var buttonSearch: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var myContactAdapter: MyContactAdapter
    lateinit var linearLayoutManager : LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_contact_search_add,
            container,
            false
        )

        buttonSearch = view.findViewById(R.id.buttonSearch)

        recyclerView = view.findViewById(R.id.myContactList)

        var results : Array<Contact>
        val context: Context = view.context


        fun displayResult(){

            val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            val randomString1 = (1..10)
                .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")

            val randomString2 = (1..10)
                .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")

            results = arrayOf(
                Contact(randomString1, "fake1@resutls.com"),
                Contact(randomString2, "fake2@results.com")
            )
            myContactAdapter = MyContactAdapter(context, results)
            recyclerView.adapter = myContactAdapter
            linearLayoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = linearLayoutManager

        }

//        fun setVisibility( isVisible : Boolean){
//
//            lateinit var param : RecyclerView.LayoutParams
//            param =
//
//
//            if (isVisible){
//                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
//                itemView.setVisibility(View.VISIBLE);
//            }else{
//                itemView.setVisibility(View.GONE);
//                param.height = 0;
//                param.width = 0;
//            }
//            itemView.setLayoutParams(param);
//        }





        buttonSearch.setOnClickListener{
            displayResult()
        }


        //dispatch keyboard touch event

        view.setOnTouchListener { _, event ->

            if (event.action == MotionEvent.ACTION_DOWN) {
                val view = activity?.currentFocus
                if (view is EditText) {

                    val outRect = Rect()
                    view.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        view.clearFocus()
                        val imm =
                            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                        imm!!.hideSoftInputFromWindow(view.getWindowToken(), 0)
                    }
                }
            }
            false
        }

        return view
    }

}
