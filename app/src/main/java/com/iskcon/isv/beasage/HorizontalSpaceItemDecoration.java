package com.iskcon.isv.beasage;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by sumitgarg on 27/03/17.
 */

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {
  private int horizontalSpaceWidth;

  public HorizontalSpaceItemDecoration(Context context) {
    horizontalSpaceWidth = (int) context.getResources().getDimension(R.dimen.horizontal_spacing);
  }

  public void setHorizontalSpaceItemDecoration(int verticalSpaceHeight) {
    this.horizontalSpaceWidth = verticalSpaceHeight;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
      outRect.right = horizontalSpaceWidth;
    } else {
      outRect.right = horizontalSpaceWidth / 2;
    }
  }
}

