package com.distinct.kitchenmanager.element_behaviour.ItemTouchHelper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.ApplicationContextSingleton;
import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.ui.fridge.FridgeViewHolder;
import com.distinct.kitchenmanager.ui.shopping_list.ShoppingListViewHolder;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;
    private int colorToMoveLeft;
    private int colorToMoveRight;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0; /*ItemTouchHelper.UP | ItemTouchHelper.DOWN;*/
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        setColors(viewHolder);
        return makeMovementFlags(dragFlags, swipeFlags);
    }


    private void setColors(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getClass() == ShoppingListViewHolder.class) {
            colorToMoveLeft = ContextCompat.getColor(ApplicationContextSingleton.getInstance().getApplicationContext(), R.color.colorToMoveToFridge);
            colorToMoveRight = ContextCompat.getColor(ApplicationContextSingleton.getInstance().getApplicationContext(), R.color.colorToDelete);
        } else if (viewHolder.getClass() == FridgeViewHolder.class) {
            colorToMoveLeft = ContextCompat.getColor(ApplicationContextSingleton.getInstance().getApplicationContext(), R.color.colorToConsume);
            colorToMoveRight = ContextCompat.getColor(ApplicationContextSingleton.getInstance().getApplicationContext(), R.color.colorToMoveToShoppingList);
        }

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        //  mAdapter.onItemMoveVertically(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.START) {
            mAdapter.onItemMovedToLeft(viewHolder.getAdapterPosition());
            Log.d("a", "moved left");
        } else if (direction == ItemTouchHelper.END) {
            mAdapter.onItemMovedToRight(viewHolder.getAdapterPosition());
            Log.d("a", "moved to right");
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            Paint p = new Paint();
            float width = (float) viewHolder.itemView.getWidth();
            float alpha = 1.0f - Math.abs(dX) / width;
            if (dX > 0) {
                p.setColor(colorToMoveRight);
                p.setAlpha(255 - (int) (alpha * 255));
            } else if (dX < 0) {
                p.setColor(colorToMoveLeft);
                p.setAlpha(255 - (int) (alpha * 255));
            } else p.setColor(Color.TRANSPARENT);

            int bound = 16;
           c.drawRect(viewHolder.itemView.getLeft(), viewHolder.itemView.getTop() + bound, viewHolder.itemView.getRight(), viewHolder.itemView.getBottom() - bound, p);



            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }

}