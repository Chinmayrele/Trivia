package com.codingproject.trivia;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.codingproject.trivia.data.Repository;
import com.codingproject.trivia.databinding.ActivityMainBinding;
import com.codingproject.trivia.models.Question;
import com.codingproject.trivia.models.Score;
import com.codingproject.trivia.util.SharedPrefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Question> questionList;
    private ActivityMainBinding binding;
    private int currentQueIndex = 0;
    private int scoreCounter = 0;
    private Score score;
    private SharedPrefs sharedPrefs;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        score = new Score();
        sharedPrefs = new SharedPrefs(MainActivity.this);
        binding.scoreText.setText(MessageFormat.format("Current Score: {0}",
                String.valueOf(score.getScore())));
        Log.d("Prefs", "onCreate: "+ sharedPrefs.getHighestScore());

        binding.highestScoreText.setText(MessageFormat.format("Highest Score: {0}",
                String.valueOf(sharedPrefs.getHighestScore())));
        questionList = new Repository().getQuestions(questionArrayList -> {
            binding.questionTextView.setText(questionArrayList.get(currentQueIndex).getQuestion());
            updateCounter(questionArrayList);
        });

        binding.buttonNext.setOnClickListener(view -> {
            currentQueIndex = (currentQueIndex + 1) % questionList.size();
            updateQuestion();
//            sharedPrefs.saveHighestScore(scoreCounter);
        });

        binding.buttonTrue.setOnClickListener(view -> {
            checkAnswer(true);
            updateQuestion();
        });

        binding.buttonFalse.setOnClickListener(view -> {
            checkAnswer(false);
            updateQuestion();
        });


    }

    private void checkAnswer(boolean userAnswerChosen) {
        boolean correctAnswer = questionList.get(currentQueIndex).isAnswerTrue();
        int snackMessageId;
        if (correctAnswer == userAnswerChosen) {
            snackMessageId = R.string.correct_answer;
            fadeAnimation();
            addScores();
        } else {
            deductScores();
            snackMessageId = R.string.incorrect_ans;
            shakeAnimation();
        }
        Toast.makeText(this, snackMessageId, Toast.LENGTH_SHORT).show();
    }

    private void updateCounter(ArrayList<Question> questionArrayList) {
        binding.queTextOutOff.setText(String.format(getString(R.string.text_formatter), currentQueIndex+1, questionArrayList.size()));
    }

    private void updateQuestion() {
        String question = questionList.get(currentQueIndex).getQuestion();
        binding.questionTextView.setText(question);
        updateCounter((ArrayList<Question>) questionList);

    }

    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(Animation.REVERSE);
        alphaAnimation.setRepeatCount(1);

        binding.questionTextView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        binding.queCardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void deductScores() {
        scoreCounter -= 10;
        if(scoreCounter < 0) {
            scoreCounter = 0;
            score.setScore(scoreCounter);

        } else {
            score.setScore(scoreCounter);
            binding.scoreText.setText(MessageFormat.format("Current Score: {0}",
                    String.valueOf(score.getScore())));
        }

    }

    private void addScores() {
        scoreCounter += 10;
        score.setScore(scoreCounter);
        binding.scoreText.setText(MessageFormat.format("Current Score: {0}",
                String.valueOf(score.getScore())));
        Log.d("ADD Score", "addScores: "+ score.getScore());
    }

    @Override
    protected void onPause() {
        sharedPrefs.saveHighestScore(score.getScore());
        super.onPause();
    }
}