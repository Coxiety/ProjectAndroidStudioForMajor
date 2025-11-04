# Sửa Màn Hình Xem Đề Thi - Hiển Thị Đầy Đủ Thông Tin

## ❌ VẤN ĐỀ

Màn hình "Đề thi A1_250Q" (ExamDetailActivity) chỉ hiển thị:
- Câu hỏi (giới hạn 2 dòng)
- Đáp án đúng (chỉ chữ cái: A, B, C, D)

**THIẾU**:
- Hình ảnh minh họa (nếu có)
- Nội dung các đáp án A, B, C, D
- Người dùng không thể đọc được nội dung các lựa chọn

## ✅ GIẢI PHÁP

Cập nhật layout và code để hiển thị đầy đủ:
1. Hình ảnh câu hỏi (nếu có)
2. Tất cả các đáp án A, B, C, D với nội dung đầy đủ
3. Highlight đáp án đúng bằng màu xanh nhạt

## 🔧 CÁC FILE ĐÃ SỬA

### 1. item_question.xml

**Thêm các view mới:**

```xml
<!-- Image cho câu hỏi có hình -->
<ImageView
    android:id="@+id/ivQuestionImage"
    android:layout_width="match_parent"
    android:layout_height="150dp"
    android:layout_marginTop="8dp"
    android:adjustViewBounds="true"
    android:scaleType="fitCenter"
    android:visibility="gone" />

<!-- 4 TextView cho các đáp án A, B, C, D -->
<TextView
    android:id="@+id/tvOptionA"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="8dp"
    android:background="@color/background"
    android:text="A. Option A" />
    
<TextView
    android:id="@+id/tvOptionB"
    ... />
    
<TextView
    android:id="@+id/tvOptionC"
    ... />
    
<TextView
    android:id="@+id/tvOptionD"
    ... />
```

**Thay đổi:**
- Bỏ `maxLines="2"` và `ellipsize="end"` khỏi tvQuestion
- Thêm ImageView cho hình ảnh (mặc định ẩn)
- Thêm 4 TextView cho các options
- Cập nhật text của tvCorrectAnswer: "Đáp án đúng: A"

### 2. ExamDetailActivity.java

**a. Thêm imports:**
```java
import android.graphics.Color;
import android.widget.ImageView;
import com.example.learningapp.utils.ImageHelper;
```

**b. Cập nhật ViewHolder:**
```java
class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvQuestionNumber, tvQuestion, tvOptionA, tvOptionB, 
             tvOptionC, tvOptionD, tvCorrectAnswer;
    ImageView ivQuestionImage;
    CardView cardView;
    
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        tvQuestion = itemView.findViewById(R.id.tvQuestion);
        ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
        tvOptionA = itemView.findViewById(R.id.tvOptionA);
        tvOptionB = itemView.findViewById(R.id.tvOptionB);
        tvOptionC = itemView.findViewById(R.id.tvOptionC);
        tvOptionD = itemView.findViewById(R.id.tvOptionD);
        tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
        cardView = (CardView) itemView;
    }
}
```

**c. Cập nhật bind() method:**
```java
public void bind(Question question, int number) {
    tvQuestionNumber.setText("Câu " + number);
    tvQuestion.setText(question.getQuestionText());
    
    // Load hình ảnh nếu có
    ImageHelper.loadQuestionImage(ExamDetailActivity.this, 
                                  ivQuestionImage, 
                                  question.getImagePath());
    
    // Hiển thị các đáp án
    tvOptionA.setText("A. " + question.getOptionA());
    tvOptionB.setText("B. " + question.getOptionB());
    
    // Ẩn option C nếu null
    if (question.getOptionC() != null && !question.getOptionC().isEmpty()) {
        tvOptionC.setVisibility(View.VISIBLE);
        tvOptionC.setText("C. " + question.getOptionC());
    } else {
        tvOptionC.setVisibility(View.GONE);
    }
    
    // Ẩn option D nếu null
    if (question.getOptionD() != null && !question.getOptionD().isEmpty()) {
        tvOptionD.setVisibility(View.VISIBLE);
        tvOptionD.setText("D. " + question.getOptionD());
    } else {
        tvOptionD.setVisibility(View.GONE);
    }
    
    // Reset background color
    tvOptionA.setBackgroundColor(Color.parseColor("#F5F5F5"));
    tvOptionB.setBackgroundColor(Color.parseColor("#F5F5F5"));
    tvOptionC.setBackgroundColor(Color.parseColor("#F5F5F5"));
    tvOptionD.setBackgroundColor(Color.parseColor("#F5F5F5"));
    
    // Highlight đáp án đúng bằng màu xanh nhạt
    switch (question.getCorrectAnswer()) {
        case "A":
            tvOptionA.setBackgroundColor(Color.parseColor("#C8E6C9"));
            break;
        case "B":
            tvOptionB.setBackgroundColor(Color.parseColor("#C8E6C9"));
            break;
        case "C":
            tvOptionC.setBackgroundColor(Color.parseColor("#C8E6C9"));
            break;
        case "D":
            tvOptionD.setBackgroundColor(Color.parseColor("#C8E6C9"));
            break;
    }
    
    tvCorrectAnswer.setText("Đáp án đúng: " + question.getCorrectAnswer());
}
```

## 🎨 THIẾT KẾ

### Màu sắc:
- **Options thường**: `#F5F5F5` (xám nhạt)
- **Đáp án đúng**: `#C8E6C9` (xanh lá nhạt)
- **Text "Đáp án đúng"**: `@color/success` (xanh lá đậm, bold)

### Layout:
```
┌─────────────────────────────┐
│ Câu 1                       │ ← Số câu (xanh, bold)
│                             │
│ [Câu hỏi đầy đủ...]        │ ← Câu hỏi (full text)
│                             │
│ [Hình ảnh 150dp]           │ ← Hình (nếu có)
│                             │
│ ┌─────────────────────┐    │
│ │ A. Đáp án A         │    │ ← Xám nhạt
│ └─────────────────────┘    │
│ ┌─────────────────────┐    │
│ │ B. Đáp án B         │    │ ← Xám nhạt
│ └─────────────────────┘    │
│ ┌─────────────────────┐    │
│ │ C. Đáp án C         │    │ ← Xanh (đáp án đúng)
│ └─────────────────────┘    │
│ ┌─────────────────────┐    │
│ │ D. Đáp án D         │    │ ← Xám nhạt
│ └─────────────────────┘    │
│                             │
│ Đáp án đúng: C              │ ← Xanh đậm, bold
└─────────────────────────────┘
```

## 📊 TÍNH NĂNG

### 1. Hiển thị đầy đủ thông tin
- ✅ Câu hỏi không bị cắt (bỏ maxLines)
- ✅ Hình ảnh hiển thị nếu có
- ✅ Tất cả 4 đáp án với nội dung đầy đủ

### 2. Ẩn hiện thông minh
- ✅ Hình chỉ hiện khi có imagePath
- ✅ Option C/D ẩn nếu null hoặc rỗng

### 3. Visual feedback
- ✅ Đáp án đúng được highlight màu xanh
- ✅ Dễ phân biệt đáp án đúng/sai

### 4. Tương tác
- ✅ Click vào card → Mở QuestionDetailActivity
- ✅ Xem chi tiết câu hỏi + giải thích

## 🚀 CÁCH TEST

1. Build và chạy app
2. Vào **Thi thử** > Chọn "Đề thi A1_250Q"
3. Kiểm tra:
   - ✅ Hiển thị đầy đủ câu hỏi
   - ✅ Hiển thị hình ảnh (với câu có hình)
   - ✅ Hiển thị đầy đủ 4 đáp án A, B, C, D
   - ✅ Đáp án đúng có màu xanh nhạt
   - ✅ Text "Đáp án đúng: C" màu xanh đậm

## 🎯 LƯU Ý

### Xử lý null values:
- Options C và D có thể null → Ẩn view nếu null
- ImagePath có thể null → ImageHelper tự động ẩn ImageView

### Performance:
- ImageHelper load hình từ assets efficiently
- RecyclerView tự động recycle views
- Chỉ bind data khi cần thiết

### Màu sắc:
- `#F5F5F5` = RGB(245, 245, 245) - Xám rất nhạt
- `#C8E6C9` = RGB(200, 230, 201) - Xanh lá nhạt (Material Green 100)

## 📝 TỔNG KẾT

**Before**:
```
Câu 1
Trong Luật TTATGTĐB, "đường...
Đáp án: C
```

**After**:
```
Câu 1
Trong Luật TTATGTĐB, "đường ưu tiên" được quy định như thế nào?

[Hình ảnh nếu có]

A. Đường ưu tiên là đường chỉ dành cho...
B. Đường ưu tiên là đường mà trên đó...
C. Đường ưu tiên là đường mà trên đó... (màu xanh)
D. [Ẩn nếu null]

Đáp án đúng: C
```

**Impact**: Người dùng có thể đọc và ôn tập đề thi hiệu quả hơn!

