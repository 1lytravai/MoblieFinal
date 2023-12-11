package com.example.mobliefinal
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % 2 // Số cột là 2

        outRect.left = column * spacing / 2
        outRect.right = spacing - (column + 1) * spacing / 2

        if (position >= 2) {
            outRect.top = spacing // Thêm khoảng cách giữa các hàng
        }
    }
}