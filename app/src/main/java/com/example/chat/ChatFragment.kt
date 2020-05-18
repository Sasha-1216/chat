package com.example.chat


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_setting.*


/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    lateinit var buttonTest: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(
            com.example.chat.R.layout.fragment_chat,
            fragmentParentViewGroup,
            false
        )

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val chatViewFragment: Fragment = ChatFragmentMessageView()
        val manager = activity?.supportFragmentManager
        val transaction = manager?.beginTransaction()
        transaction?.replace(R.id.fragmentParentViewGroup, chatViewFragment)
        transaction?.commit()
        return
    }
}




