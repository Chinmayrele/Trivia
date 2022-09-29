package com.codingproject.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.codingproject.trivia.controllers.AppController;
import com.codingproject.trivia.models.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    JSONArray arr;
    String apiUrl = "https://opentdb.com/api.php?amount=50&type=boolean";

    ArrayList<Question> questionArrayList = new ArrayList<>();

    public List<Question> getQuestions(final AnswerListAsyncResponse callback) {

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        arr = (response.getJSONArray("results"));
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = (JSONObject) arr.get(i);
                            String que = obj.getString("question");
                            boolean isTrueAnswer = obj.getBoolean("correct_answer");

                            Question question = new Question(que, isTrueAnswer);
                            // ADD QUESTION TO THE ARRAY LIST
                            questionArrayList.add(question);

                            Log.d("ARR", "ARRAY IS: " + que);
                            Log.d("ARR", "TRUE/FALSE IS: " + isTrueAnswer);
                        }
                        if(callback != null) callback.processFinished(questionArrayList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    Log.d("TAG", "onCreate: " + response.toString());
                }, error -> Log.d("Error", "ERROR IS: " + error.toString()));

        AppController.getInstance().addToRequestQueue(jsonObjRequest);

        return questionArrayList;
    }
}
