# Sửa Hiển Thị Giá Trị Null - Trang Chi Tiết Câu Hỏi

## Vấn đề
- Các đáp án có giá trị null vẫn hiển thị text "D. null"
- Phần giải thích hiển thị "null" thay vì ẩn đi

## Giải pháp
Ẩn hoàn toàn các phần có giá trị null thay vì hiển thị chữ "null"

## Các file đã sửa

### 1. activity_question_detail.xml
**Thay đổi**: Thêm id cho CardView giải thích để có thể ẩn cả card

```xml
<androidx.cardview.widget.CardView
    android:id="@+id/cardExplanation"
    style="@style/CardStyle"
    ...>
```

### 2. QuestionDetailActivity.java
**Thay đổi chính**:

#### a. Thêm các import và biến cần thiết
```java
import android.view.View;
import androidx.cardview.widget.CardView;

private CardView cardExplanation;
```

#### b. Thêm hàm kiểm tra null
```java
private boolean isNullOrEmpty(String text) {
    return text == null || text.trim().isEmpty() || text.equalsIgnoreCase("null");
}
```

#### c. Thêm hàm set text cho option với kiểm tra null
```java
private void setOptionText(TextView textView, String label, String text) {
    if (isNullOrEmpty(text)) {
        textView.setVisibility(View.GONE); // Ẩn luôn TextView
    } else {
        textView.setVisibility(View.VISIBLE);
        textView.setText(label + ". " + text);
    }
}
```

#### d. Sử dụng hàm mới để set các đáp án
```java
setOptionText(tvOptionA, "A", optionA);
setOptionText(tvOptionB, "B", optionB);
setOptionText(tvOptionC, "C", optionC);
setOptionText(tvOptionD, "D", optionD);
```

#### e. Kiểm tra và ẩn card giải thích nếu null
```java
if (isNullOrEmpty(explanation)) {
    cardExplanation.setVisibility(View.GONE); // Ẩn cả card
} else {
    cardExplanation.setVisibility(View.VISIBLE);
    tvExplanation.setText(explanation);
}
```

#### f. Cập nhật hàm highlightCorrectAnswer
```java
private void highlightCorrectAnswer(String correctAnswer) {
    if (isNullOrEmpty(correctAnswer)) {
        return;
    }
    
    // ... switch case ...
    
    // Chỉ highlight nếu TextView đang visible
    if (correctTextView != null && correctTextView.getVisibility() == View.VISIBLE) {
        correctTextView.setTextColor(correctColor);
        correctTextView.setTypeface(null, Typeface.BOLD);
        correctTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.success_light));
    }
}
```

## Kết quả
Bây giờ trang chi tiết câu hỏi sẽ:
- ✅ Ẩn hoàn toàn các đáp án có giá trị null (không hiển thị "D. null" nữa)
- ✅ Ẩn cả card "Giải thích" nếu explanation null
- ✅ Chỉ highlight đáp án đúng nếu nó đang được hiển thị
- ✅ Giao diện gọn gàng hơn, không có text "null" nào

## Cách build lại app
```bash
# Trong Android Studio
1. Clean Project (Build > Clean Project)
2. Rebuild Project (Build > Rebuild Project)
3. Run app
```

## Lưu ý
Hàm `isNullOrEmpty()` kiểm tra 3 trường hợp:
1. `text == null` - giá trị null thật sự
2. `text.trim().isEmpty()` - chuỗi rỗng hoặc chỉ có khoảng trắng
3. `text.equalsIgnoreCase("null")` - chuỗi "null" (do JSON parsing)

