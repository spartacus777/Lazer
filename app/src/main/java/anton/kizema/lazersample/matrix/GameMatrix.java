package anton.kizema.lazersample.matrix;

import android.util.Log;

public class GameMatrix {

    private static int GAME_FIELD_SIZE = 20;

    public int size = GAME_FIELD_SIZE;
    public int[][] elements;

    public GameMatrix() {
        elements = new int[GAME_FIELD_SIZE][GAME_FIELD_SIZE];

        for (int i = 0; i < GAME_FIELD_SIZE; i++) {
            for (int j = 0; j < GAME_FIELD_SIZE; j++) {
                elements[i][j] = 0;
            }
        }
    }

    public static GameMatrix createDumpMatrix(){
        GameMatrix gameMatrix = new GameMatrix();

        for (int i = 0; i < GAME_FIELD_SIZE; i++) {
            for (int j = 0; j < GAME_FIELD_SIZE; j++) {

                //create super dump square matrix
                if (i==2 || j==2 || i == 18 || j==18){
                    Log.d("ANT", "gameMatrix.elements[i][j] = 1");
                    gameMatrix.elements[i][j] = 1;
                }
            }
        }

        return gameMatrix;
    }

}
