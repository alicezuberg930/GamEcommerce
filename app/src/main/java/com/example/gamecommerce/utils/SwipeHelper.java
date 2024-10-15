package com.example.gamecommerce.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class SwipeHelper extends ItemTouchHelper.SimpleCallback {
    private int buttonWidth;
    private RecyclerView recyclerView;
    private List<DeleteButton> buttonList;
    private GestureDetector gestureDetector;
    private int swipePosition = -1;
    private float swipeThreshold = 0.5f;
    private Map<Integer, List<DeleteButton>> buttonBuffer;
    private Queue<Integer> removeQueue;

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            for(DeleteButton button: buttonList) {
                if(button.onClick(e.getX(), e.getY())) {
                    break;
                }
            }
            return true;
        }
    };

    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(swipePosition < 0) {
                return false;
            }
            Point p = new Point((int)event.getRawX(), (int)event.getRawY());
            RecyclerView.ViewHolder swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition);
            if(swipeViewHolder == null) return false;
            View swipedItem = swipeViewHolder.itemView;
            Rect rect = new Rect();
            swipedItem.getGlobalVisibleRect(rect);
            if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) {
                if(rect.top < p.y && rect.bottom > p.y) {
                    gestureDetector.onTouchEvent(event);
                } else {
                    removeQueue.add(swipePosition);
                    swipePosition = -1;
                    recoverSwipedItem();
                }
            }
            return false;
        }
    };

    public SwipeHelper(Context context, RecyclerView recyclerView, int buttonWidth) {
        super(0, ItemTouchHelper.LEFT);
        this.recyclerView = recyclerView;
        this.buttonList = new ArrayList<>();
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        this.buttonBuffer = new HashMap<>();
        this.buttonWidth = buttonWidth;
        removeQueue = new LinkedList<Integer>() {
            @Override
            public boolean add(Integer o) {
                if(contains(o)) return false;
                return super.add(o);
            }
        };
        attachSwipe();
    }

    private void attachSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private synchronized void recoverSwipedItem() {
        while (!removeQueue.isEmpty()) {
            int pos = removeQueue.poll();
            if(pos > -1) {
                recyclerView.getAdapter().notifyItemChanged(pos);
            }
        }
    }

    public class DeleteButton {
        private int icon ,color, position;
        private RectF clickRegion;
        private final DeleteButtonClickListener listener;
        private final Context context;
        private Resources resources;

        public DeleteButton(Context context, int icon, int color, DeleteButtonClickListener listener) {
            this.icon = icon;
            this.color = color;
            this.listener = listener;
            this.context = context;
            this.resources = context.getResources();
        }

        public boolean onClick(float x, float y) {
            if(clickRegion != null && clickRegion.contains(x,y)) {
                listener.onClick(position);
                return true;
            }
            return false;
        }

        public void onDraw(Canvas canvas, RectF rectF, int position) {
            Paint p = new Paint();
            p.setColor(color);
            canvas.drawRect(rectF, p);
//            Rect rect = new Rect();
//            float cHeight = rectF.height();
//            float cWidth = rectF.width();
//            p.setTextAlign(Paint.Align.CENTER);
            Drawable d = ContextCompat.getDrawable(context, icon);
            Bitmap bitmap = drawableToBitmap(d);
            canvas.drawBitmap(bitmap, ((rectF.left + rectF.right) / 2) - (float)bitmap.getWidth() / 2, ((rectF.top + rectF.bottom) / 2) - (float)bitmap.getHeight() / 2, p);
            clickRegion = rectF;
            this.position = position;
        }

        private Bitmap drawableToBitmap(Drawable d) {
            if(d instanceof BitmapDrawable) {
                return ((BitmapDrawable) d).getBitmap();
            }
            Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            d.draw(canvas);
            return bitmap;
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        if(swipePosition != pos) {
            removeQueue.add(swipePosition);
        }
        swipePosition = pos;
        if(buttonBuffer.containsKey(swipePosition)) {
            buttonList = buttonBuffer.get(swipePosition);
        } else {
            buttonList.clear();
        }
        swipeThreshold = 0.5f * buttonList.size() * buttonWidth;
        recoverSwipedItem();
    }

    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.5f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 10.0f * defaultValue;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;
        if(pos < 0) {
            swipePosition = pos;
            return;
        }
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if(dX < 0) {
                List<DeleteButton> buffer = new ArrayList<>();
                if(!buttonBuffer.containsKey(pos)) {
                    instantiateDeleteButton(viewHolder, buffer);
                    buttonBuffer.put(pos, buffer);
                } else {
                    buffer = buttonBuffer.get(pos);
                }
                translationX = dX * buffer.size() * buttonWidth / itemView.getWidth();
                drawButton(c, itemView, buffer, pos, translationX);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private void drawButton(Canvas c, View itemView, List<DeleteButton> buffer, int pos, float translationX) {
        float right = itemView.getRight();
        float dButtonWidth = -1 * translationX / buffer.size();
        for(DeleteButton button: buffer) {
            float left = right - dButtonWidth;
            button.onDraw(c, new RectF(left, itemView.getTop(), right, itemView.getBottom()), pos);
            right = left;
        }
    }

    public abstract void instantiateDeleteButton(RecyclerView.ViewHolder viewHolder, List<DeleteButton> buffer);
}
