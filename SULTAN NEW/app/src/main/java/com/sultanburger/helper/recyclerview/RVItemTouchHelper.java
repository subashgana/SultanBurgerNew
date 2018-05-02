package com.sultanburger.helper.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.sultanburger.R;
import com.sultanburger.utils.DisplayUtil;
import com.sultanburger.utils.DrawableUtil;

public class RVItemTouchHelper extends ItemTouchHelper.Callback {

    private static final String TAG = RVItemTouchHelper.class.getSimpleName();

    private final int DEFAULT_IMAGE_LEFT = R.drawable.ic_delete;
    private final int DEFAULT_IMAGE_RIGHT = R.drawable.ic_archive;
    private final int DEFAULT_BACKGROUND_COLOR = R.color.windowBackground;

    private Context context;
    private RVAdapterCallback rvAdapterCallback;

    private int imageLeft = DEFAULT_IMAGE_LEFT;
    private int imageRight = DEFAULT_IMAGE_RIGHT;
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;

    private boolean isSwipeEnabled = true;
    private boolean isSwipeDirectionRight = false;

    public RVItemTouchHelper(Context context, RVAdapterCallback rvAdapterCallback) {
        this.context = context;
        this.rvAdapterCallback = rvAdapterCallback;
    }

    public void setImageLeft(@DrawableRes int imageLeft) {
        this.imageLeft = imageLeft;
    }

    public void setImageRight(@DrawableRes int imageRight) {
        this.imageRight = imageRight;
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        isSwipeEnabled = swipeEnabled;
    }

    public void setSwipeDirectionRight(boolean swipeDirectionRight) {
        isSwipeDirectionRight = swipeDirectionRight;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        int swipeFlags = 0;
        if (isSwipeEnabled) {
            swipeFlags = ItemTouchHelper.END;

            if (isSwipeDirectionRight)
                swipeFlags = ItemTouchHelper.END | ItemTouchHelper.START;
        }

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        return animationType == ItemTouchHelper.ANIMATION_TYPE_DRAG ? DEFAULT_DRAG_ANIMATION_DURATION : DEFAULT_SWIPE_ANIMATION_DURATION;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType())
            return false;

        rvAdapterCallback.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        rvAdapterCallback.onItemDismiss(viewHolder, viewHolder.getAdapterPosition(), direction == ItemTouchHelper.END ? true : false);
    }

    @Override
    public int getBoundingBoxMargin() {
        return super.getBoundingBoxMargin();
    }

    @Override
    public void onChildDraw(final Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View view = viewHolder.itemView;

            if (dX > 0) {
                Bitmap bitmap;
                Drawable drawable = DrawableUtil.getDrawable(context, imageLeft);
                if (drawable instanceof BitmapDrawable) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), imageLeft);
                } else {
                    VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(context.getResources(), imageLeft, null);
                    bitmap = DrawableUtil.getBitmap(vectorDrawableCompat);
                }

                Paint paint = new Paint();
                paint.setColor(DrawableUtil.getColor(context, backgroundColor));

                float leftDrawRect = (float) view.getLeft() + DisplayUtil.dpToPx(0);
                float topDrawRect = (float) view.getTop();
                float rightDrawRect = dX + DisplayUtil.dpToPx(0);
                float bottomDrawRect = (float) view.getBottom();
                canvas.drawRect(leftDrawRect, topDrawRect, rightDrawRect, bottomDrawRect, paint);

                float leftDrawBitmap = (float) view.getLeft() + DisplayUtil.dpToPx(16);
                float topDrawBitmap = (float) view.getTop() + ((float) view.getBottom() - (float) view.getTop() - bitmap.getHeight()) / 2;
                canvas.drawBitmap(bitmap, leftDrawBitmap, topDrawBitmap, paint);
                bitmap.recycle();
            } else if (dX < 0) {
                Bitmap bitmap;
                Drawable drawable = DrawableUtil.getDrawable(context, imageRight);
                if (drawable instanceof BitmapDrawable) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), imageRight);
                } else {
                    VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(context.getResources(), imageRight, null);
                    bitmap = DrawableUtil.getBitmap(vectorDrawableCompat);
                }

                Paint paint = new Paint();
                paint.setColor(DrawableUtil.getColor(context, backgroundColor));

                float leftDrawRect = (view.getRight() + dX) + DisplayUtil.dpToPx(10);
                float topDrawRect = (float) view.getTop();
                float rightDrawRect = (float) view.getRight() + DisplayUtil.dpToPx(0);
                float bottomDrawRect = (float) view.getBottom();
                canvas.drawRect(leftDrawRect, topDrawRect, rightDrawRect, bottomDrawRect, paint);

                float leftDrawBitmap = (float) (view.getRight() - view.getLeft()) - DisplayUtil.dpToPx(32);
                float topDrawBitmap = (float) view.getTop() + ((float) view.getBottom() - (float) view.getTop() - bitmap.getHeight()) / 2;
                canvas.drawBitmap(bitmap, leftDrawBitmap, topDrawBitmap, paint);
                bitmap.recycle();
            }
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            RVViewHolderCallback rvViewHolderCallback = (RVViewHolderCallback) viewHolder;
            rvViewHolderCallback.onItemSelected(context);
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        RVViewHolderCallback rvViewHolderCallback = (RVViewHolderCallback) viewHolder;
        rvViewHolderCallback.onItemClear(context);

    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return 0.1f;
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return 0.9f;
    }
}
