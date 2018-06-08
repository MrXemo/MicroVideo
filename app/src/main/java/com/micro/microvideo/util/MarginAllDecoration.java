package com.micro.microvideo.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class MarginAllDecoration extends RecyclerView.ItemDecoration {
  private int margin;

  public MarginAllDecoration(int i) {
    this.margin = i;
  }

  @Override
  public void getItemOffsets(
          Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    outRect.set(margin, margin, margin, margin);
  }
}