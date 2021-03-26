package com.kooapps.mininumbergame.model.main;

import com.kooapps.mininumbergame.model.helper.GridGameMode;
import java.util.ArrayList;
import java.util.Collections;

public class NumberGridGame extends GridGame {

    public NumberGridGame(Grid grid, int baseScore, GridGameMode gameMode, long timeLimitInMiliseconds, boolean isTimed) {
        super(grid, baseScore, gameMode, timeLimitInMiliseconds, isTimed);
    }

    //region ABSTRACT_METHOD_IMPLEMENTATION

    public void generateCellContents() {
        if (this.getGameMode() == GridGameMode.GridGameModeAscending) {
            generateCellContentsAscending();
        } else if (this.getGameMode() == GridGameMode.GridGameModeDescending) {
            generateCellContentsDescending();
        } else {
            throw new EnumConstantNotPresentException(GridGameMode.class,
                    this.getGameMode().toString());
        }
    }

    public void randomizeCellContents() {
        if (this.getOriginalCellContents().size() > 0) {
            ArrayList<String> contentsToShuffle = new ArrayList<>(this.getOriginalCellContents().size());
            contentsToShuffle.addAll(this.getOriginalCellContents());
            Collections.shuffle(contentsToShuffle);
            this.setShuffledCellContents(contentsToShuffle);
        }
    }

    //endregion ABSTRACT_METHOD_IMPLEMENTATION

    //region GENERATE_RANDOM_CONTENTS_HELPER_METHODS

    public void generateCellContentsAscending() {
        ArrayList<String> contents = new ArrayList<>();
        int size = this.getGrid().getRowCount() * this.getGrid().getColumnCount();
        for (int i = 1; i <= size; i++) {
            contents.add(String.valueOf(i));
        }
        this.setOriginalCellContents(contents);
    }

    public void generateCellContentsDescending() {
        ArrayList<String> contents = new ArrayList<>();
        int size = this.getGrid().getRowCount() * this.getGrid().getColumnCount();
        for (int i = size; i >= 1; i--) {
            contents.add(String.valueOf(i));
        }
        this.setOriginalCellContents(contents);
    }

    //endregion GENERATE_RANDOM_CONTENTS_HELPER_METHODS

}
