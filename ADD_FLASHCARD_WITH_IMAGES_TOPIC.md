# Thêm Topic "Đề thi có hình" Trong Flashcard Hub

## Yêu cầu
Thêm một topic đặc biệt trong Flashcard Hub có tên "Đề thi có hình" chỉ chứa các flashcards có hình ảnh.

## Giải pháp
Khi import flashcards từ JSON:
1. Import tất cả flashcards vào topics gốc (như cũ)
2. Tạo thêm một topic đặc biệt "Đề thi có hình"
3. Copy chỉ những flashcards có `image_path` vào topic này

## Các file đã sửa

### 1. JsonImporter.java

#### a. Cập nhật hàm `importFlashcardsFromAssets()`
Thêm logic đếm số flashcards có hình:

```java
int totalFlashcardsWithImages = 0;

for (int j = 0; j < questions.length(); j++) {
    JSONObject question = questions.getJSONObject(j);
    // ... existing code ...
    String imagePath = question.optString("imagePath", null);
    
    if (imagePath != null && !imagePath.isEmpty()) {
        totalFlashcardsWithImages++;
    }
    
    // ... insert flashcard to original topic ...
}

// After importing all topics, create special topic
createFlashcardsWithImagesTopic(context, db, fileName, totalFlashcardsWithImages);
```

#### b. Thêm hàm mới `createFlashcardsWithImagesTopic()`
Tạo topic đặc biệt và chỉ insert flashcards có hình:

```java
private static void createFlashcardsWithImagesTopic(Context context, SQLiteDatabase db, 
                                                      String fileName, int totalWithImages) {
    try {
        String jsonString = loadJsonFromAssets(context, fileName);
        JSONArray examSets = new JSONArray(jsonString);
        
        // Create topic "Đề thi có hình"
        ContentValues topicValues = new ContentValues();
        topicValues.put("name", "Đề thi có hình");
        topicValues.put("description", "Tất cả các câu hỏi có hình ảnh minh họa");
        topicValues.put("total_cards", totalWithImages);
        topicValues.put("learned_cards", 0);
        
        long imageTopicId = db.insert("flashcard_topics", null, topicValues);
        
        // Loop through all questions and only insert those with images
        for (int i = 0; i < examSets.length(); i++) {
            JSONObject examSetJson = examSets.getJSONObject(i);
            JSONArray questions = examSetJson.getJSONArray("questions");
            
            for (int j = 0; j < questions.length(); j++) {
                JSONObject question = questions.getJSONObject(j);
                
                String imagePath = question.optString("imagePath", null);
                
                // Only insert if has image
                if (imagePath != null && !imagePath.isEmpty()) {
                    // ... get question data ...
                    
                    ContentValues flashcardValues = new ContentValues();
                    flashcardValues.put("front", questionText);
                    flashcardValues.put("back", correctAnswer + ". " + correctAnswerText);
                    flashcardValues.put("explanation", explanation);
                    flashcardValues.put("image_path", imagePath);
                    flashcardValues.put("topic_id", imageTopicId);  // Use special topic ID
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
```

### 2. DatabaseHelper.java
Tăng DATABASE_VERSION từ 3 lên 4:

```java
private static final int DATABASE_VERSION = 4;  // Added "Đề thi có hình" topic
```

## Kết quả
Sau khi rebuild database, Flashcard Hub sẽ hiển thị:
1. **Đề thi gốc 600 câu hỏi A1** - 250 flashcards (tất cả)
2. **Đề thi có hình** - Chỉ flashcards có hình ảnh (ví dụ: ~50-100 flashcards)

## Cách hoạt động
- Mỗi flashcard có hình sẽ tồn tại ở **2 topics**:
  1. Topic gốc (ví dụ: "Đề thi gốc 600 câu hỏi A1")
  2. Topic "Đề thi có hình"
  
- Khi user học trong "Đề thi có hình", họ chỉ thấy các câu hỏi có hình
- Tiến độ học (is_learned, review_count) được track riêng cho mỗi topic

## Cách build và test

### Bước 1: Clean và Rebuild
```bash
Build > Clean Project
Build > Rebuild Project
```

### Bước 2: Uninstall app cũ (QUAN TRỌNG!)
```bash
# Trong Android Studio
Right click app > Uninstall

# Hoặc dùng ADB
adb uninstall com.example.learningapp
```

### Bước 3: Run app
```bash
Run > Run 'app'
```

### Kiểm tra:
1. Mở **Flashcard Hub**
2. Thấy topic mới "Đề thi có hình" với description "Tất cả các câu hỏi có hình ảnh minh họa"
3. Nhấn vào topic này
4. Lướt qua các flashcards → Tất cả đều có hình ảnh

## Lưu ý quan trọng

1. **PHẢI UNINSTALL app cũ** để database rebuild với version 4
2. Nếu chỉ rebuild không uninstall → Topic mới sẽ không xuất hiện
3. Các flashcards có hình sẽ được duplicate trong database (tồn tại ở 2 topics)
4. Điều này là bình thường và cần thiết để hệ thống flashcard hoạt động đúng

## Tùy chỉnh

Nếu muốn thay đổi tên hoặc mô tả topic, sửa trong `JsonImporter.java`:

```java
topicValues.put("name", "Đề thi có hình");  // Đổi tên ở đây
topicValues.put("description", "Tất cả các câu hỏi có hình ảnh minh họa");  // Đổi mô tả ở đây
```

## Database Version History

- **Version 1**: Initial database với sample data
- **Version 2**: Import questions từ JSON
- **Version 3**: Import flashcards từ JSON
- **Version 4**: Thêm topic "Đề thi có hình" (hiện tại)

