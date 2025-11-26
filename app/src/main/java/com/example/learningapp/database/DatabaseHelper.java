package com.example.learningapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.learningapp.models.ExamHistory;
import com.example.learningapp.models.ExamSet;
import com.example.learningapp.models.Flashcard;
import com.example.learningapp.models.FlashcardTopic;
import com.example.learningapp.models.Question;
import com.example.learningapp.utils.JsonImporter;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "learning_app.db";
    private static final int DATABASE_VERSION = 10;  // Incremented version to force DB recreation with front options/back answer format
    
    private static final String TABLE_FLASHCARD_TOPICS = "flashcard_topics";
    private static final String TABLE_FLASHCARDS = "flashcards";
    private static final String TABLE_EXAM_SETS = "exam_sets";
    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_EXAM_HISTORY = "exam_history";
    
    private Context context;
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createFlashcardTopicsTable = "CREATE TABLE " + TABLE_FLASHCARD_TOPICS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "total_cards INTEGER DEFAULT 0, " +
                "learned_cards INTEGER DEFAULT 0, " +
                "is_image_only INTEGER DEFAULT 0)";
        
        String createFlashcardsTable = "CREATE TABLE " + TABLE_FLASHCARDS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "front TEXT NOT NULL, " +
                "back TEXT NOT NULL, " +
                "explanation TEXT, " +
                "image_path TEXT, " +
                "topic_id INTEGER, " +
                "is_learned INTEGER DEFAULT 0, " +
                "review_count INTEGER DEFAULT 0, " +
                "last_review_time INTEGER, " +
                "FOREIGN KEY(topic_id) REFERENCES " + TABLE_FLASHCARD_TOPICS + "(id))";
        
        String createExamSetsTable = "CREATE TABLE " + TABLE_EXAM_SETS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "question_count INTEGER DEFAULT 0, " +
                "is_official INTEGER DEFAULT 0, " +
                "category TEXT)";
        
        String createQuestionsTable = "CREATE TABLE " + TABLE_QUESTIONS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "question_text TEXT NOT NULL, " +
                "option_a TEXT NOT NULL, " +
                "option_b TEXT NOT NULL, " +
                "option_c TEXT, " +
                "option_d TEXT, " +
                "correct_answer TEXT NOT NULL, " +
                "explanation TEXT, " +
                "image_path TEXT, " +
                "exam_set_id INTEGER, " +
                "FOREIGN KEY(exam_set_id) REFERENCES " + TABLE_EXAM_SETS + "(id))";
        
        String createExamHistoryTable = "CREATE TABLE " + TABLE_EXAM_HISTORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "exam_set_id INTEGER, " +
                "exam_name TEXT, " +
                "total_questions INTEGER, " +
                "correct_answers INTEGER, " +
                "wrong_answers INTEGER, " +
                "score INTEGER, " +
                "is_passed INTEGER, " +
                "test_date INTEGER, " +
                "duration_minutes INTEGER, " +
                "answers_json TEXT, " +
                "FOREIGN KEY(exam_set_id) REFERENCES " + TABLE_EXAM_SETS + "(id))";
        
        db.execSQL(createFlashcardTopicsTable);
        db.execSQL(createFlashcardsTable);
        db.execSQL(createExamSetsTable);
        db.execSQL(createQuestionsTable);
        db.execSQL(createExamHistoryTable);
        
        insertSampleData(db);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAM_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAM_SETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLASHCARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLASHCARD_TOPICS);
        onCreate(db);
    }
    
    private void insertSampleData(SQLiteDatabase db) {
        // Import questions from JSON file for Practice Tests
        try {
            JsonImporter.importQuestionsFromAssets(context, db, "A1_250Q_with_images.json");
        } catch (Exception e) {
            e.printStackTrace();
            // If JSON import fails, insert sample questions as fallback
            ContentValues values = new ContentValues();
            values.put("name", "Đề thi chính thức số 1");
            values.put("description", "Đề thi bằng lái B2");
            values.put("question_count", 3);
            values.put("is_official", 1);
            values.put("category", "B2");
            long examSetId = db.insert(TABLE_EXAM_SETS, null, values);
            
            insertSampleQuestion(db, examSetId, 
                "Khái niệm 'người điều khiển giao thông' được hiểu như thế nào?",
                "Là người điều khiển phương tiện tham gia giao thông đường bộ",
                "Là cảnh sát giao thông, người được giao nhiệm vụ hướng dẫn giao thông",
                "Là người tham gia giao thông tại các ngã ba, ngã tư có tín hiệu đèn",
                null, "B", "Người điều khiển giao thông là cảnh sát hoặc người được giao nhiệm vụ");
            
            insertSampleQuestion(db, examSetId,
                "Người lái xe phải làm gì khi điều khiển xe vượt xe khác?",
                "Quan sát để bảo đảm an toàn, báo hiệu bằng còi, đèn rồi vượt",
                "Tăng tốc độ để vượt nhanh",
                "Liên tục bấm còi và bật đèn pha",
                null, "A", "Phải quan sát kỹ và báo hiệu trước khi vượt");
            
            insertSampleQuestion(db, examSetId,
                "Tại ngã ba có đèn điều khiển giao thông, xe nào được quyền đi trước?",
                "Xe đi theo tín hiệu đèn xanh",
                "Xe có còi ưu tiên",
                "Cả hai trường hợp trên",
                null, "C", "Xe ưu tiên và xe đi theo đèn xanh đều được ưu tiên");
        }
        
        // Import flashcards from JSON file for Flashcard feature
        try {
            JsonImporter.importFlashcardsFromAssets(context, db, "A1_250Q_with_images.json");
        } catch (Exception e) {
            e.printStackTrace();
            // If JSON import fails, insert sample flashcards as fallback
            ContentValues values = new ContentValues();
            values.put("name", "Biển báo giao thông");
            values.put("description", "Các biển báo giao thông cơ bản");
            values.put("total_cards", 3);
            values.put("learned_cards", 0);
            long topicId = db.insert(TABLE_FLASHCARD_TOPICS, null, values);
            
            insertSampleFlashcard(db, topicId, "Biển nào cấm đi ngược chiều?", "Biển 3 - P.123a", "Biển này cấm phương tiện đi ngược chiều", "question_image_5.png");
            insertSampleFlashcard(db, topicId, "Biển nào cấm quay đầu xe?", "Biển 3 - P.123b", "Cấm quay đầu xe tại vị trí có biển này", "question_image_7.png");
            insertSampleFlashcard(db, topicId, "Biển nào báo hiệu đường dành cho xe thô sơ?", "Biển 3 - R.130", "Đường dành riêng cho xe thô sơ", "question_image_3.jpeg");
        }
    }
    
    private void insertSampleFlashcard(SQLiteDatabase db, long topicId, String front, String back, String explanation) {
        insertSampleFlashcard(db, topicId, front, back, explanation, null);
    }
    
    private void insertSampleFlashcard(SQLiteDatabase db, long topicId, String front, String back, String explanation, String imagePath) {
        ContentValues values = new ContentValues();
        values.put("front", front);
        values.put("back", back);
        values.put("explanation", explanation);
        values.put("image_path", imagePath);
        values.put("topic_id", topicId);
        values.put("is_learned", 0);
        values.put("review_count", 0);
        db.insert(TABLE_FLASHCARDS, null, values);
    }
    
    private void insertSampleQuestion(SQLiteDatabase db, long examSetId, String questionText,
                                     String optionA, String optionB, String optionC, String optionD,
                                     String correctAnswer, String explanation) {
        ContentValues values = new ContentValues();
        values.put("question_text", questionText);
        values.put("option_a", optionA);
        values.put("option_b", optionB);
        values.put("option_c", optionC);
        values.put("option_d", optionD);
        values.put("correct_answer", correctAnswer);
        values.put("explanation", explanation);
        values.put("exam_set_id", examSetId);
        db.insert(TABLE_QUESTIONS, null, values);
    }
    
    public List<FlashcardTopic> getAllFlashcardTopics() {
        List<FlashcardTopic> topics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FLASHCARD_TOPICS, null, null, null, null, null, "id DESC");
        
        if (cursor.moveToFirst()) {
            do {
                FlashcardTopic topic = new FlashcardTopic();
                topic.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                topic.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                topic.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                topic.setTotalCards(cursor.getInt(cursor.getColumnIndexOrThrow("total_cards")));
                topic.setLearnedCards(cursor.getInt(cursor.getColumnIndexOrThrow("learned_cards")));
                topic.setImageOnly(cursor.getInt(cursor.getColumnIndexOrThrow("is_image_only")) == 1);
                topics.add(topic);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return topics;
    }
    
    public List<Flashcard> getFlashcardsByTopic(int topicId) {
        List<Flashcard> flashcards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FLASHCARDS, null, "topic_id = ?", 
                new String[]{String.valueOf(topicId)}, null, null, "id ASC");
        
        if (cursor.moveToFirst()) {
            do {
                Flashcard flashcard = new Flashcard();
                flashcard.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                flashcard.setFront(cursor.getString(cursor.getColumnIndexOrThrow("front")));
                flashcard.setBack(cursor.getString(cursor.getColumnIndexOrThrow("back")));
                flashcard.setExplanation(cursor.getString(cursor.getColumnIndexOrThrow("explanation")));
                flashcard.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow("image_path")));
                flashcard.setTopicId(cursor.getInt(cursor.getColumnIndexOrThrow("topic_id")));
                flashcard.setLearned(cursor.getInt(cursor.getColumnIndexOrThrow("is_learned")) == 1);
                flashcard.setReviewCount(cursor.getInt(cursor.getColumnIndexOrThrow("review_count")));
                flashcard.setLastReviewTime(cursor.getLong(cursor.getColumnIndexOrThrow("last_review_time")));
                flashcards.add(flashcard);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return flashcards;
    }
    
    public void updateFlashcardLearned(int flashcardId, boolean isLearned) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_learned", isLearned ? 1 : 0);
        values.put("review_count", "review_count + 1");
        values.put("last_review_time", System.currentTimeMillis());
        db.update(TABLE_FLASHCARDS, values, "id = ?", new String[]{String.valueOf(flashcardId)});
    }
    
    public List<ExamSet> getAllExamSets() {
        List<ExamSet> examSets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXAM_SETS, null, null, null, null, null, "id DESC");
        
        if (cursor.moveToFirst()) {
            do {
                ExamSet examSet = new ExamSet();
                examSet.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                examSet.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                examSet.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                examSet.setQuestionCount(cursor.getInt(cursor.getColumnIndexOrThrow("question_count")));
                examSet.setOfficial(cursor.getInt(cursor.getColumnIndexOrThrow("is_official")) == 1);
                examSet.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
                examSets.add(examSet);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return examSets;
    }
    
    public List<Question> getQuestionsByExamSet(int examSetId) {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUESTIONS, null, "exam_set_id = ?",
                new String[]{String.valueOf(examSetId)}, null, null, "id ASC");
        
        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                question.setQuestionText(cursor.getString(cursor.getColumnIndexOrThrow("question_text")));
                question.setOptionA(cursor.getString(cursor.getColumnIndexOrThrow("option_a")));
                question.setOptionB(cursor.getString(cursor.getColumnIndexOrThrow("option_b")));
                question.setOptionC(cursor.getString(cursor.getColumnIndexOrThrow("option_c")));
                question.setOptionD(cursor.getString(cursor.getColumnIndexOrThrow("option_d")));
                question.setCorrectAnswer(cursor.getString(cursor.getColumnIndexOrThrow("correct_answer")));
                question.setExplanation(cursor.getString(cursor.getColumnIndexOrThrow("explanation")));
                question.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow("image_path")));
                question.setExamSetId(cursor.getInt(cursor.getColumnIndexOrThrow("exam_set_id")));
                questions.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questions;
    }
    
    public List<Question> getRandomQuestionsForTest(int examSetId, int totalQuestions) {
        List<Question> allQuestions = getQuestionsByExamSet(examSetId);
        
        List<Question> questionsWithImage = new ArrayList<>();
        List<Question> questionsWithoutImage = new ArrayList<>();
        
        for (Question q : allQuestions) {
            String imagePath = q.getImagePath();
            if (imagePath != null && !imagePath.isEmpty() && !imagePath.equals("null")) {
                questionsWithImage.add(q);
            } else {
                questionsWithoutImage.add(q);
            }
        }
        
        int numImageQuestions = (int) Math.round(totalQuestions * 0.3);
        int numNormalQuestions = totalQuestions - numImageQuestions;
        
        if (numImageQuestions > questionsWithImage.size()) {
            numImageQuestions = questionsWithImage.size();
            numNormalQuestions = totalQuestions - numImageQuestions;
        }
        
        if (numNormalQuestions > questionsWithoutImage.size()) {
            numNormalQuestions = questionsWithoutImage.size();
            numImageQuestions = totalQuestions - numNormalQuestions;
        }
        
        List<Question> selectedQuestions = new ArrayList<>();
        
        java.util.Collections.shuffle(questionsWithoutImage);
        for (int i = 0; i < numNormalQuestions && i < questionsWithoutImage.size(); i++) {
            selectedQuestions.add(questionsWithoutImage.get(i));
        }
        
        java.util.Collections.shuffle(questionsWithImage);
        for (int i = 0; i < numImageQuestions && i < questionsWithImage.size(); i++) {
            selectedQuestions.add(questionsWithImage.get(i));
        }
        
        java.util.Collections.shuffle(selectedQuestions);
        
        return selectedQuestions;
    }
    
    public long saveExamHistory(ExamHistory history) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("exam_set_id", history.getExamSetId());
        values.put("exam_name", history.getExamName());
        values.put("total_questions", history.getTotalQuestions());
        values.put("correct_answers", history.getCorrectAnswers());
        values.put("wrong_answers", history.getWrongAnswers());
        values.put("score", history.getScore());
        values.put("is_passed", history.isPassed() ? 1 : 0);
        values.put("test_date", history.getTestDate());
        values.put("duration_minutes", history.getDurationMinutes());
        values.put("answers_json", history.getAnswersJson());
        return db.insert(TABLE_EXAM_HISTORY, null, values);
    }
    
    public List<ExamHistory> getAllExamHistory() {
        List<ExamHistory> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXAM_HISTORY, null, null, null, null, null, "test_date DESC");
        
        if (cursor.moveToFirst()) {
            do {
                ExamHistory history = new ExamHistory();
                history.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                history.setExamSetId(cursor.getInt(cursor.getColumnIndexOrThrow("exam_set_id")));
                history.setExamName(cursor.getString(cursor.getColumnIndexOrThrow("exam_name")));
                history.setTotalQuestions(cursor.getInt(cursor.getColumnIndexOrThrow("total_questions")));
                history.setCorrectAnswers(cursor.getInt(cursor.getColumnIndexOrThrow("correct_answers")));
                history.setWrongAnswers(cursor.getInt(cursor.getColumnIndexOrThrow("wrong_answers")));
                history.setScore(cursor.getInt(cursor.getColumnIndexOrThrow("score")));
                history.setPassed(cursor.getInt(cursor.getColumnIndexOrThrow("is_passed")) == 1);
                history.setTestDate(cursor.getLong(cursor.getColumnIndexOrThrow("test_date")));
                history.setDurationMinutes(cursor.getInt(cursor.getColumnIndexOrThrow("duration_minutes")));
                history.setAnswersJson(cursor.getString(cursor.getColumnIndexOrThrow("answers_json")));
                historyList.add(history);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyList;
    }
}
