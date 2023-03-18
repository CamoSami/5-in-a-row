package board;

import static util.Constants.GameValues.*;

public class Cell {
	private int playerValue;
	private int minmaxValue;
	
	public Cell() {
		playerValue = VALUE_EMPTY;
		
		minmaxValue = -1;
	}

	public int getPlayerValue() {
		return playerValue;
	}

	public void setPlayerValue(int playerValue) {
		this.playerValue = playerValue;
	}

	public int getMinmaxValue() {
		return minmaxValue;
	}

	public void setMinmaxValue(int minmaxValue) {
		this.minmaxValue = minmaxValue;
	}
}
