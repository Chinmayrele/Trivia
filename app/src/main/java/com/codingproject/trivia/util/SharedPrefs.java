package com.codingproject.trivia.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    public static final String HIGHEST_SCORE = "highest_score";
    private SharedPreferences preferences;

    public SharedPrefs(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    public  void saveHighestScore(int score) {
        int currentScore = score;
        int lastScore = preferences.getInt(HIGHEST_SCORE, 0);

        if(currentScore > lastScore) {
            //WE HAVE A NEW HIGH SCORE
            preferences.edit().putInt(HIGHEST_SCORE, currentScore).apply();
        }
    }

    public int getHighestScore() {
        return preferences.getInt(HIGHEST_SCORE, 0);
    }
}
