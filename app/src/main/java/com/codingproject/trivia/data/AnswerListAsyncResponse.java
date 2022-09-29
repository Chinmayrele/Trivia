package com.codingproject.trivia.data;

import com.codingproject.trivia.models.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}
