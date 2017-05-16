
import java.util.ArrayList;
import javax.swing.event.*;
/**
 * Project Team : JYL
 * @author Jimmy Kwong
 * Model of Mancala Game
 */
public class Model {

    public static final int MAX_PITS = 14; //Max number of pits
    public static final int PLAYER_A_MANCALA = 6; // Pit index 7-12 will be Player A's side
    public static final int PLAYER_B_MANCALA = 13; //Pit index 0-5 will be Player B's side
    public static final int MAX_UNDOS = 3; //Max possible undos per player turn.

    public static enum GameState { //Current game status
        PLACING, ONGOING, ENDED
    };

    public static enum Player { //Player A and Player B
        A, B
    };

    private ArrayList<ChangeListener> listeners;
    private int[] pits; //current Mancala pit values
    private int[] previousPits; //previous Mancala pit values
    private Player currentPlayer;
    private GameState currentState; //Status of game
    private int PLAYER_A_UNDO;
    private int PLAYER_B_UNDO;
    private boolean freeTurn; //For if last stone lands on current player Mancala
    private boolean canUndo;
    
    /**
     * Constructor for the game, initializes required private variables.
     */
    public Model() {
        listeners = new ArrayList<ChangeListener>();
        pits = new int[MAX_PITS];
        freeTurn = false;
        PLAYER_A_UNDO = 0;
        PLAYER_B_UNDO = 0;
        previousPits = new int[MAX_PITS];
        currentPlayer = Player.A;
        currentState = GameState.PLACING;
        canUndo = false;
    }
    
    /**
     * Checks if player move is possible.
     * @param pitIndex the location of player mouse click
     * @return true if move is possible, false if not
     */
    public boolean canMakeMove(int pitIndex) {
        if (pitIndex < 0 || pitIndex > MAX_PITS
                || pitIndex == PLAYER_A_MANCALA
                || pitIndex == PLAYER_B_MANCALA) {
            return false;
        }
        if (currentState != GameState.ONGOING) {
            return false;
        }
        if (getPitPlayer(pitIndex) != currentPlayer) {
            return false;
        }
        if (pits[pitIndex] == 0) {
            return false;
        }
        return true;
    }
    /**
     * Move stones based on player chosen pit
     * @param pitIndex the pit player clicked
     */
    public void makeMove(int pitIndex) {
        this.save();
        canUndo = true;
        if (currentPlayer.equals(Player.A)) {
            PLAYER_B_UNDO = 0;
        } else {
            PLAYER_A_UNDO = 0;
        }
        int holdingStones = pits[pitIndex];
        pits[pitIndex] = 0;
        int currentLocation = pitIndex;
        while (holdingStones > 0) {
            currentLocation++;
            if (currentLocation >= MAX_PITS) {
                currentLocation = 0;
            }
            if ((currentLocation == PLAYER_A_MANCALA || currentLocation == PLAYER_B_MANCALA)
                    && getPitPlayer(currentLocation) != currentPlayer) {
                continue;
            }
            pits[currentLocation]++;
            holdingStones--;
        }

        //If last stone lands on current player's pit then the player gets another turn.
        if (getPitPlayer(currentLocation) == currentPlayer && playerMancala(currentLocation)) {
            freeTurn = true;
        } 
        
        //If stone landed on a empty pit on their respective side and opposite side has stones, capture and place into current player Mancala.
        else if (getPitPlayer(currentLocation) == currentPlayer
                && pits[currentLocation] == 1
                && pits[getOppositePit(currentLocation)] > 0) {
            
            pitCapture(currentLocation);
            freeTurn = false;
            nextTurn();
            
        } 
        
        //Else give turn to next player
        else {
            freeTurn = false;
            nextTurn();
        }

        // Check if game is over.
        if (isOver()) {
            gameOver();
            currentState = GameState.ENDED;
        }
        this.update(); //Update all listeners 
    }
    /**
     * It will take both the current pit and the opposite side pit and add it to current player Mancala
     * @param pitLocation the location where the last stone landed in
     */
    private void pitCapture(int pitLocation) {
        int capturedStones = pits[pitLocation] + pits[getOppositePit(pitLocation)];
        pits[pitLocation] = 0; //empty pit
        pits[getOppositePit(pitLocation)] = 0; //empty pit
        if (currentPlayer == Player.A) {
            pits[PLAYER_A_MANCALA] += capturedStones;
        } 
        else {
            pits[PLAYER_B_MANCALA] += capturedStones;
        }
    }
    /**
     * Equation to find opposite side pit
     * @param pitLocation the location where it needs opposite side pit
     * @return the opposite location.
     */
    private int getOppositePit(int pitLocation) {
        return 12 - pitLocation;
    }
    /**
     * Checks if current Pit is player's Mancala.
     * @param pitLocation current pit location
     * @return true if it is player's Mancala, false if not
     */
    public boolean playerMancala(int pitLocation) {
        if (currentPlayer == Player.A && pitLocation == PLAYER_A_MANCALA) {
            return true;
        } else if (currentPlayer == Player.B && pitLocation == PLAYER_B_MANCALA) {
            return true;
        }
        return false;
    }
    /**
     * Changes current player to the other player.
     */
    private void nextTurn() {
        if (currentPlayer == Player.A) {
            currentPlayer = Player.B;
        } else {
            currentPlayer = Player.A;
        }
    }
    /**
     * Checks if game is over, by checking if one side is fully empty
     * @return true if over, false if it is not
     */
    private boolean isOver() {
        int totalA = 0;
        int totalB = 0;
        for (int i = 0; i < MAX_PITS; ++i) {
            if (i != PLAYER_A_MANCALA && i != PLAYER_B_MANCALA) {
                if (getPitPlayer(i) == Player.A) {
                    totalA += pits[i];
                } else {
                    totalB += pits[i];
                }
            }
        }
        return (totalA == 0 || totalB == 0); //Checks if any side are empty
    }
    /**
     * If game is over, then this will add all remaining pits to their respective Mancala.
     */
    private void gameOver() {
        for (int i = 0; i < MAX_PITS; ++i) {
            if (i != PLAYER_A_MANCALA && i != PLAYER_B_MANCALA) {
                if (i == PLAYER_A_MANCALA) {
                    pits[PLAYER_A_MANCALA] += pits[i];
                    pits[i] = 0;
                } else {
                    pits[PLAYER_B_MANCALA] += pits[i];
                    pits[i] = 0;
                }
            }
        }
    }
    /**
     * Sets the state of the game
     * @param state the current state of game
     */
    public void setCurrentState(String state) {
        if (state.equals("PLACING")) {
            currentState = GameState.PLACING;
        } else if (state.equals("ONGOING")) {
            currentState = GameState.ONGOING;
        } else {
            currentState = GameState.ENDED;
        }
    }
    /**
     * Sets the starting number of stones
     * @param numOfStones the number of stones per pit, skipping Mancala
     */
    public void setNumStones(int numOfStones) {
        int pitLen = pits.length;
        for (int i = 0; i < pitLen; i++) {
            if (i != PLAYER_A_MANCALA && i != PLAYER_B_MANCALA) {
                pits[i] = numOfStones;
            }
        }
        this.update();
    }
    /**
     * Returns the current player
     * @return current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    /**
     * Returns the current pit's side
     * @param pitLocation current pit location
     * @return Player A side or Player B
     */
    private Player getPitPlayer(int pitLocation) {
        if (pitLocation >= 0 && pitLocation <= PLAYER_A_MANCALA) {
            return Player.A;
        }
        return Player.B; //If its not Player A then its B
    }
    /**
     * Returns game status for viewer and controller
     * @return current game status
     */
    public GameState getCurrentState() {
        return currentState;
    }
    /**
     * Returns the current pit values
     * @return pit values
     */
    public int[] getPits() {
        return pits;
    }
    /**
     * Returns current scores of selected player
     * @param player get score of selected player
     * @return score of player
     */
    public int getScore(Model.Player player) {
        if (player == Model.Player.A) {
            return pits[PLAYER_A_MANCALA];
        }
        if (player == Model.Player.B) {
            return pits[PLAYER_B_MANCALA];
        }
        return 0;
    }
    /**
     * Used for undoing turn.
     */
    public void undo() {
        
        if (!canUndo) { 
            return; //If cannot undo, end.
        }
        
        boolean undid = false; //Used to check if undo was made.

        if (currentPlayer == Player.A) { //Undo for player A
            if (freeTurn && PLAYER_A_UNDO < MAX_UNDOS) { //If previous turn was from free turn and if current undo has not reached max
                PLAYER_A_UNDO++; //Increment current player's undo
                undid = true; //undo was successful
            } 
            
            else if (!freeTurn && PLAYER_B_UNDO < MAX_UNDOS) { //If it is not then previous turn was other player's turn
                PLAYER_B_UNDO++; //Increment player B's undo because undo was called by Player B during Player A turn.
                currentPlayer = Player.B; //Give turn to player B
                undid = true; //undo was successful
            } 
        } 
        
        else if (currentPlayer == Player.B) { //Undo for player B
            if (freeTurn && PLAYER_B_UNDO < MAX_UNDOS) { //If previous turn was from free turn and if current undo has not reached max
                PLAYER_B_UNDO++; //Increment current player's undo
                undid = true; //undo was successful
            } else if (!freeTurn && PLAYER_A_UNDO < MAX_UNDOS) { //If it is not then previous turn was other player's turn
                PLAYER_A_UNDO++; //Increment player A's undo because undo was called by Player A.
                currentPlayer = Player.A; //Give turn to player A
                undid = true; //undo was successful
            }
        }

        //Reverse GameState if undo was clicked after game ended
        if (PLAYER_A_UNDO < MAX_UNDOS && currentState.equals(GameState.ENDED)
                || PLAYER_B_UNDO < MAX_UNDOS && currentState.equals(GameState.ENDED)) {
            currentState = GameState.ONGOING;
        }
        
        //If undo was used, cannot undo again until play has been made.
        if (undid) {
            canUndo = false; //Else set undo-able state to false
            pits = previousPits.clone(); //Set previous pit as current pit.
            this.update(); //Notify all listeners.
        }
    }
    /**
     * Save the previous pit in case of undo.
     */
    public void save() {
        previousPits = pits.clone();
    }
    /**
     * Attaches listener to model
     * @param cl ChangeListerner that will be added to model.
     */
    public void attach(ChangeListener cl) {
        listeners.add(cl);
    }
    /**
     * Notify and Updates all listeners attached to model.
     */
    private void update() {
        for (ChangeListener cl : listeners) {
            cl.stateChanged(new ChangeEvent(this));
        }
    }
}
