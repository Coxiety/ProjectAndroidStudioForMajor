package com.example.learningapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonImporter {
    
    public static void importQuestionsFromAssets(Context context, SQLiteDatabase db, String fileName) {
        try {
            String jsonString = loadJsonFromAssets(context, fileName);
            JSONArray examSets = new JSONArray(jsonString);
            
            for (int i = 0; i < examSets.length(); i++) {
                JSONObject examSetJson = examSets.getJSONObject(i);
                
                JSONObject examSetInfo = examSetJson.getJSONObject("examSet");
                String name = examSetInfo.getString("name");
                String description = examSetInfo.getString("description");
                boolean isOfficial = examSetInfo.getBoolean("isOfficial");
                String category = examSetInfo.getString("category");
                
                JSONArray questions = examSetJson.getJSONArray("questions");
                
                ContentValues examSetValues = new ContentValues();
                examSetValues.put("name", name);
                examSetValues.put("description", description);
                examSetValues.put("question_count", questions.length());
                examSetValues.put("is_official", isOfficial ? 1 : 0);
                examSetValues.put("category", category);
                
                long examSetId = db.insert("exam_sets", null, examSetValues);
                
                for (int j = 0; j < questions.length(); j++) {
                    JSONObject question = questions.getJSONObject(j);
                    
                    ContentValues questionValues = new ContentValues();
                    questionValues.put("question_text", question.getString("questionText"));
                    questionValues.put("option_a", question.optString("optionA", ""));
                    questionValues.put("option_b", question.optString("optionB", ""));
                    questionValues.put("option_c", question.optString("optionC", null));
                    questionValues.put("option_d", question.optString("optionD", null));
                    questionValues.put("correct_answer", question.getString("correctAnswer"));
                    questionValues.put("explanation", question.optString("explanation", null));
                    questionValues.put("image_path", question.optString("imagePath", null));
                    questionValues.put("exam_set_id", examSetId);
                    
                    db.insert("questions", null, questionValues);
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void importFlashcardsFromAssets(Context context, SQLiteDatabase db, String fileName) {
        try {
            String jsonString = loadJsonFromAssets(context, fileName);
            JSONArray examSets = new JSONArray(jsonString);
            
            int totalFlashcardsWithImages = 0;
            
            for (int i = 0; i < examSets.length(); i++) {
                JSONObject examSetJson = examSets.getJSONObject(i);
                
                JSONObject examSetInfo = examSetJson.getJSONObject("examSet");
                String topicName = examSetInfo.getString("name");
                String topicDescription = examSetInfo.getString("description");
                
                JSONArray questions = examSetJson.getJSONArray("questions");
                
                ContentValues topicValues = new ContentValues();
                topicValues.put("name", topicName);
                topicValues.put("description", topicDescription);
                topicValues.put("total_cards", questions.length());
                topicValues.put("learned_cards", 0);
                
                long topicId = db.insert("flashcard_topics", null, topicValues);
                
                for (int j = 0; j < questions.length(); j++) {
                    JSONObject question = questions.getJSONObject(j);
                    
                    String questionText = question.getString("questionText");
                    String correctAnswer = question.getString("correctAnswer");
                    String optionA = question.optString("optionA", "");
                    String optionB = question.optString("optionB", "");
                    String optionC = question.optString("optionC", "");
                    String optionD = question.optString("optionD", "");
                    String explanation = question.optString("explanation", null);
                    String imagePath = !question.isNull("imagePath") ? question.getString("imagePath") : null;
                    
                    if (imagePath != null && !imagePath.isEmpty()) {
                        totalFlashcardsWithImages++;
                    }
                    
                    String correctAnswerText = "";
                    switch (correctAnswer) {
                        case "A":
                            correctAnswerText = optionA;
                            break;
                        case "B":
                            correctAnswerText = optionB;
                            break;
                        case "C":
                            correctAnswerText = optionC;
                            break;
                        case "D":
                            correctAnswerText = optionD;
                            break;
                    }
                    
                    ContentValues flashcardValues = new ContentValues();
                    flashcardValues.put("front", questionText);
                    flashcardValues.put("back", correctAnswer + ". " + correctAnswerText);
                    flashcardValues.put("explanation", explanation);
                    flashcardValues.put("image_path", imagePath);
                    flashcardValues.put("topic_id", topicId);
                    flashcardValues.put("is_learned", 0);
                    flashcardValues.put("review_count", 0);
                    
                    db.insert("flashcards", null, flashcardValues);
                }
            }
            
            // Create special topic for flashcards with images only
            createFlashcardsWithImagesTopic(context, db, fileName, totalFlashcardsWithImages);
            
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void createFlashcardsWithImagesTopic(Context context, SQLiteDatabase db, String fileName, int totalWithImages) {
        try {
            String jsonString = loadJsonFromAssets(context, fileName);
            JSONArray examSets = new JSONArray(jsonString);
            
            ContentValues topicValues = new ContentValues();
            topicValues.put("name", "Câu hỏi có hình");
            topicValues.put("description", "Chỉ những câu hỏi có hình ảnh minh họa");
            topicValues.put("total_cards", totalWithImages);
            topicValues.put("learned_cards", 0);
            topicValues.put("is_image_only", 1);
            
            long imageTopicId = db.insert("flashcard_topics", null, topicValues);
            
            for (int i = 0; i < examSets.length(); i++) {
                JSONObject examSetJson = examSets.getJSONObject(i);
                JSONArray questions = examSetJson.getJSONArray("questions");
                
                for (int j = 0; j < questions.length(); j++) {
                    JSONObject question = questions.getJSONObject(j);
                    
                    String imagePath = !question.isNull("imagePath") ? question.getString("imagePath") : null;
                    
                    if (imagePath != null && !imagePath.isEmpty()) {
                        String questionText = question.getString("questionText");
                        String correctAnswer = question.getString("correctAnswer");
                        String optionA = question.optString("optionA", "");
                        String optionB = question.optString("optionB", "");
                        String optionC = question.optString("optionC", "");
                        String optionD = question.optString("optionD", "");
                        String explanation = question.optString("explanation", null);
                        
                        String correctAnswerText = "";
                        switch (correctAnswer) {
                            case "A":
                                correctAnswerText = optionA;
                                break;
                            case "B":
                                correctAnswerText = optionB;
                                break;
                            case "C":
                                correctAnswerText = optionC;
                                break;
                            case "D":
                                correctAnswerText = optionD;
                                break;
                        }
                        
                        ContentValues flashcardValues = new ContentValues();
                        flashcardValues.put("front", questionText);
                        flashcardValues.put("back", correctAnswer + ". " + correctAnswerText);
                        flashcardValues.put("explanation", explanation);
                        flashcardValues.put("image_path", imagePath);
                        flashcardValues.put("topic_id", imageTopicId);
                        flashcardValues.put("is_learned", 0);
                        flashcardValues.put("review_count", 0);
                        
                        db.insert("flashcards", null, flashcardValues);
                    }
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String loadJsonFromAssets(Context context, String fileName) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        reader.close();
        is.close();
        return sb.toString();
    }
}


