# Thêm Chế Độ "Chỉ Xem Hình" Cho Flashcard

## Yêu cầu
Thêm topic "Chỉ xem hình" trong Flashcard Hub:
- Chỉ chứa flashcards có hình ảnh (`imagePath`)
- **Mặt trước**: Chỉ hiển thị hình ảnh (không có text câu hỏi)
- **Mặt sau**: Hiển thị đáp án (text)

## Giải pháp
1. Thêm cột `is_image_only` vào bảng `flashcard_topics`
2. Tạo topic "Chỉ xem hình" với `is_image_only = 1`
3. Sửa `FlashcardSessionActivity` để kiểm tra flag này và ẩn text khi ở mặt trước

## Các file đã sửa

### 1. DatabaseHelper.java

#### a. Thêm cột `is_image_only` vào table
```java
String createFlashcardTopicsTable = "CREATE TABLE " + TABLE_FLASHCARD_TOPICS + " (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "name TEXT NOT NULL, " +
        "description TEXT, " +
        "total_cards INTEGER DEFAULT 0, " +
        "learned_cards INTEGER DEFAULT 0, " +
        "is_image_only INTEGER DEFAULT 0)";  // <- Thêm cột mới
```

#### b. Đọc field `is_image_only` khi query
```java
public List<FlashcardTopic> getAllFlashcardTopics() {
    // ... existing code ...
    topic.setImageOnly(cursor.getInt(cursor.getColumnIndexOrThrow("is_image_only")) == 1);
    // ... existing code ...
}
```

#### c. Tăng DATABASE_VERSION lên 5
```java
private static final int DATABASE_VERSION = 5;  // Added "Chỉ xem hình" mode
```

### 2. FlashcardTopic.java (Model)

Thêm field và getter/setter:
```java
public class FlashcardTopic {
    // ... existing fields ...
    private boolean isImageOnly;
    
    // Constructor với isImageOnly
    public FlashcardTopic(int id, String name, String description, 
                          int totalCards, int learnedCards, boolean isImageOnly) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalCards = totalCards;
        this.learnedCards = learnedCards;
        this.isImageOnly = isImageOnly;
    }
    
    public boolean isImageOnly() {
        return isImageOnly;
    }
    
    public void setImageOnly(boolean imageOnly) {
        isImageOnly = imageOnly;
    }
}
```

### 3. JsonImporter.java

#### a. Đổi tên topic và set flag
```java
private static void createFlashcardsWithImagesTopic(...) {
    ContentValues topicValues = new ContentValues();
    topicValues.put("name", "Chỉ xem hình");  // Đổi tên
    topicValues.put("description", "Chỉ hiển thị hình ảnh câu hỏi (không có text)");
    topicValues.put("total_cards", totalWithImages);
    topicValues.put("learned_cards", 0);
    topicValues.put("is_image_only", 1);  // <- Set flag
    
    long imageTopicId = db.insert("flashcard_topics", null, topicValues);
    // ... rest of code ...
}
```

### 4. FlashcardHubActivity.java

Truyền flag `is_image_only` sang FlashcardSessionActivity:
```java
cardView.setOnClickListener(v -> {
    Intent intent = new Intent(FlashcardHubActivity.this, FlashcardSessionActivity.class);
    intent.putExtra("topic_id", topic.getId());
    intent.putExtra("topic_name", topic.getName());
    intent.putExtra("is_image_only", topic.isImageOnly());  // <- Truyền flag
    startActivity(intent);
});
```

### 5. FlashcardSessionActivity.java

#### a. Nhận flag từ Intent
```java
private boolean isImageOnly;

@Override
protected void onCreate(Bundle savedInstanceState) {
    // ... existing code ...
    isImageOnly = getIntent().getBooleanExtra("is_image_only", false);
    // ... existing code ...
}
```

#### b. Sửa hàm `showCurrentCard()`
```java
private void showCurrentCard() {
    Flashcard card = flashcards.get(currentIndex);
    tvProgress.setText((currentIndex + 1) + "/" + flashcards.size());
    isFlipped = false;
    tvFlipHint.setVisibility(View.VISIBLE);
    
    if (isImageOnly) {
        // Image-only mode: show only image on front
        tvCardContent.setVisibility(View.GONE);
        ImageHelper.loadFlashcardImage(this, ivFlashcardImage, card.getImagePath());
    } else {
        // Normal mode: show text on front, image below
        tvCardContent.setVisibility(View.VISIBLE);
        tvCardContent.setText(card.getFront());
        ImageHelper.loadFlashcardImage(this, ivFlashcardImage, card.getImagePath());
    }
    
    // ... rest of code ...
}
```

#### c. Sửa hàm `flipCard()`
```java
private void flipCard() {
    Flashcard card = flashcards.get(currentIndex);
    if (!isFlipped) {
        // Flip to back: always show text (answer)
        tvCardContent.setVisibility(View.VISIBLE);
        tvCardContent.setText(card.getBack());
        isFlipped = true;
        tvFlipHint.setVisibility(View.GONE);
    } else {
        // Flip to front
        if (isImageOnly) {
            // Image-only mode: hide text, show only image
            tvCardContent.setVisibility(View.GONE);
        } else {
            // Normal mode: show question text
            tvCardContent.setVisibility(View.VISIBLE);
            tvCardContent.setText(card.getFront());
        }
        isFlipped = false;
        tvFlipHint.setVisibility(View.VISIBLE);
    }
}
```

## Kết quả

### Flashcard Hub
Sẽ hiển thị 2 topics:
1. **Đề thi gốc 600 câu hỏi A1** - 250 flashcards (bình thường)
2. **Chỉ xem hình** - ~50-100 flashcards (chỉ câu có hình)

### Khi học "Chỉ xem hình"
- **Mặt trước**: 🖼️ CHỈ HIỂN THỊ HÌNH ẢNH (không có text câu hỏi)
- **Nhấn để lật**: 
- **Mặt sau**: 📝 Hiển thị đáp án đúng (text: "A. Đáp án...")

### Khi học topic bình thường
- **Mặt trước**: 📝 Text câu hỏi + 🖼️ Hình ảnh (nếu có)
- **Mặt sau**: 📝 Đáp án

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
2. Thấy topic **"Chỉ xem hình"** với description "Chỉ hiển thị hình ảnh câu hỏi (không có text)"
3. Nhấn vào topic này
4. **Mặt trước của flashcard**: CHỈ THẤY HÌNH, KHÔNG CÓ TEXT CÂU HỎI
5. Nhấn để lật → **Mặt sau**: Hiển thị đáp án dạng text "A. Đáp án..."
6. Lật lại → Lại chỉ thấy hình

## Database Schema Changes

### Bảng `flashcard_topics`
```sql
CREATE TABLE flashcard_topics (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    total_cards INTEGER DEFAULT 0,
    learned_cards INTEGER DEFAULT 0,
    is_image_only INTEGER DEFAULT 0  -- <- Cột mới
)
```

## Lưu ý quan trọng

1. **PHẢI UNINSTALL app cũ** để database rebuild với version 5
2. Nếu không uninstall:
   - Database vẫn ở version 4
   - Không có cột `is_image_only` → App sẽ crash
3. Topic "Chỉ xem hình" chỉ có flashcards có `imagePath` không null/empty
4. Nếu flashcard không có hình → Sẽ không xuất hiện trong topic này

## Use Case

Topic "Chỉ xem hình" hữu ích cho:
- 🚗 Học nhận biết biển báo giao thông chỉ qua hình ảnh
- 🧠 Rèn luyện trí nhớ hình ảnh
- ⚡ Ôn tập nhanh các câu hỏi có hình

## Database Version History

- **Version 1**: Initial database với sample data
- **Version 2**: Import questions từ JSON
- **Version 3**: Import flashcards từ JSON
- **Version 4**: Thêm topic "Đề thi có hình" (chưa có image-only mode)
- **Version 5**: Thêm cột `is_image_only` và mode "Chỉ xem hình" (hiện tại)

