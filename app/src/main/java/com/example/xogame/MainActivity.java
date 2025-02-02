package com.example.xogame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private boolean isXTurn = true;
    private Button[][] buttons = new Button[3][3];
    private TextView status;
    private boolean gameActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = findViewById(R.id.status);
        GridLayout grid = findViewById(R.id.grid);
        Button resetButton = findViewById(R.id.resetButton);

        // Initialize buttons and set click listeners
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);

                final int row = i, col = j;
                buttons[i][j].setOnClickListener(v -> onButtonClick(row, col));
            }
        }

        resetButton.setOnClickListener(v -> resetGame());
    }

    private void onButtonClick(int row, int col) {
        if (!gameActive || !buttons[row][col].getText().toString().isEmpty()) return;

        buttons[row][col].setText(isXTurn ? "X" : "O");
        buttons[row][col].setTextColor(isXTurn ? 0xFF00EFFF : 0xFFFF0099);

        if (checkWinner()) {
            showWinPopup("Player " + (isXTurn ? "X" : "O") + " Wins!");
            gameActive = false;
        } else if (isBoardFull()) {
            showWinPopup("It's a Draw!");
            gameActive = false;
        } else {
            isXTurn = !isXTurn;
            status.setText("Player " + (isXTurn ? "X" : "O") + "'s Turn");
        }
    }

    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(buttons[i][0], buttons[i][1], buttons[i][2]) ||
                    checkRowCol(buttons[0][i], buttons[1][i], buttons[2][i])) {
                return true;
            }
        }
        return checkRowCol(buttons[0][0], buttons[1][1], buttons[2][2]) ||
                checkRowCol(buttons[0][2], buttons[1][1], buttons[2][0]);
    }

    private boolean checkRowCol(Button b1, Button b2, Button b3) {
        return !b1.getText().toString().isEmpty() &&
                b1.getText().toString().equals(b2.getText().toString()) &&
                b1.getText().toString().equals(b3.getText().toString());
    }

    private boolean isBoardFull() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                if (button.getText().toString().isEmpty()) return false;
            }
        }
        return true;
    }

    private void showWinPopup(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(message)
                .setPositiveButton("Play Again", (dialog, which) -> resetGame())
                .setCancelable(false)
                .show();
    }

    private void resetGame() {
        gameActive = true;
        isXTurn = true;
        status.setText("Player X's Turn");
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setText("");
            }
        }
    }
}
