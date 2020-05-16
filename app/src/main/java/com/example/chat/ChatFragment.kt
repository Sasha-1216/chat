package com.example.chat


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    lateinit var buttonTest: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_chat, container, false)

        val listener = object : View.OnClickListener {
            override fun onClick(view: View) {

                var fragment: Fragment? = null
                if (view == view.findViewById(R.id.button1)) {
                    fragment = FragmentOne()
                } else {
                    fragment = FragmentTwo()
                }

                val manager = activity?.supportFragmentManager

                val transaction = manager?.beginTransaction()
                transaction?.replace(R.id.output, fragment)
                transaction?.commit()
            }
        }

        val btn1 = view.findViewById(R.id.button1) as Button
        btn1.setOnClickListener(listener)

        val btn2 = view.findViewById(R.id.button2) as Button
        btn2.setOnClickListener(listener)

        return view
    }
}




