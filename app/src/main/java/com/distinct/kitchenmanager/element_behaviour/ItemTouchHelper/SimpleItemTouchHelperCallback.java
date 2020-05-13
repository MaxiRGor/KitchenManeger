package com.distinct.kitchenmanager.element_behaviour.ItemTouchHelper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.distinct.kitchenmanager.ApplicationContextSingleton;
import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.ui.shopping_list.ShoppingListViewHolder;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;
    private int colorOfSwipedItem;
    private int colorToSwipedText;

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
        setColors();
        return makeMovementFlags(dragFlags, swipeFlags);
    }


    private void setColors() {
        colorOfSwipedItem = ContextCompat.getColor(ApplicationContextSingleton.getInstance().getApplicationContext(), R.color.colorPrimary);
        colorToSwipedText = ContextCompat.getColor(ApplicationContextSingleton.getInstance().getApplicationContext(), R.color.white);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.START) {
            mAdapter.onItemMovedToLeft(viewHolder.getAdapterPosition());
        } else if (direction == ItemTouchHelper.END) {
            mAdapter.onItemMovedToRight(viewHolder.getAdapterPosition());
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            Paint p = new Paint();
            int bound = 0;
            if (dX != 0) p.setColor(colorOfSwipedItem);
            else p.setColor(Color.TRANSPARENT);
            c.drawRect(viewHolder.itemView.getLeft(), viewHolder.itemView.getTop() + bound, viewHolder.itemView.getRight(), viewHolder.itemView.getBottom() - bound, p);
            viewHolder.itemView.setTranslationX(dX);
            drawText(dX, c, viewHolder.itemView, viewHolder);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX , dY,
                    actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }

    private void drawText(float dX, Canvas c, View itemView, RecyclerView.ViewHolder viewHolder) {
        Paint p = new Paint();
        float textSize = 56;
        int verticalBound = 16;
        p.setColor(colorToSwipedText);
        p.setAntiAlias(true);
        p.setTextSize(textSize);
        p.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        String text;
        int x;
        if (dX > 0) {
            if (viewHolder.getClass() == ShoppingListViewHolder.class)
                text = ApplicationContextSingleton.getInstance().getApplicationContext().getString(R.string.delete);
            else
                text = ApplicationContextSingleton.getInstance().getApplicationContext().getString(R.string.consume_fully);
            x = verticalBound;
        } else if (dX < 0) {
            if (viewHolder.getClass() == ShoppingListViewHolder.class)
                text = ApplicationContextSingleton.getInstance().getApplicationContext().getString(R.string. move_to_fridge);
            else
                text = ApplicationContextSingleton.getInstance().getApplicationContext().getString(R.string.partly_consume);
            x = itemView.getWidth() - verticalBound - (int) (p.measureText(text));
        } else {
            text = "";
            x = 0;
        }

        c.drawText(text, x, itemView.getY() + ((itemView.getHeight() + textSize) / 2), p);
    }

}