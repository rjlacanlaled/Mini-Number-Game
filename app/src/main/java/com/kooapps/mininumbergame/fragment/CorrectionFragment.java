package com.kooapps.mininumbergame.fragment;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.kooapps.mininumbergame.R;
import com.kooapps.mininumbergame.handler.GameHandler;

public class CorrectionFragment extends DialogFragment {

    public static final String TAG = "com.kooapps.mininumbergame.CORRECTION_FRAGMENT";

    // UI OBJECTS
    private TableLayout mCorrectionTableLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_correction, container, false);
        mCorrectionTableLayout = (TableLayout) rootView.findViewById(R.id.correction_table_layout);
        mCorrectionTableLayout.setBackgroundColor(getResources().getColor(R.color.white));
        initUI();
        return rootView;
    }

    // INITIAL SETUP

    public void initUI() {
        createCorrectionGrid(GameHandler.getInstance().getGridGame().getGrid().getRowCount(),
                GameHandler.getInstance().getGridGame().getGrid().getColumnCount());
    }

    // UI DRAWING METHODS

    public void createCorrectionGrid(int row, int column) {
        int tagCounter = 0;
        for (int i = 0; i < row; i++) {
            TableRow newRow = new TableRow(getContext());
            newRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            for (int j = 0; j < column; j++) {
                String content = GameHandler.getInstance().getGridGame().getShuffledCellContents().get(tagCounter);
                boolean isCorrectCell = content.equals(GameHandler.getInstance().getGridGame().getOriginalCellContents().get(
                        GameHandler.getInstance().getGridGame().getCellToTap()));
                TextView newTextView = createCellTextView(content, isCorrectCell);
                newRow.addView(newTextView);
                tagCounter++;
            }
            mCorrectionTableLayout.addView(newRow);
        }
    }

    public TextView createCellTextView(String text, boolean isHighlighted) {
        TextView newTextView = new TextView(getContext());
        newTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        newTextView.setText(text);
        newTextView.setGravity(Gravity.CENTER);
        newTextView.setTypeface(null, Typeface.BOLD);
        newTextView.setBackgroundColor(getResources().getColor(R.color.light_forest_80_alpha));
        if (GameHandler.getInstance().getGridGame().getGrid().getRowCount() > 4 ||
                GameHandler.getInstance().getGridGame().getGrid().getColumnCount() > 4) {
            newTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.0f);
        } else {
            newTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0f);
        }
        if (isHighlighted) {
            newTextView.setBackgroundColor(getResources().getColor(R.color.light_forest));
        }
        return newTextView;
    }
}