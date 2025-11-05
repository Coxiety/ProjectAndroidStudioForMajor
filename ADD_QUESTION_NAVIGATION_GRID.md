# Thêm Lưới Điều Hướng Câu Hỏi Trong Thi Thử

## ✅ TÍNH NĂNG MỚI

Thêm button "DS câu" (Danh sách câu) để hiển thị lưới các số câu hỏi:
- **Câu chưa trả lời**: Màu xám
- **Câu đã trả lời**: Màu xanh (primary)
- **Click vào số**: Nhảy đến câu đó ngay lập tức
- **Grid layout**: 5 cột, dễ nhìn và dễ nhấn

## 🎨 GIAO DIỆN

### Button trong header:
```
┌─────────────────────────────────┐
│ 45:00    [DS câu]    1/30       │ ← Thêm button giữa
└─────────────────────────────────┘
```

### Dialog lưới câu hỏi:
```
┌─────────────────────────────────┐
│     Danh sách câu hỏi           │
├─────────────────────────────────┤
│  1   2   3   4   5              │
│  6   7   8   9  10              │
│ 11  12  13  14  15              │
│ 16  17  18  19  20              │
│ 21  22  23  24  25              │
│ 26  27  28  29  30              │
├─────────────────────────────────┤
│ ⬜ Chưa trả lời  🟦 Đã trả lời  │
└─────────────────────────────────┘

Màu:
- Số xám (⬜) = Chưa chọn đáp án
- Số xanh (🟦) = Đã chọn đáp án
```

## 🔧 CÁC FILE MỚI

### 1. dialog_question_overview.xml

Dialog chứa RecyclerView hiển thị grid:

```xml
<LinearLayout>
    <TextView>Danh sách câu hỏi</TextView>
    
    <RecyclerView
        android:id="@+id/recyclerViewQuestions"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    
    <!-- Legend -->
    <LinearLayout>
        <View (xám) /> <TextView>Chưa trả lời</TextView>
        <View (xanh) /> <TextView>Đã trả lời</TextView>
    </LinearLayout>
</LinearLayout>
```

### 2. item_question_number.xml

Item cho mỗi số câu (48x48dp):

```xml
<TextView
    android:id="@+id/tvQuestionNumber"
    android:layout_width="48dp"
    android:layout_height="48dp"
    android:background="@color/text_secondary"
    android:gravity="center"
    android:textColor="@android:color/white"
    android:textSize="16sp"
    android:textStyle="bold"
    android:clickable="true" />
```

## 🔧 CÁC FILE ĐÃ SỬA

### 1. activity_practice_test.xml

Thêm button "DS câu" vào header:

```xml
<LinearLayout android:background="@color/primary">
    <TextView android:id="@+id/tvTimeRemaining" />
    
    <!-- THÊM BUTTON MỚI -->
    <Button
        android:id="@+id/btnQuestionOverview"
        android:text="DS câu"
        android:textColor="@color/text_white" />
    
    <TextView android:id="@+id/tvQuestionProgress" />
</LinearLayout>
```

### 2. PracticeTestActivity.java

#### a. Thêm imports:
```java
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
```

#### b. Thêm button field:
```java
private Button btnQuestionOverview;
```

#### c. Initialize button:
```java
btnQuestionOverview = findViewById(R.id.btnQuestionOverview);
```

#### d. Setup listener:
```java
btnQuestionOverview.setOnClickListener(v -> showQuestionOverviewDialog());
```

#### e. Thêm method showQuestionOverviewDialog():
```java
private void showQuestionOverviewDialog() {
    // Inflate dialog layout
    View dialogView = getLayoutInflater().inflate(R.layout.dialog_question_overview, null);
    RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerViewQuestions);
    
    // Setup grid: 5 columns
    recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
    
    // Setup adapter
    QuestionOverviewAdapter adapter = new QuestionOverviewAdapter();
    recyclerView.setAdapter(adapter);
    
    // Create dialog
    AlertDialog dialog = new AlertDialog.Builder(this)
            .setView(dialogView)
            .create();
    
    // Handle click: jump to question and dismiss
    adapter.setOnQuestionClickListener(position -> {
        currentQuestionIndex = position;
        displayQuestion();
        dialog.dismiss();
    });
    
    dialog.show();
}
```

#### f. Thêm inner class QuestionOverviewAdapter:
```java
private class QuestionOverviewAdapter extends RecyclerView.Adapter<ViewHolder> {
    
    private OnQuestionClickListener listener;
    
    interface OnQuestionClickListener {
        void onQuestionClick(int position);
    }
    
    void setOnQuestionClickListener(OnQuestionClickListener listener) {
        this.listener = listener;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question_number, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }
    
    @Override
    public int getItemCount() {
        return questions.size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;
        
        ViewHolder(View itemView) {
            super(itemView);
            tvNumber = (TextView) itemView;
        }
        
        void bind(int position) {
            // Hiển thị số câu (1-indexed)
            tvNumber.setText(String.valueOf(position + 1));
            
            // Check đã trả lời chưa
            UserAnswer userAnswer = userAnswers.get(position);
            if (userAnswer.getSelectedAnswer() != null) {
                // Đã trả lời → màu xanh
                tvNumber.setBackgroundColor(getResources().getColor(R.color.primary, null));
            } else {
                // Chưa trả lời → màu xám
                tvNumber.setBackgroundColor(getResources().getColor(R.color.text_secondary, null));
            }
            
            // Handle click
            tvNumber.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onQuestionClick(position);
                }
            });
        }
    }
}
```

## 🔄 QUY TRÌNH HOẠT ĐỘNG

```
┌──────────────────────────────────┐
│ User đang làm bài thi            │
│ - Đã trả lời câu 1, 2, 3         │
│ - Đang ở câu 4                   │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ User click "DS câu"              │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ showQuestionOverviewDialog()     │
│ - Tạo RecyclerView grid 5 cột    │
│ - Load adapter với userAnswers   │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ Hiển thị dialog:                 │
│ ┌────────────────────────────┐  │
│ │  1🟦 2🟦 3🟦  4⬜  5⬜      │  │
│ │  6⬜  7⬜  8⬜  9⬜ 10⬜      │  │
│ │ 11⬜ 12⬜ 13⬜ 14⬜ 15⬜      │  │
│ │ ...                        │  │
│ └────────────────────────────┘  │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ User click số "15"               │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ listener.onQuestionClick(14)     │
│ - currentQuestionIndex = 14      │
│ - displayQuestion()              │
│ - dialog.dismiss()               │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ Màn hình hiện câu 15             │
│ User tiếp tục làm bài            │
└──────────────────────────────────┘
```

## 📊 LOGIC PHÂN LOẠI MÀU

### Check trạng thái:

```java
UserAnswer userAnswer = userAnswers.get(position);

if (userAnswer.getSelectedAnswer() != null) {
    // Có đáp án → Đã trả lời
    tvNumber.setBackgroundColor(primary_color);  // Xanh
} else {
    // Null → Chưa trả lời
    tvNumber.setBackgroundColor(gray_color);     // Xám
}
```

### Các trường hợp:

| Trạng thái câu hỏi | selectedAnswer | Màu nền |
|--------------------|----------------|---------|
| Chưa chọn gì       | null           | Xám     |
| Đã chọn A          | "A"            | Xanh    |
| Đã chọn B          | "B"            | Xanh    |
| Đã chọn C          | "C"            | Xanh    |
| Đã chọn D          | "D"            | Xanh    |

## 🎯 USER EXPERIENCE

### Trường hợp sử dụng:

#### 1. Kiểm tra tiến độ
```
User: "Tôi đã làm được bao nhiêu câu rồi?"
→ Click "DS câu"
→ Thấy 15 câu xanh, 15 câu xám
→ "Ồ còn 15 câu nữa!"
```

#### 2. Nhảy đến câu cụ thể
```
User: "Để tôi quay lại câu 5 xem lại"
→ Click "DS câu"
→ Click số "5"
→ Màn hình chuyển về câu 5
→ Xem lại và sửa đáp án nếu cần
```

#### 3. Làm câu theo chiến thuật
```
User: "Tôi muốn làm hết câu dễ trước"
→ Lướt qua các câu
→ Bỏ qua câu khó
→ Click "DS câu" xem câu nào chưa làm
→ Quay lại làm các câu còn lại
```

#### 4. Kiểm tra trước khi nộp
```
User: "Để xem tôi có bỏ sót câu nào không"
→ Click "DS câu"
→ Thấy tất cả đều xanh
→ "OK, nộp bài!"
```

## 🎨 LAYOUT DETAILS

### Grid Layout:
- **Columns**: 5 cột
- **Spacing**: 4dp margin mỗi item
- **Item size**: 48x48dp (vừa đủ để tap dễ dàng)
- **Text**: 16sp, bold, trắng

### Color Scheme:
```
Chưa trả lời: @color/text_secondary (#9E9E9E - Xám)
Đã trả lời:   @color/primary (#2196F3 - Xanh dương)
Text:         #FFFFFF (Trắng)
```

### Dialog:
- **Width**: match_parent
- **Height**: wrap_content
- **Padding**: 16dp
- **Background**: Trắng với corner radius

## 🚀 TƯƠNG LAI MỞ RỘNG

### Có thể thêm:

1. **Màu cho câu đánh dấu review**
```java
if (userAnswer.isMarkedForReview()) {
    tvNumber.setBackgroundColor(orange_color);  // Cam
}
```

2. **Hiển thị câu hiện tại**
```java
if (position == currentQuestionIndex) {
    tvNumber.setStrokeColor(accent_color);  // Viền đậm
    tvNumber.setStrokeWidth(3);
}
```

3. **Long press để đánh dấu**
```java
tvNumber.setOnLongClickListener(v -> {
    userAnswer.setMarkedForReview(!userAnswer.isMarkedForReview());
    notifyItemChanged(position);
    return true;
});
```

4. **Show số câu đã làm**
```java
dialogTitle.setText("Danh sách câu hỏi (15/30)");
```

## 💡 TIPS SỬ DỤNG

### Cho User:
1. Click "DS câu" để xem tổng quan
2. Màu xanh = đã trả lời, xám = chưa trả lời
3. Click vào số để nhảy đến câu đó
4. Kiểm tra trước khi nộp bài

### Cho Dev:
1. GridLayoutManager tự động wrap rows
2. RecyclerView tự động scroll nếu nhiều câu
3. Dialog dismiss tự động khi chọn câu
4. Màu sắc update realtime từ userAnswers

## 📱 RESPONSIVE

### Với nhiều câu (30-50):
- RecyclerView tự động scroll
- Grid vẫn giữ 5 cột
- Dialog height điều chỉnh tự động

### Với ít câu (10-20):
- Dialog nhỏ gọn
- Không cần scroll
- Dễ nhìn toàn bộ

## 🎯 TỔNG KẾT

**Tính năng**: Lưới điều hướng câu hỏi

**Files mới**: 
- dialog_question_overview.xml
- item_question_number.xml

**Files sửa**:
- activity_practice_test.xml (thêm button)
- PracticeTestActivity.java (thêm dialog + adapter)

**Lợi ích**:
- ✅ Xem tổng quan tiến độ
- ✅ Nhảy nhanh đến câu bất kỳ
- ✅ Kiểm tra câu chưa làm
- ✅ Chiến thuật làm bài linh hoạt
- ✅ UX giống app thi thật

**UX**: Giống các app thi trắc nghiệm chuyên nghiệp! 🎊

