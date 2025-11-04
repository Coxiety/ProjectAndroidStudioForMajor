# Sửa Màn Hình Kết Quả Thi Thử

## ❌ VẤN ĐỀ

### 1. Text hiển thị placeholder
Màn hình kết quả hiển thị:
- "Câu đúng: %1$d" (thay vì "Câu đúng: 3")
- "Câu sai: %1$d" (thay vì "Câu sai: 0")

### 2. Không hiển thị hình ảnh
Màn hình "Xem tất cả đáp án" không hiển thị hình ảnh câu hỏi.

## ✅ GIẢI PHÁP

### 1. Sửa String Placeholder

**File**: `strings.xml`

**Vấn đề**: String có format `%1$d` nhưng được dùng làm label text trong layout, không được format trong code.

```xml
<!-- TRƯỚC - Có format placeholder -->
<string name="correct_answers">Câu đúng: %1$d</string>
<string name="wrong_answers">Câu sai: %1$d</string>

<!-- SAU - Bỏ placeholder -->
<string name="correct_answers">Câu đúng:</string>
<string name="wrong_answers">Câu sai:</string>
```

**Lý do**: 
- Layout sử dụng string này cho TextView label
- Số lượng được set riêng vào `tvCorrectCount` và `tvWrongCount`
- Không cần format vì không dùng `getString(R.string.correct_answers, count)`

### 2. Thêm Hình Ảnh Vào Review Screen

#### a. Layout: item_review_question.xml

Thêm ImageView giữa câu hỏi và đáp án:

```xml
<TextView
    android:id="@+id/tvQuestion"
    ... />

<!-- Thêm ImageView mới -->
<ImageView
    android:id="@+id/ivQuestionImage"
    android:layout_width="match_parent"
    android:layout_height="150dp"
    android:layout_marginBottom="16dp"
    android:adjustViewBounds="true"
    android:scaleType="fitCenter"
    android:visibility="gone"
    android:contentDescription="@string/question_image" />

<TextView
    android:id="@+id/tvYourAnswer"
    ... />
```

#### b. Code: ReviewMistakesActivity.java

**Thêm imports:**
```java
import android.widget.ImageView;
import com.example.learningapp.utils.ImageHelper;
```

**Cập nhật ViewHolder:**
```java
class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvQuestionNumber, tvResultBadge, tvQuestion, tvYourAnswer, tvCorrectAnswer;
    ImageView ivQuestionImage;  // ← Thêm
    
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvResultBadge = itemView.findViewById(R.id.tvResultBadge);
        tvQuestion = itemView.findViewById(R.id.tvQuestion);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);  // ← Thêm
        tvYourAnswer = itemView.findViewById(R.id.tvYourAnswer);
        tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
    }
    
    public void bind(ReviewItem item) {
        tvQuestionNumber.setText("Câu " + item.questionNumber);
        tvQuestion.setText(item.question.getQuestionText());
        
        // ← Thêm load hình
        ImageHelper.loadQuestionImage(ReviewMistakesActivity.this, 
                                      ivQuestionImage, 
                                      item.question.getImagePath());
        
        // ... rest of code ...
    }
}
```

## 📊 CÁC FILE ĐÃ SỬA

### 1. strings.xml
- ✅ Bỏ `%1$d` khỏi `correct_answers`
- ✅ Bỏ `%1$d` khỏi `wrong_answers`

### 2. item_review_question.xml
- ✅ Thêm `ImageView` với id `ivQuestionImage`
- ✅ Đặt giữa câu hỏi và đáp án
- ✅ Mặc định `visibility="gone"`

### 3. ReviewMistakesActivity.java
- ✅ Thêm import `ImageView` và `ImageHelper`
- ✅ Thêm field `ivQuestionImage` trong ViewHolder
- ✅ Load hình trong `bind()` method

## 🎯 KẾT QUẢ

### Before:
**Kết quả:**
```
3/3
Đậu

Thống kê
Câu đúng: %1$d    3    ← Hiển thị placeholder
Câu sai: %1$d     0    ← Hiển thị placeholder
```

**Review:**
```
Câu 1
[Sai]
Trong Luật TTATGTĐB...
                          ← Không có hình
Bạn chọn: A
Đáp án đúng: C
```

### After:
**Kết quả:**
```
3/3
Đậu

Thống kê
Câu đúng:    3    ← Hiển thị đúng
Câu sai:     0    ← Hiển thị đúng
```

**Review:**
```
Câu 1
[Sai]
Trong Luật TTATGTĐB...

[Hình ảnh 150dp]      ← Có hình ảnh!

Bạn chọn: A
Đáp án đúng: C
```

## 🔍 GIẢI THÍCH VẤN ĐỀ

### Tại sao hiển thị "%1$d"?

**Layout hiện tại:**
```xml
<TextView
    android:text="@string/correct_answers" />  <!-- "Câu đúng: %1$d" -->
    
<TextView
    android:id="@+id/tvCorrectCount"
    android:text="25" />
```

**Code hiện tại:**
```java
tvCorrectCount.setText(String.valueOf(correctAnswers));  // Set số
// Nhưng label vẫn có "%1$d" vì không được format!
```

**Giải pháp 1 (Đã chọn)**: Bỏ placeholder khỏi string
```xml
<string name="correct_answers">Câu đúng:</string>
```

**Giải pháp 2**: Format string trong code (phức tạp hơn)
```java
String correctText = getString(R.string.correct_answers, correctAnswers);
tvCorrectLabel.setText(correctText);  // Cần đổi cấu trúc layout
```

## 🚀 CÁCH TEST

1. **Clean & Build**
   ```
   Build > Clean Project
   Build > Rebuild Project (hoặc Run)
   ```

2. **Test Kết quả**
   - Vào **Thi thử**
   - Làm bài (chọn đáp án)
   - Nộp bài
   - Kiểm tra: "Câu đúng: 3" (không còn %1$d)

3. **Test Hình ảnh**
   - Sau khi xem kết quả
   - Nhấn "XEM TẤT CẢ ĐÁP ÁN"
   - Kiểm tra: Câu hỏi có hình phải hiển thị hình ảnh

## 📝 LƯU Ý

### String Format trong Android:
```xml
<!-- Có placeholder - cần format -->
<string name="score">Điểm: %1$d/%2$d</string>

<!-- Sử dụng: -->
String text = getString(R.string.score, correct, total);
// Kết quả: "Điểm: 25/30"
```

```xml
<!-- Không có placeholder - dùng trực tiếp -->
<string name="label">Điểm:</string>

<!-- Sử dụng: -->
android:text="@string/label"
// Kết quả: "Điểm:"
```

### ImageHelper hoạt động:
- Kiểm tra `imagePath != null && !isEmpty()`
- Load bitmap từ `assets/Images/`
- Set `visibility = VISIBLE` nếu có hình
- Set `visibility = GONE` nếu không có hình

## 🎨 LAYOUT MẪU

```
┌─────────────────────────────────┐
│ Câu 1                    [Sai] │
│                                 │
│ Trong Luật TTATGTĐB...         │
│                                 │
│ ┌──────────────────────────┐   │
│ │                          │   │
│ │    [Hình ảnh câu hỏi]   │   │ ← 150dp
│ │                          │   │
│ └──────────────────────────┘   │
│                                 │
│ Bạn chọn: A (màu đỏ)          │
│ Đáp án đúng: C (màu xanh)     │
└─────────────────────────────────┘
```

## 📌 TỔNG KẾT

**Vấn đề 1**: String placeholder không được format
**Giải pháp**: Bỏ `%1$d` vì không cần format

**Vấn đề 2**: Không có hình ảnh trong review
**Giải pháp**: Thêm ImageView + ImageHelper.loadQuestionImage()

**Files sửa**: 
- `strings.xml` (2 strings)
- `item_review_question.xml` (thêm ImageView)
- `ReviewMistakesActivity.java` (load hình)

**Kết quả**: Màn hình kết quả và review hiển thị đúng! 🎉

