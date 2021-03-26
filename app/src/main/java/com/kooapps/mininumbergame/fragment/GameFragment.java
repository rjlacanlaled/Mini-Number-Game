package com.kooapps.mininumbergame.fragment;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.handler.GameHandler;
import com.kooapps.mininumbergame.model.main.GridGame;

import java.util.ArrayList;

public class GameFragment extends DialogFragment implements View.OnClickListener{

    public static final String TAG = "com.kooapps.mininumbergame.GAME_FRAGMENT";

    // UI OBJECTS
    private TableLayout mGameTableLayout;
    private View mRootView;
    private ArrayList<TextView> mCellTextViews;
    private ArrayList<TableRow> mTableRows;

    // DATA
    private GridGame mGridGame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_game, container, false);
        initGridGame();
        initTableLayout();
        initButtonList();
        updateUI();
        return mRootView;
    }

    public void initGridGame() {
        mGridGame = GameHandler.getInstance().getGridGame();
    }

    public void updateUI() {
        createGameGrid(mGridGame.getGrid().getRowCount(), mGridGame.getGrid().getColumnCount());
    }

    public void initTableLayout() {
        mGameTableLayout = (TableLayout)mRootView.findViewById(R.id.game_table_layout);
        mGameTableLayout.setBackgroundColor(getResources().getColor(R.color.light_forest_80_alpha));
    }

    public void initButtonList() {
        mCellTextViews = new ArrayList<>();
        mTableRows = new ArrayList<>();
    }

    // UI DRAWING METHODS

    public void createGameGrid(int row, int column) {
        int tagCounter = 0;
        for (int i = 0; i< row; i++) {
            TableRow tableRow;
            if (i < mTableRows.size()) {
                tableRow = mTableRows.get(i);
            } else {
                tableRow = new TableRow(getContext());
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
                mTableRows.add(tableRow);
                mGameTableLayout.addView(tableRow);
            }

            for (int j = 0; j < column; j++) {
                TextView textView;
                if (tagCounter < mCellTextViews.size()) {
                    textView = mCellTextViews.get(tagCounter);
                    textView.setText(mGridGame.getShuffledCellContents().get(tagCounter));
                } else {
                    textView = createTextView(tagCounter, mGridGame.getShuffledCellContents().get(tagCounter));
                    mCellTextViews.add(textView);
                    tableRow.addView(textView);
                }

                if (GameHandler.getInstance().getGridGame().getCellToTap() > 0) {
                    if (GameHandler.getInstance().getGridGame().getSelectedContents() != null) {
                        if (GameHandler.getInstance().getGridGame().getSelectedContents().contains(tagCounter)) {
                            textView.setEnabled(false);
                        }
                    }
                }
                tagCounter++;
            }
        }
    }

    public TextView createTextView(int tag, String text) {
        TextView newTextView = new TextView(getContext());
        newTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        newTextView.setEnabled(true);
        newTextView.setText(text);
        newTextView.setTag(tag);
        newTextView.setTextColor(getResources().getColor(R.color.dark_forest));
        if (mGridGame.getGrid().getRowCount() > 4 || GameHandler.getInstance().getGridGame().getGrid().getColumnCount() > 4) {
            newTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.0f);
        } else {
            newTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0f);
        }
        newTextView.setTypeface(null, Typeface.BOLD);
        newTextView.setGravity(Gravity.CENTER);
        newTextView.setOnClickListener(this);
        return newTextView;
    }


    // ON CLICK LISTENER

    @Override
    public void onClick(View view) {
        TextView textView = (TextView)view;
        GameHandler.getInstance().getGridGame().compareChosenValue(Integer.parseInt(textView.getTag().toString()));
        textView.setEnabled(false);
    }

    // UI HELPER METHODS

    public void disableCellButtons() {
        for (TextView cellTextView : mCellTextViews) {
            cellTextView.setEnabled(false);
        }
    }
}