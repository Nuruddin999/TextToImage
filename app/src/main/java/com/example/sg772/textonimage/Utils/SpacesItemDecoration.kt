package com.example.sg772.textonimage.Utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpacesItemDecoration: RecyclerView.ItemDecoration {
     var space: Int? =null

    constructor(space: Int){
        this.space=space
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view)==state.itemCount-1){
            outRect.left= space!!
            outRect.right=0
        } else {
            outRect.left= 0
            outRect.right=space!!
        }
    }
}