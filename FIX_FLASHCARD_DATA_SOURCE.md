# Sửa Flashcard - Import Dữ Liệu Từ JSON Thay Vì Dùng Sample Data

## Vấn đề
- Flashcard đang sử dụng 3 flashcard mẫu (sample data)
- Không có hình ảnh
- Không sử dụng dữ liệu thật từ JSON file

## Nguyên nhân
Trước đây `DatabaseHelper` chỉ import questions vào bảng `questions` (cho Practice Test), không import vào bảng `flashcards`.

## Giải pháp
Tự động tạo flashcards từ dữ liệu questions trong JSON file:
- **Front**: Câu hỏi (questionText)
- **Back**: Đáp án đúng (correctAnswer + text)
- **Explanation**: Giải thích
- **Image**: Hình ảnh câu hỏi

## Các file đã sửa

### 1. JsonImporter.java
**Thêm hàm mới**: `importFlashcardsFromAssets()`

```java
public static void importFlashcardsFromAssets(Context context, SQLiteDatabase db, String fileName) {
    try {
        String jsonString = loadJsonFromAssets(context, fileName);
        JSONArray examSets = new JSONArray(jsonString);
        
        for (int i = 0; i < examSets.length(); i++) {
            JSONObject examSetJson = examSets.getJSONObject(i);
            
            // Tạo topic từ exam set
            JSONObject examSetInfo = examSetJson.getJSONObject("examSet");
            String topicName = examSetInfo.getString("name");
            String topicDescription = examSetInfo.getString("description");
            
            JSONArray questions = examSetJson.getJSONArray("questions");
            
            // Insert topic
            ContentValues topicValues = new ContentValues();
            topicValues.put("name", topicName);
            topicValues.put("description", topicDescription);
            topicValues.put("total_cards", questions.length());
            topicValues.put("learned_cards", 0);
            long topicId = db.insert("flashcard_topics", null, topicValues);
            
            // Tạo flashcard từ mỗi question
            for (int j = 0; j < questions.length(); j++) {
                JSONObject question = questions.getJSONObject(j);
                
                String questionText = question.getString("questionText");
                String correctAnswer = question.getString("correctAnswer");
                String optionA = question.optString("optionA", "");
                String optionB = question.optString("optionB", "");
                String optionC = question.optString("optionC", "");
                String optionD = question.optString("optionD", "");
                String explanation = question.optString("explanation", null);
                String imagePath = question.optString("imagePath", null);
                
                // Lấy text của đáp án đúng
                String correctAnswerText = "";
                switch (correctAnswer) {
                    case "A": correctAnswerText = optionA; break;
                    case "B": correctAnswerText = optionB; break;
                    case "C": correctAnswerText = optionC; break;
                    case "D": correctAnswerText = optionD; break;
                }
                
                // Insert flashcard
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
    } catch (JSONException | IOException e) {
        e.printStackTrace();
    }
}
```

### 2. DatabaseHelper.java
**Thay đổi 1**: Tăng DATABASE_VERSION từ 2 lên 3

```java
private static final int DATABASE_VERSION = 3;  // Increased to import flashcards from JSON
```

**Thay đổi 2**: Cập nhật hàm `insertSampleData()`

```java
private void insertSampleData(SQLiteDatabase db) {
    // Import questions from JSON file for Practice Tests
    try {
        JsonImporter.importQuestionsFromAssets(context, db, "A1_250Q_with_images.json");
    } catch (Exception e) {
        e.printStackTrace();
        // Fallback to sample questions if import fails
    }
    
    // Import flashcards from JSON file for Flashcard feature
    try {
        JsonImporter.importFlashcardsFromAssets(context, db, "A1_250Q_with_images.json");
    } catch (Exception e) {
        e.printStackTrace();
        // Fallback to sample flashcards if import fails
    }
}
```

## Kết quả
Bây giờ khi khởi động app, flashcards sẽ:
- ✅ Import tất cả 250 câu hỏi từ JSON thành flashcards
- ✅ Hiển thị hình ảnh nếu câu hỏi có hình
- ✅ Có giải thích đầy đủ
- ✅ Được nhóm theo topic (exam set name)

Cấu trúc flashcard:
- **Mặt trước**: Câu hỏi
- **Mặt sau**: A. Đáp án đúng
- **Giải thích**: Hiển thị khi nhấn nút "Xem giải thích"
- **Hình ảnh**: Hiển thị trên cả 2 mặt

## Cách build lại app
```bash
# Trong Android Studio
1. Clean Project (Build > Clean Project)
2. Rebuild Project (Build > Rebuild Project)
3. Uninstall app cũ trên emulator/thiết bị (để xóa database cũ)
4. Run app

# Hoặc dùng ADB
adb uninstall com.example.learningapp
```

## Lưu ý quan trọng
**PHẢI UNINSTALL APP CŨ** để database được tạo lại với version 3 và import dữ liệu mới!

Nếu chỉ rebuild mà không uninstall, app sẽ vẫn dùng database cũ (version 2) với 3 flashcard mẫu.

