package com.google.engedu.puzzle8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();
//    private static int[] number = {0, 1, 2, 3, 4, 5, 6, 7, 8};

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap, View parent) {
        int width = getWidth();
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, width, width, true);
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
//        if (animation == null && puzzleBoard != null) {
//            // Do something.
//            if (random == null) random = new Random();
//            int count = 9;
//            for (int i = count; i > 1; i--) {
//                int rand=random.nextInt(i);
//                puzzleBoard.swapTiles(i-1,rand);
//            }
//
//        }
//        if(animation==null){
//            Log.d("animation","not null");
//        }

        if (random == null) random = new Random();
        int steps = random.nextInt(10);
        steps += 20;
        for (int i = 0; i < steps; i++) {
            ArrayList<PuzzleBoard> abc = puzzleBoard.neighbours(false);
            int num = random.nextInt(abc.size());
            puzzleBoard = abc.get(num);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public void solve() {
        if (puzzleBoard == null) {
            Toast.makeText(getContext(), "Add a photo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (puzzleBoard.resolved()) {
            return;
        }
        PuzzleBoardComparator comparator = new PuzzleBoardComparator();
        PriorityQueue<PuzzleBoard> queue = new PriorityQueue<PuzzleBoard>(1, comparator);
        ArrayList<PuzzleBoard> answer = new ArrayList<PuzzleBoard>();
        queue.add(puzzleBoard);
//        int m=3;
        while (!queue.isEmpty()) {
//            m--;
            PuzzleBoard temp = queue.remove();
            if (temp.resolved()) {
                while (temp.previousBoard != null) {
                    answer.add(temp);
                    temp = temp.previousBoard;
                }
                Collections.reverse(answer);
                queue.removeAll(queue);
            } else {
                ArrayList<PuzzleBoard> neighbours = temp.neighbours(true);
                for (int i = 0; i < neighbours.size(); i++) {
                    PuzzleBoard tempB = neighbours.get(i), thisB;
                    thisB = tempB;
                    boolean a = false;
                    while ( tempB.previousBoard != null&&tempB.previousBoard.previousBoard != null ) {
                        tempB = tempB.previousBoard.previousBoard;
                        if (tempB.equals(thisB))
                            a = true;
                    }
                    if (!a)
                        queue.add(neighbours.get(i));

                }
            }
        }
        this.animation = answer;

        invalidate();

    }
}
