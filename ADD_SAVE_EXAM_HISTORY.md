# Thêm Chức Năng Lưu Lịch Sử Thi

## ❌ VẤN ĐỀ

Hiện tại:
- Database lịch sử thi **đã có** ✓
- Activity xem lịch sử **đã có** ✓
- **NHƯNG**: `TestResultActivity` không lưu kết quả vào database
- Kết quả: Màn hình "Lịch sử thi" luôn trống

## ✅ GIẢI PHÁP

Thêm logic lưu kết quả thi tự động vào database khi hiển thị màn hình kết quả.

## 🔧 FILE ĐÃ SỬA

### TestResultActivity.java

#### 1. Thêm imports

```java
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.ExamHistory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
```

#### 2. Thêm fields

```java
private DatabaseHelper databaseHelper;
private int examSetId;
private String examName;
```

#### 3. Khởi tạo trong onCreate()

```java
databaseHelper = new DatabaseHelper(this);
examSetId = getIntent().getIntExtra("exam_set_id", -1);
examName = "Đề thi thử";

calculateResults();
displayResults();
saveToHistory();  // ← Thêm dòng này
setupListeners();
```

#### 4. Thêm method saveToHistory()

```java
private void saveToHistory() {
    try {
        // Tạo object ExamHistory
        ExamHistory history = new ExamHistory();
        history.setExamSetId(examSetId);
        history.setExamName(examName + " (" + totalQuestions + " câu)");
        history.setTotalQuestions(totalQuestions);
        history.setCorrectAnswers(correctAnswers);
        history.setWrongAnswers(wrongAnswers);
        
        // Tính điểm và kết quả
        int score = (correctAnswers * 100) / totalQuestions;
        history.setScore(score);
        history.setPassed(score >= 80);  // 80% để đậu
        history.setTestDate(System.currentTimeMillis());
        
        // Lưu thời gian làm bài
        int durationMinutes = getIntent().getIntExtra("duration", 0);
        history.setDurationMinutes(durationMinutes);
        
        // Lưu chi tiết câu trả lời dạng JSON
        ArrayList<Integer> questionIds = getIntent().getIntegerArrayListExtra("question_ids");
        ArrayList<String> selectedAnswers = getIntent().getStringArrayListExtra("selected_answers");
        ArrayList<String> correctAnswersList = getIntent().getStringArrayListExtra("correct_answers");
        
        JSONArray answersArray = new JSONArray();
        if (questionIds != null && selectedAnswers != null && correctAnswersList != null) {
            for (int i = 0; i < questionIds.size(); i++) {
                JSONObject answerObj = new JSONObject();
                answerObj.put("question_id", questionIds.get(i));
                answerObj.put("selected", selectedAnswers.get(i));
                answerObj.put("correct", correctAnswersList.get(i));
                answersArray.put(answerObj);
            }
        }
        history.setAnswersJson(answersArray.toString());
        
        // Lưu vào database
        databaseHelper.saveExamHistory(history);
    } catch (JSONException e) {
        e.printStackTrace();
    }
}
```

## 📊 DỮ LIỆU LƯU VÀO DATABASE

### Bảng exam_history

| Field            | Giá trị                          | Mô tả                           |
|------------------|----------------------------------|---------------------------------|
| exam_set_id      | 1                                | ID của đề thi                   |
| exam_name        | "Đề thi thử (20 câu)"            | Tên đề + số câu                 |
| total_questions  | 20                               | Tổng số câu                     |
| correct_answers  | 15                               | Số câu đúng                     |
| wrong_answers    | 5                                | Số câu sai                      |
| score            | 75                               | Điểm % (15/20 * 100)            |
| is_passed        | 0                                | 0=Rớt, 1=Đậu (cần >= 80%)      |
| test_date        | 1699234567890                    | Timestamp (milliseconds)        |
| duration_minutes | 45                               | Thời gian làm bài (phút)        |
| answers_json     | `[{...}, {...}]`                 | Chi tiết câu trả lời (JSON)     |

### Format answers_json

```json
[
  {
    "question_id": 123,
    "selected": "A",
    "correct": "C"
  },
  {
    "question_id": 124,
    "selected": "B",
    "correct": "B"
  },
  ...
]
```

## 🔄 QUY TRÌNH HOẠT ĐỘNG

```
┌──────────────────────────────────┐
│ User làm bài thi                 │
│ - Trả lời câu hỏi                │
│ - Nộp bài                        │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ PracticeTestActivity.submitTest()│
│ - Tạo Intent với dữ liệu         │
│ - question_ids, selected_answers │
│ - correct_answers, etc.          │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ TestResultActivity.onCreate()    │
│ 1. calculateResults()            │
│ 2. displayResults()              │
│ 3. saveToHistory() ← LƯU VÀO DB │
│ 4. setupListeners()              │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ saveToHistory()                  │
│ - Tạo ExamHistory object         │
│ - Tính score, isPassed           │
│ - Convert answers to JSON        │
│ - Call databaseHelper.save()    │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ DatabaseHelper.saveExamHistory() │
│ - Insert vào exam_history table │
│ - Return history_id              │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ Lưu thành công!                  │
│ User có thể xem trong            │
│ "Lịch sử thi"                    │
└──────────────────────────────────┘
```

## 📱 CÁC MÀN HÌNH LIÊN QUAN

### 1. Màn hình kết quả (TestResultActivity)
- Hiển thị điểm số
- Hiển thị Đậu/Rớt
- **Tự động lưu vào database** ← Đã thêm

### 2. Màn hình lịch sử (HistoryActivity)
- Load từ database: `getAllExamHistory()`
- Hiển thị danh sách các lần thi
- Click vào để xem chi tiết

### 3. Màn hình chi tiết lịch sử (HistoryDetailActivity)
- Hiển thị thông tin đầy đủ
- Thống kê, biểu đồ
- Xem lại các câu trả lời

## 🎯 TÍNH TOÁN ĐIỂM

### Công thức

```java
int score = (correctAnswers * 100) / totalQuestions;
boolean isPassed = score >= 80;
```

### Ví dụ

```
20/20 câu đúng → 100 điểm → Đậu ✓
18/20 câu đúng → 90 điểm  → Đậu ✓
16/20 câu đúng → 80 điểm  → Đậu ✓
15/20 câu đúng → 75 điểm  → Rớt ✗
10/20 câu đúng → 50 điểm  → Rớt ✗
```

### Điều kiện Đậu/Rớt

- **Đậu**: Điểm >= 80%
- **Rớt**: Điểm < 80%

(Giống như quy định thi thật bằng lái xe)

## 🔍 FORMAT DỮ LIỆU

### ExamHistory Object

```java
ExamHistory {
    id: 1
    examSetId: 1
    examName: "Đề thi thử (20 câu)"
    totalQuestions: 20
    correctAnswers: 15
    wrongAnswers: 5
    score: 75
    isPassed: false
    testDate: 1699234567890
    durationMinutes: 45
    answersJson: "[{...}, {...}]"
}
```

### Timestamp Format

```java
System.currentTimeMillis()
// → 1699234567890

// Convert về Date
Date date = new Date(1699234567890);
// → Mon Nov 06 2023 10:22:47

// Format hiển thị
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
String dateStr = sdf.format(date);
// → "06/11/2023 10:22"
```

## 🚀 CÁCH TEST

### 1. Làm một bài thi
```
1. Vào Thi thử > Chọn đề
2. Chọn 10 câu, thời gian 15 phút
3. Làm bài và nộp
4. Xem kết quả
```

### 2. Kiểm tra đã lưu
```
1. Quay về màn hình chính
2. Vào "Lịch sử thi"
3. Phải thấy: "Đề thi thử (10 câu)" vừa làm ✓
```

### 3. Xem chi tiết
```
1. Click vào lịch sử vừa làm
2. Thấy đầy đủ thông tin:
   - Điểm số
   - Số câu đúng/sai
   - Thời gian
   - Kết quả Đậu/Rớt
```

## 💡 LƯU Ý

### 1. Lưu tự động
- Không cần user nhấn nút "Lưu"
- Tự động lưu khi mở màn hình kết quả
- Đảm bảo mọi lần thi đều được ghi nhận

### 2. Không lưu trùng
- Mỗi lần mở TestResultActivity = 1 lần lưu
- Nếu user back và vào lại → Không lưu lại
- (Có thể thêm logic check trùng nếu cần)

### 3. answers_json
- Lưu chi tiết để có thể review lại
- Format JSON dễ parse
- Có thể dùng cho tính năng "Xem lại bài làm"

### 4. Performance
- Insert rất nhanh (~1-5ms)
- Không ảnh hưởng UX
- Thực hiện ngay sau displayResults()

## 🎨 UI MÀN HÌNH LỊCH SỬ

### HistoryActivity (Danh sách)

```
┌─────────────────────────────────┐
│ ← Lịch sử thi                   │
├─────────────────────────────────┤
│                                 │
│ ┌─────────────────────────────┐ │
│ │ Đề thi thử (20 câu)    [Đậu]│ │
│ │ Ngày: 06/11/2023 10:22      │ │
│ │ Điểm: 18/20                 │ │
│ └─────────────────────────────┘ │
│                                 │
│ ┌─────────────────────────────┐ │
│ │ Đề thi thử (30 câu)   [Rớt]│ │
│ │ Ngày: 05/11/2023 14:30      │ │
│ │ Điểm: 20/30                 │ │
│ └─────────────────────────────┘ │
│                                 │
│ ┌─────────────────────────────┐ │
│ │ Đề thi thử (10 câu)    [Đậu]│ │
│ │ Ngày: 04/11/2023 09:15      │ │
│ │ Điểm: 9/10                  │ │
│ └─────────────────────────────┘ │
│                                 │
└─────────────────────────────────┘
```

### Khi rỗng

```
┌─────────────────────────────────┐
│ ← Lịch sử thi                   │
├─────────────────────────────────┤
│                                 │
│                                 │
│      Chưa có lịch sử thi        │
│                                 │
│  Hãy thử làm một đề thi thử     │
│  để xem kết quả tại đây!        │
│                                 │
│                                 │
└─────────────────────────────────┘
```

## 📝 DATABASE SCHEMA

```sql
CREATE TABLE exam_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    exam_set_id INTEGER,
    exam_name TEXT,
    total_questions INTEGER,
    correct_answers INTEGER,
    wrong_answers INTEGER,
    score INTEGER,
    is_passed INTEGER,
    test_date INTEGER,
    duration_minutes INTEGER,
    answers_json TEXT,
    FOREIGN KEY(exam_set_id) REFERENCES exam_sets(id)
);
```

## 🎯 TỔNG KẾT

**Vấn đề**: TestResultActivity không lưu kết quả thi

**Giải pháp**: 
1. ✅ Thêm DatabaseHelper vào TestResultActivity
2. ✅ Tạo method `saveToHistory()`
3. ✅ Tự động lưu khi hiển thị kết quả
4. ✅ Lưu đầy đủ thông tin (điểm, câu trả lời, thời gian)
5. ✅ Format answers thành JSON để review sau

**Kết quả**: 
- Mỗi lần thi đều được lưu tự động
- User có thể xem lại lịch sử
- Có thể tracking progress theo thời gian
- Sẵn sàng cho tính năng thống kê/analytics! 🎉

