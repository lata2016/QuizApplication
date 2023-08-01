package com.example.quizapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    TextView totalQuestionsTextView;
    TextView questionsTextView;
    Button answerA, answerB, answerC, answerD;
    Button submitButton;

    int score = 0;
    int totalQuestion = QuestionsAnswers.question.length;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";

    private CountDownTimer quizTimer;
    private long timeLeftInMillis;
    private TextView timerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialize the timer
        setContentView(R.layout.activity_main);
        timeLeftInMillis = 30000;
        startQuizTimer();

        timerTextView = findViewById(R.id.timerTextView);
        // initialize the questions
        totalQuestionsTextView = findViewById(R.id.total_questions);
        questionsTextView = findViewById(R.id.question);
        answerA = findViewById(R.id.answer_A);
        answerB = findViewById(R.id.answer_B);
        answerC = findViewById(R.id.answer_C);
        answerD = findViewById(R.id.answer_D);
        submitButton = findViewById(R.id.submit_button);

        //
        answerA.setOnClickListener(this);
        answerB.setOnClickListener(this);
        answerC.setOnClickListener(this);
        answerD.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        totalQuestionsTextView.setText("Total questions: " + totalQuestion);
        loadNewQuestion();

    }

    @Override
    public void onClick(View view) {

        answerA.setBackgroundColor(Color.WHITE);
        answerB.setBackgroundColor(Color.WHITE);
        answerC.setBackgroundColor(Color.WHITE);
        answerD.setBackgroundColor(Color.WHITE);

        Button clickedButton = (Button) view;
        if(clickedButton.getId()==R.id.submit_button){
            if(selectedAnswer.equals(QuestionsAnswers.correctAnswers[currentQuestionIndex])){
                score++;
            }
            currentQuestionIndex++;
            loadNewQuestion();

        }else{
            //choices button clicked
            selectedAnswer  = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.MAGENTA);
        }
    }

    void loadNewQuestion() {
        if (currentQuestionIndex == totalQuestion) {
            finishQuiz();
            return;
        }
        questionsTextView.setText(QuestionsAnswers.question[currentQuestionIndex]);
        answerA.setText(QuestionsAnswers.choices[currentQuestionIndex][0]);
        answerB.setText(QuestionsAnswers.choices[currentQuestionIndex][1]);
        answerC.setText(QuestionsAnswers.choices[currentQuestionIndex][2]);
        answerD.setText(QuestionsAnswers.choices[currentQuestionIndex][3]);

    }


    void finishQuiz() {
        String quizStatus = "";
        if (score > totalQuestion * 0.60) {
            quizStatus = "Passed";
        } else quizStatus = "Failed";
        new AlertDialog.Builder(this)
                .setTitle(quizStatus)
                .setMessage("Score is :"+score +"out of "+totalQuestion)
                .setPositiveButton("Restart",((dialogInterface, i) -> restartQuiz()))
                .setCancelable(false)
                .show();
    }

    void restartQuiz() {
        score = 0;
        currentQuestionIndex = 0;
        loadNewQuestion();
    }

   /* private void startQuizTimer() {
        quizTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }
            @Override
            public void onFinish() {

                // Time is up, perform any action you want when the timer finishes
                finishQuiz();
            }
        }.start();
    }

    */
   private void startQuizTimer() {
       quizTimer = new CountDownTimer(timeLeftInMillis, 1000) {
           @Override
           public void onTick(long millisUntilFinished) {
               timeLeftInMillis = millisUntilFinished;
               updateTimerText();
           }

           @Override
           public void onFinish() {
               // Time is up, perform any action you want when the timer finishes
               if (currentQuestionIndex < totalQuestion) {
                   // The quiz is not finished, show the finish time alert
                   finishQuizTimeUp();
               }
           }
       }.start();
   }

    private void updateTimerText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
        timerTextView.setText(timeFormatted);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (quizTimer != null) {
            quizTimer.cancel();
        }
    }
    void finishQuizTimeUp() {
        new AlertDialog.Builder(this)
                .setTitle("Time's Up!")
                .setMessage("You ran out of time!")
                .setPositiveButton("OK", (dialogInterface, i) -> {
                })
                .setCancelable(false)
                .show();
    }
}