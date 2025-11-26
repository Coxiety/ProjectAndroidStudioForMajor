package com.example.learningapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonImporter {
    
    public static void importQuestionsFromAssets(Context context, SQLiteDatabase db, String fileName) {
        try {
            Log.d("JsonImporter", "Starting import questions from: " + fileName);
            String jsonString = loadJsonFromAssets(context, fileName);
            JSONArray examSets = new JSONArray(jsonString);
            Log.d("JsonImporter", "Found " + examSets.length() + " exam sets");
            
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
                Log.d("JsonImporter", "Inserted exam set: " + name + " with ID: " + examSetId + ", questions count: " + questions.length());
                
                for (int j = 0; j < questions.length(); j++) {
                    JSONObject question = questions.getJSONObject(j);
                    
                    String optA = question.optString("optionA", "");
                    String optB = question.optString("optionB", "");
                    String optC = question.optString("optionC", null);
                    String optD = question.optString("optionD", null);
                    
                    // Fix merged C-D options (e.g. "C. content  4. content")
                    if ((optD == null || optD.trim().isEmpty() || optD.equals("null")) && optC != null) {
                        String[] parts = splitMergedOption(optC, "4", "D");
                        if (parts != null) {
                            optC = parts[0];
                            optD = parts[1];
                        }
                    }

                    ContentValues questionValues = new ContentValues();
                    questionValues.put("question_text", question.getString("questionText"));
                    questionValues.put("option_a", cleanOptionText(optA));
                    questionValues.put("option_b", cleanOptionText(optB));
                    questionValues.put("option_c", cleanOptionText(optC));
                    questionValues.put("option_d", cleanOptionText(optD));
                    
                    String correctAnswer = question.getString("correctAnswer");
                    if ("4".equals(correctAnswer)) {
                        correctAnswer = "D";
                    }
                    questionValues.put("correct_answer", correctAnswer);
                    
                    questionValues.put("image_path", question.optString("imagePath", null));
                    questionValues.put("is_liet", question.optBoolean("isLiet", false) ? 1 : 0);
                    questionValues.put("exam_set_id", examSetId);
                    
                    db.insert("questions", null, questionValues);
                }
            }
            Log.d("JsonImporter", "Successfully imported questions");
        } catch (JSONException | IOException e) {
            Log.e("JsonImporter", "Error importing questions: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void importFlashcardsFromAssets(Context context, SQLiteDatabase db, String fileName) {
        try {
            Log.d("JsonImporter", "Starting import flashcards from: " + fileName);
            String jsonString = loadJsonFromAssets(context, fileName);
            JSONArray examSets = new JSONArray(jsonString);
            Log.d("JsonImporter", "Found " + examSets.length() + " exam sets for flashcards");
            
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
                Log.d("JsonImporter", "Inserted flashcard topic: " + topicName + " with ID: " + topicId + ", total cards: " + questions.length());
                
                for (int j = 0; j < questions.length(); j++) {
                    JSONObject question = questions.getJSONObject(j);
                    
                    String questionText = question.getString("questionText");
                    String correctAnswer = question.getString("correctAnswer");
                    
                    String optA = question.optString("optionA", "");
                    String optB = question.optString("optionB", "");
                    String optC = question.optString("optionC", "");
                    String optD = question.optString("optionD", "");
                    
                    // Fix merged C-D options
                    if ((optD == null || optD.trim().isEmpty() || optD.equals("null")) && optC != null && !optC.isEmpty()) {
                        String[] parts = splitMergedOption(optC, "4", "D");
                        if (parts != null) {
                            optC = parts[0];
                            optD = parts[1];
                        }
                    }

                    String optionA = cleanOptionText(optA);
                    String optionB = cleanOptionText(optB);
                    String optionC = cleanOptionText(optC);
                    String optionD = cleanOptionText(optD);
                    
                    String imagePath = !question.isNull("imagePath") ? question.getString("imagePath").trim() : null;
                    
                    if (imagePath != null && !imagePath.isEmpty()) {
                        totalFlashcardsWithImages++;
                    }
                    
                    if ("4".equals(correctAnswer)) {
                        correctAnswer = "D";
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
                    
                    // New format: Front = Question + Options, Back = Correct Answer
                    String frontContent = formatFlashcardFront(questionText, optionA, optionB, optionC, optionD);
                    String backContent = correctAnswer + ". " + correctAnswerText;
                    
                    ContentValues flashcardValues = new ContentValues();
                    flashcardValues.put("front", frontContent);
                    flashcardValues.put("back", backContent);
                    flashcardValues.put("image_path", imagePath);
                    flashcardValues.put("topic_id", topicId);
                    flashcardValues.put("is_learned", 0);
                    flashcardValues.put("review_count", 0);
                    
                    db.insert("flashcards", null, flashcardValues);
                }
            }
            
            // Create special topic for flashcards with images only
            createFlashcardsWithImagesTopic(context, db, fileName, totalFlashcardsWithImages);
            
            Log.d("JsonImporter", "Successfully imported flashcards. Total with images: " + totalFlashcardsWithImages);
        } catch (JSONException | IOException e) {
            Log.e("JsonImporter", "Error importing flashcards: " + e.getMessage());
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
                    
                    String imagePath = !question.isNull("imagePath") ? question.getString("imagePath").trim() : null;
                    
                    if (imagePath != null && !imagePath.isEmpty()) {
                        String questionText = question.getString("questionText");
                        String correctAnswer = question.getString("correctAnswer");
                        
                        String optA = question.optString("optionA", "");
                        String optB = question.optString("optionB", "");
                        String optC = question.optString("optionC", "");
                        String optD = question.optString("optionD", "");
                        
                        // Fix merged C-D options
                        if ((optD == null || optD.trim().isEmpty() || optD.equals("null")) && optC != null && !optC.isEmpty()) {
                            String[] parts = splitMergedOption(optC, "4", "D");
                            if (parts != null) {
                                optC = parts[0];
                                optD = parts[1];
                            }
                        }
                        
                        String optionA = cleanOptionText(optA);
                        String optionB = cleanOptionText(optB);
                        String optionC = cleanOptionText(optC);
                        String optionD = cleanOptionText(optD);
                        
                        if ("4".equals(correctAnswer)) {
                            correctAnswer = "D";
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
                        
                        // New format: Front = Question + Options, Back = Correct Answer
                        String frontContent = formatFlashcardFront(questionText, optionA, optionB, optionC, optionD);
                        String backContent = correctAnswer + ". " + correctAnswerText;
                        
                        ContentValues flashcardValues = new ContentValues();
                        flashcardValues.put("front", frontContent);
                        flashcardValues.put("back", backContent);
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
    
    // Helper method to split text like "Some content 4. Some other content"
    private static String[] splitMergedOption(String text, String nextNumMarker, String nextLetterMarker) {
        // Regex to find space followed by marker (e.g., " 4.", " D.", " 4)", " D)") and space/end
        // Pattern matches: space + (marker) + (dot or paren) + space*
        String pattern = "\\s+(" + nextNumMarker + "|" + nextLetterMarker + ")[.)]\\s*";
        
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        
        if (m.find()) {
            int start = m.start();
            int end = m.end();
            
            String part1 = text.substring(0, start).trim();
            String part2 = text.substring(end).trim();
            
            return new String[]{part1, part2};
        }
        return null;
    }
    
    private static String formatFlashcardFront(String questionText, String optionA, String optionB, String optionC, String optionD) {
        StringBuilder sb = new StringBuilder();
        sb.append(questionText).append("\n\n");
        
        if (optionA != null && !optionA.isEmpty() && !optionA.equals("null") && !optionA.equals("4")) {
            sb.append("A. ").append(optionA).append("\n");
        }
        if (optionB != null && !optionB.isEmpty() && !optionB.equals("null") && !optionB.equals("4")) {
            sb.append("B. ").append(optionB).append("\n");
        }
        if (optionC != null && !optionC.isEmpty() && !optionC.equals("null") && !optionC.equals("4")) {
            sb.append("C. ").append(optionC).append("\n");
        }
        if (optionD != null && !optionD.isEmpty() && !optionD.equals("null") && !optionD.equals("4")) {
            sb.append("D. ").append(optionD).append("\n");
        }
        
        return sb.toString().trim();
    }
    
    private static String cleanOptionText(String text) {
        if (text == null) return null;
        if (text.isEmpty() || text.equals("null")) return text;
        
        // Remove prefixes like "A.", "B ", "1.", "4."
        // Matches A-D or 1-4 followed by dot or space at the start of the string
        String cleaned = text.trim().replaceAll("^([A-Da-d]|[1-4])[.\\s)]\\s*", "");
        return cleaned;
    }
    
    private static String loadJsonFromAssets(Context context, String fileName) throws IOException {
        Log.d("JsonImporter", "loadJsonFromAssets - fileName: " + fileName);
        InputStream is = context.getAssets().open(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        reader.close();
        is.close();
        String jsonString = sb.toString();
        Log.d("JsonImporter", "Loaded JSON length: " + jsonString.length() + " characters");
        return jsonString;
    }
}
