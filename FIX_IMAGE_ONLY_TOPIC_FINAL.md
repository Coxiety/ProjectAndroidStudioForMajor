# Sửa Topic "Câu Hỏi Có Hình" - Bản Cuối Cùng

## Yêu cầu chính xác
Topic "Câu hỏi có hình" là:
- Chỉ chứa các flashcard CÓ HÌNH (filter `imagePath != null`)
- Hiển thị **BÌNH THƯỜNG**: Text câu hỏi ở trên + Hình ảnh ở dưới
- KHÔNG ẩn text câu hỏi
- Dành cho người dùng muốn ôn tập các câu hỏi có hình

## Số liệu thực tế
- **Tổng câu hỏi**: 242 câu
- **Câu có hình**: 50 câu (20.7%)
- Topic này nên có **50 flashcards**, không phải 242

## Các file đã sửa

### 1. DatabaseHelper.java
- Thêm cột `is_image_only` vào bảng `flashcard_topics` (để đánh dấu topic đặc biệt)
- Tăng `DATABASE_VERSION` lên 5
- Đọc field `is_image_only` khi query topics

```java
private static final int DATABASE_VERSION = 5;

String createFlashcardTopicsTable = "CREATE TABLE " + TABLE_FLASHCARD_TOPICS + " (" +
        // ... other columns ...
        "is_image_only INTEGER DEFAULT 0)";

public List<FlashcardTopic> getAllFlashcardTopics() {
    // ... existing code ...
    topic.setImageOnly(cursor.getInt(cursor.getColumnIndexOrThrow("is_image_only")) == 1);
    // ...
}
```

### 2. FlashcardTopic.java (Model)
- Thêm field `isImageOnly` để đánh dấu
- Thêm getter/setter

```java
private boolean isImageOnly;

public boolean isImageOnly() {
    return isImageOnly;
}

public void setImageOnly(boolean imageOnly) {
    isImageOnly = imageOnly;
}
```

### 3. JsonImporter.java
- Tạo topic "Câu hỏi có hình"
- Chỉ insert flashcards có `imagePath` vào topic này
- Set `is_image_only = 1`

```java
private static void createFlashcardsWithImagesTopic(Context context, SQLiteDatabase db, 
                                                      String fileName, int totalWithImages) {
    try {
        String jsonString = loadJsonFromAssets(context, fileName);
        JSONArray examSets = new JSONArray(jsonString);
        
        ContentValues topicValues = new ContentValues();
        topicValues.put("name", "Câu hỏi có hình");
        topicValues.put("description", "Chỉ những câu hỏi có hình ảnh minh họa");
        topicValues.put("total_cards", totalWithImages);  // <- 50, không phải 242
        topicValues.put("learned_cards", 0);
        topicValues.put("is_image_only", 1);
        
        long imageTopicId = db.insert("flashcard_topics", null, topicValues);
        
        // Loop và chỉ insert flashcards có imagePath
        for (int i = 0; i < examSets.length(); i++) {
            JSONObject examSetJson = examSets.getJSONObject(i);
            JSONArray questions = examSetJson.getJSONArray("questions");
            
            for (int j = 0; j < questions.length(); j++) {
                JSONObject question = questions.getJSONObject(j);
                
                String imagePath = question.optString("imagePath", null);
                
                // CHỈ INSERT NÊU CÓ HÌNH
                if (imagePath != null && !imagePath.isEmpty()) {
                    // ... insert flashcard với topic_id = imageTopicId ...
                }
            }
        }
    } catch (JSONException | IOException e) {
        e.printStackTrace();
    }
}
```

### 4. FlashcardHubActivity.java
- Truyền flag `is_image_only` (cho tương lai, hiện tại không dùng)

```java
intent.putExtra("is_image_only", topic.isImageOnly());
```

### 5. FlashcardSessionActivity.java
- Nhận flag `is_image_only` (nhưng không dùng để thay đổi cách hiển thị)
- Hiển thị bình thường: Text + Hình

```java
private boolean isImageOnly;

@Override
protected void onCreate(Bundle savedInstanceState) {
    // ...
    isImageOnly = getIntent().getBooleanExtra("is_image_only", false);
    // ...
}

private void showCurrentCard() {
    Flashcard card = flashcards.get(currentIndex);
    tvProgress.setText((currentIndex + 1) + "/" + flashcards.size());
    tvCardContent.setText(card.getFront());  // <- Luôn hiển thị text
    isFlipped = false;
    tvFlipHint.setVisibility(View.VISIBLE);
    
    // Always show text + image (normal mode)
    ImageHelper.loadFlashcardImage(this, ivFlashcardImage, card.getImagePath());
    
    // ...
}

private void flipCard() {
    Flashcard card = flashcards.get(currentIndex);
    if (!isFlipped) {
        tvCardContent.setText(card.getBack());
        isFlipped = true;
        tvFlipHint.setVisibility(View.GONE);
    } else {
        tvCardContent.setText(card.getFront());
        isFlipped = false;
        tvFlipHint.setVisibility(View.VISIBLE);
    }
}
```

## Kết quả

### Flashcard Hub
Sẽ hiển thị 2 topics:
1. **Đề thi A1_2500** - 242 flashcards (tất cả)
2. **Câu hỏi có hình** - 50 flashcards (chỉ câu có hình)

### Khi học "Câu hỏi có hình"
- **Mặt trước**: 📝 Text câu hỏi + 🖼️ Hình ảnh (luôn hiển thị cả 2)
- **Mặt sau**: 📝 Đáp án đúng

### So sánh với topic thường
Không có sự khác biệt về cách hiển thị, chỉ khác về **nội dung**:
- Topic thường: Có cả câu có hình + câu không có hình
- Topic "Câu hỏi có hình": Chỉ có câu có hình

## Cách build và test

### Bước 1: Clean và Rebuild
```bash
Build > Clean Project
Build > Rebuild Project
```

### Bước 2: Uninstall app cũ (BẮT BUỘC!)
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
2. Thấy 2 topics:
   - "Đề thi A1_2500" - **0/242 thẻ** ✅
   - "Câu hỏi có hình" - **0/50 thẻ** ✅
3. Nhấn vào "Câu hỏi có hình"
4. Tất cả 50 flashcards đều có hình ảnh
5. Hiển thị: Text câu hỏi + Hình ảnh (bình thường)

## Use Case

Topic "Câu hỏi có hình" hữu ích cho:
- 🚗 Ôn tập các câu hỏi về biển báo giao thông
- 🖼️ Tập trung vào các câu có hình ảnh minh họa
- ⚡ Luyện tập nhận biết hình ảnh kết hợp với nội dung câu hỏi

## Lưu ý quan trọng

1. **PHẢI UNINSTALL app cũ** để database rebuild với version 5
2. Topic này có **50 flashcards**, không phải 242
3. Hiển thị bình thường (text + hình), không có gì đặc biệt
4. Flag `is_image_only` chỉ để đánh dấu topic đặc biệt, không ảnh hưởng đến UI

## Database Version History

- **Version 1**: Initial database với sample data
- **Version 2**: Import questions từ JSON
- **Version 3**: Import flashcards từ JSON
- **Version 4**: Thêm topic "Đề thi có hình" (chưa hoàn chỉnh)
- **Version 5**: Fix topic "Câu hỏi có hình" với 50 flashcards, hiển thị bình thường (hiện tại)

