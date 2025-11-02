# Sửa Trang Chi Tiết Câu Hỏi - Hiển Thị Tất Cả Đáp Án

## Vấn đề
Trang chi tiết câu hỏi chỉ hiển thị đáp án đúng, không hiển thị tất cả các đáp án A, B, C, D.

## Giải pháp
Đã cập nhật để hiển thị tất cả 4 đáp án và đánh dấu đáp án đúng bằng:
- Màu xanh lá đậm (success color)
- Font chữ đậm
- Nền màu xanh lá nhạt

## Các file đã sửa

### 1. ExamDetailActivity.java
**Thay đổi**: Truyền tất cả các đáp án khi mở QuestionDetailActivity

```java
// Trước đây: Chỉ truyền đáp án đúng
intent.putExtra("correct_answer", question.getCorrectAnswer() + ". " + correctAnswerText);

// Bây giờ: Truyền tất cả đáp án
intent.putExtra("option_a", question.getOptionA());
intent.putExtra("option_b", question.getOptionB());
intent.putExtra("option_c", question.getOptionC());
intent.putExtra("option_d", question.getOptionD());
intent.putExtra("correct_answer", question.getCorrectAnswer());
intent.putExtra("image_path", question.getImagePath()); // Thêm hình ảnh
```

### 2. activity_question_detail.xml
**Thay đổi**: 
- Thêm ImageView để hiển thị hình ảnh câu hỏi
- Thay card "Đáp án đúng" thành card "Các đáp án" với 4 TextView cho A, B, C, D

```xml
<!-- Thêm ImageView trong card câu hỏi -->
<ImageView
    android:id="@+id/ivQuestionImage"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:layout_marginTop="16dp"
    android:adjustViewBounds="true"
    android:scaleType="fitCenter"
    android:visibility="gone"
    android:contentDescription="@string/question_image" />

<!-- Card hiển thị tất cả đáp án -->
<TextView
    android:id="@+id/tvOptionA"
    style="@style/BodyText"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="12dp"
    android:padding="12dp"
    android:text="A. Option A" />
<!-- ... tương tự cho B, C, D -->
```

### 3. QuestionDetailActivity.java
**Thay đổi**: 
- Nhận tất cả đáp án thay vì chỉ đáp án đúng
- Hiển thị tất cả 4 đáp án
- Đánh dấu đáp án đúng bằng hàm `highlightCorrectAnswer()`
- Thêm hiển thị hình ảnh

```java
// Nhận tất cả đáp án
String optionA = getIntent().getStringExtra("option_a");
String optionB = getIntent().getStringExtra("option_b");
String optionC = getIntent().getStringExtra("option_c");
String optionD = getIntent().getStringExtra("option_d");
String correctAnswer = getIntent().getStringExtra("correct_answer");
String imagePath = getIntent().getStringExtra("image_path");

// Hiển thị tất cả đáp án
tvOptionA.setText("A. " + optionA);
tvOptionB.setText("B. " + optionB);
tvOptionC.setText("C. " + optionC);
tvOptionD.setText("D. " + optionD);

// Đánh dấu đáp án đúng
highlightCorrectAnswer(correctAnswer);

// Hiển thị hình ảnh nếu có
ImageHelper.loadQuestionImage(this, ivQuestionImage, imagePath);
```

### 4. colors.xml
**Thêm màu mới**: Màu nền cho đáp án đúng

```xml
<color name="success_light">#E8F5E9</color>
```

## Kết quả
Bây giờ khi người dùng nhấn vào câu hỏi từ danh sách bài thi, trang chi tiết sẽ hiển thị:
1. Câu hỏi và hình ảnh (nếu có)
2. **Tất cả 4 đáp án A, B, C, D**
3. Đáp án đúng được đánh dấu rõ ràng với màu xanh lá và nền sáng
4. Giải thích

## Cách build lại app
```bash
# Trong Android Studio
1. Clean Project (Build > Clean Project)
2. Rebuild Project (Build > Rebuild Project)
3. Run app trên emulator hoặc thiết bị thật
```

Hoặc dùng Gradle:
```bash
cd d:\Data\AndroidProject\ProjectMonLapTrinhAndroid
gradlew clean assembleDebug
```

