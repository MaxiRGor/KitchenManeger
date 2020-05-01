package com.distinct.kitchenmanager.element_behaviour.ItemTouchHelper;

public interface ItemTouchHelperAdapter {
    void onItemMoveVertically(int fromPosition, int toPosition);
    void onItemMovedToRight(int position);
    void onItemMovedToLeft(int position);
}
