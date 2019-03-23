package com.example.sg772.textonimage.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.DialogTitle
import java.util.ArrayList

class ViewPagerAdapter: FragmentPagerAdapter {
   private val fragmentList=ArrayList<Fragment>()
   private val fragmentTitleList=ArrayList<String>()



    constructor(fragmentManager: FragmentManager): super(fragmentManager){

    }

    override fun getItem(p0: Int): Fragment {
return fragmentList.get(p0)

    }

    override fun getCount(): Int {
        return fragmentList.size
    }
fun addFragment(fragment: Fragment,title: String) {
    fragmentList.add(fragment)
    fragmentTitleList.add(title)

}

    override fun getPageTitle(position: Int): CharSequence? {
      return  fragmentTitleList.get(position)
    }

}