package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };
    private ArrayList<PuzzleTile> tiles;
    int steps=0;
    PuzzleBoard previousBoard=null;

    int priority=0;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        Bitmap[][] puzzleBitmap = new Bitmap[3][3];
        tiles = new ArrayList<PuzzleTile>();

        for (int i = 0; i < puzzleBitmap.length; i++) {
            for (int j = 0; j < puzzleBitmap[i].length; j++) {
                puzzleBitmap[i][j] = Bitmap.createBitmap(bitmap, bitmap.getWidth() * j / NUM_TILES,
                        bitmap.getHeight() * i / NUM_TILES, bitmap.getWidth() / NUM_TILES, bitmap.getWidth() / NUM_TILES);
                puzzleBitmap[i][j]=Bitmap.createScaledBitmap(puzzleBitmap[i][j],parentWidth/NUM_TILES,parentWidth/NUM_TILES,true );
                if (puzzleBitmap[i][j] == null) {
                    Log.d("null", "the bitmap is null");
                }
                PuzzleTile temp;
                if (i == NUM_TILES - 1 && j == NUM_TILES - 1) {
                    puzzleBitmap[i][j] = null;
                    temp = null;
                } else {
                    temp = new PuzzleTile(puzzleBitmap[i][j], XYtoIndex(j,i));
                    Log.d("working", "for " + i + " " + j);
                }
                tiles.add(temp);


            }
        }

    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        this.previousBoard=otherBoard;
        this.steps=otherBoard.steps+1;
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }
    public ArrayList<PuzzleBoard> neighbours(boolean setPrevious) {
        ArrayList<PuzzleBoard> neighbours=new ArrayList<PuzzleBoard>();
        int nullNo=0;
        for (int i=0;i<tiles.size();i++){
            if (tiles.get(i)==null){
                nullNo=i;
            }
        }
        Log.d("Null Number",nullNo+" is null");
        int x=nullNo%NUM_TILES,y=nullNo/NUM_TILES;
        for (int i = 0; i < NEIGHBOUR_COORDS.length; i++) {

                int tempX=x+NEIGHBOUR_COORDS[i][0],tempY=y+NEIGHBOUR_COORDS[i][1];
                if(tempX<NUM_TILES&&tempX>=0&&tempY<NUM_TILES&&tempY>=0){
//                    PuzzleTile tempTile=tiles.get(tempX+tempY*NUM_TILES);
                    PuzzleBoard tempBoard=new PuzzleBoard(this);
                    if(!setPrevious)
                        tempBoard.previousBoard=null;
                    tempBoard.swapTiles(nullNo,XYtoIndex(tempX,tempY));
                    Log.d("switching for neighbour",i+" "+XYtoIndex(tempX,tempY));
                    neighbours.add(tempBoard);
                }
        }

        return neighbours;
    }

    public int priority() {
        if(priority==0) {
            Log.d("finding priority", "priority");
            for (int i = 0; i < tiles.size(); i++) {
                PuzzleTile temp = tiles.get(i);
                int x = 2, y = 2;
                if (temp != null) {
                    x = temp.getNumber() % NUM_TILES;
                    y = temp.getNumber() / NUM_TILES;
                }
                int distance = Math.abs(x - i % NUM_TILES) + Math.abs(y - i / NUM_TILES);
                Log.d("distance " + distance, x + " " + y + " " + " i is " + i);
                priority += distance;
            }
            priority = priority * 10 + steps;
            Log.d("priority", priority + "");
        }
        return priority;

    }

}
