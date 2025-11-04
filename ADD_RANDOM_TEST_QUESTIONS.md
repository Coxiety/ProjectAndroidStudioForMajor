# Thêm Tính Năng Random Câu Hỏi Thi Thử (70-30)

## ❌ VẤN ĐỀ CŨ

Khi chọn thi thử với số câu hỏi tùy chỉnh (ví dụ 20 câu):
- App lấy **20 câu đầu tiên** từ database
- Không random
- Không phân bổ câu có hình và không có hình
- Người dùng luôn gặp cùng các câu nếu chọn cùng số lượng

```java
// CODE CŨ
questions = databaseHelper.getQuestionsByExamSet(examSetId);

if (questions.size() > numQuestions) {
    questions = questions.subList(0, numQuestions);  // ← Lấy tuần tự từ đầu
}
```

## ✅ GIẢI PHÁP MỚI

Tạo logic random với tỷ lệ:
- **70% câu thường** (không có hình)
- **30% câu có hình** (có imagePath)
- Random từ cả 2 nhóm
- Trộn lại để không theo thứ tự cố định

## 🔧 CÁC FILE ĐÃ SỬA

### 1. DatabaseHelper.java

Thêm method mới `getRandomQuestionsForTest()`:

```java
public List<Question> getRandomQuestionsForTest(int examSetId, int totalQuestions) {
    // Bước 1: Lấy tất cả câu hỏi
    List<Question> allQuestions = getQuestionsByExamSet(examSetId);
    
    // Bước 2: Phân loại thành 2 nhóm
    List<Question> questionsWithImage = new ArrayList<>();
    List<Question> questionsWithoutImage = new ArrayList<>();
    
    for (Question q : allQuestions) {
        String imagePath = q.getImagePath();
        if (imagePath != null && !imagePath.isEmpty() && !imagePath.equals("null")) {
            questionsWithImage.add(q);      // Có hình
        } else {
            questionsWithoutImage.add(q);   // Không có hình
        }
    }
    
    // Bước 3: Tính số lượng theo tỷ lệ 70-30
    int numImageQuestions = (int) Math.round(totalQuestions * 0.3);  // 30%
    int numNormalQuestions = totalQuestions - numImageQuestions;     // 70%
    
    // Bước 4: Xử lý trường hợp không đủ câu
    if (numImageQuestions > questionsWithImage.size()) {
        numImageQuestions = questionsWithImage.size();
        numNormalQuestions = totalQuestions - numImageQuestions;
    }
    
    if (numNormalQuestions > questionsWithoutImage.size()) {
        numNormalQuestions = questionsWithoutImage.size();
        numImageQuestions = totalQuestions - numNormalQuestions;
    }
    
    // Bước 5: Random và chọn câu từ mỗi nhóm
    List<Question> selectedQuestions = new ArrayList<>();
    
    Collections.shuffle(questionsWithoutImage);  // Random nhóm không có hình
    for (int i = 0; i < numNormalQuestions && i < questionsWithoutImage.size(); i++) {
        selectedQuestions.add(questionsWithoutImage.get(i));
    }
    
    Collections.shuffle(questionsWithImage);     // Random nhóm có hình
    for (int i = 0; i < numImageQuestions && i < questionsWithImage.size(); i++) {
        selectedQuestions.add(questionsWithImage.get(i));
    }
    
    // Bước 6: Trộn lại tất cả câu đã chọn
    Collections.shuffle(selectedQuestions);
    
    return selectedQuestions;
}
```

### 2. PracticeTestActivity.java

Đổi từ lấy tuần tự sang random:

```java
// TRƯỚC
databaseHelper = new DatabaseHelper(this);
questions = databaseHelper.getQuestionsByExamSet(examSetId);

if (questions.size() > numQuestions) {
    questions = questions.subList(0, numQuestions);
}

// SAU
databaseHelper = new DatabaseHelper(this);
questions = databaseHelper.getRandomQuestionsForTest(examSetId, numQuestions);
```

## 📊 LOGIC PHÂN BỔ

### Ví dụ: Thi 20 câu

```
Tổng: 20 câu
├─ 70% không có hình: 14 câu
└─ 30% có hình:       6 câu

Database có:
├─ 192 câu không có hình ✓ (đủ)
└─ 50 câu có hình       ✓ (đủ)

Kết quả:
1. Random chọn 14 câu từ 192 câu không có hình
2. Random chọn 6 câu từ 50 câu có hình
3. Trộn 20 câu lại với nhau
4. Return danh sách 20 câu random
```

### Ví dụ: Thi 30 câu

```
Tổng: 30 câu
├─ 70% không có hình: 21 câu
└─ 30% có hình:       9 câu

Kết quả:
1. Random chọn 21 câu không có hình
2. Random chọn 9 câu có hình
3. Trộn 30 câu lại
```

### Xử lý trường hợp đặc biệt

**Nếu không đủ câu có hình:**
```
Yêu cầu: 30 câu (9 có hình, 21 không có hình)
Thực tế: Chỉ có 5 câu có hình

Giải pháp:
├─ Lấy hết 5 câu có hình
└─ Lấy 25 câu không có hình (để đủ 30)
```

**Nếu không đủ câu không có hình:**
```
Yêu cầu: 30 câu (9 có hình, 21 không có hình)
Thực tế: Chỉ có 10 câu không có hình

Giải pháp:
├─ Lấy hết 10 câu không có hình
└─ Lấy 20 câu có hình (để đủ 30)
```

## 🎯 TỶ LỆ PHÂN BỔ

| Tổng câu | 70% Thường | 30% Có hình |
|----------|------------|-------------|
| 10       | 7 câu      | 3 câu       |
| 20       | 14 câu     | 6 câu       |
| 30       | 21 câu     | 9 câu       |
| 40       | 28 câu     | 12 câu      |
| 50       | 35 câu     | 15 câu      |

**Công thức:**
- Câu có hình = `Math.round(totalQuestions * 0.3)`
- Câu thường = `totalQuestions - câu có hình`

## 🔄 QUY TRÌNH HOẠT ĐỘNG

```
┌──────────────────────────────────┐
│ User chọn: Thi 20 câu            │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ getRandomQuestionsForTest(id,20) │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ Lấy tất cả 242 câu từ DB         │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ Phân loại:                       │
│ ├─ 192 câu không có hình         │
│ └─ 50 câu có hình                │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ Tính tỷ lệ:                      │
│ ├─ 14 câu không có hình (70%)    │
│ └─ 6 câu có hình (30%)           │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ Random từ mỗi nhóm:              │
│ ├─ Shuffle 192 → Lấy 14 câu     │
│ └─ Shuffle 50 → Lấy 6 câu       │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ Trộn 20 câu đã chọn              │
└───────────┬──────────────────────┘
            ↓
┌──────────────────────────────────┐
│ Return 20 câu random             │
└──────────────────────────────────┘
```

## 🚀 CÁCH TEST

### 1. Test Random
1. Chọn **Thi thử** > 20 câu
2. Ghi nhớ câu hỏi đầu tiên
3. Thoát và làm lại
4. **Kết quả**: Câu hỏi đầu tiên khác lần trước ✓

### 2. Test Tỷ lệ 70-30
1. Chọn thi 30 câu
2. Đếm số câu có hình khi làm bài
3. **Kết quả**: Khoảng 9 câu có hình (30%) ✓

### 3. Test với số câu khác nhau
```
Thử nghiệm:
├─ 10 câu → ~3 có hình
├─ 20 câu → ~6 có hình
├─ 30 câu → ~9 có hình
└─ 50 câu → ~15 có hình
```

## 📝 CODE EXAMPLE

### Random câu không có hình
```java
Collections.shuffle(questionsWithoutImage);
// Trước: [Q1, Q2, Q3, Q4, Q5, ...]
// Sau:  [Q17, Q3, Q89, Q45, Q12, ...]
```

### Lấy N câu đầu sau khi shuffle
```java
for (int i = 0; i < numNormalQuestions && i < questionsWithoutImage.size(); i++) {
    selectedQuestions.add(questionsWithoutImage.get(i));
}
// Lấy 14 câu đầu từ danh sách đã shuffle
```

### Trộn lại tất cả
```java
Collections.shuffle(selectedQuestions);
// Trộn 14 câu thường + 6 câu có hình
// → 20 câu theo thứ tự ngẫu nhiên
```

## 🎨 UX IMPROVEMENT

### Before:
```
Lần 1: Câu 1, 2, 3, 4, 5... (luôn giống nhau)
Lần 2: Câu 1, 2, 3, 4, 5... (lặp lại)
Lần 3: Câu 1, 2, 3, 4, 5... (nhàm chán)
```

### After:
```
Lần 1: Câu 45, 12, 89, 3, 156... (random)
Lần 2: Câu 78, 34, 5, 167, 23... (khác hoàn toàn)
Lần 3: Câu 102, 56, 8, 134, 67... (mỗi lần khác nhau)
```

**Lợi ích:**
- ✅ Tăng tính thách thức
- ✅ Tránh nhàm chán khi luyện tập nhiều lần
- ✅ Mô phỏng đề thi thực tế (có cả câu dễ, khó, có hình)
- ✅ Cân bằng giữa câu có hình và không có hình

## 💡 LƯU Ý

### 1. Tỷ lệ 70-30 là gợi ý
- Có thể điều chỉnh bằng cách thay `0.3` thành giá trị khác
- Ví dụ: `0.4` = 40% có hình, 60% thường

### 2. Math.round() để làm tròn
```java
int numImageQuestions = (int) Math.round(totalQuestions * 0.3);

// 20 * 0.3 = 6.0 → 6 câu
// 25 * 0.3 = 7.5 → 8 câu (làm tròn)
// 30 * 0.3 = 9.0 → 9 câu
```

### 3. Xử lý edge cases
- Nếu không đủ câu có hình → Lấy hết + bù bằng câu thường
- Nếu không đủ câu thường → Lấy hết + bù bằng câu có hình
- Đảm bảo luôn đủ số câu user yêu cầu

## 🎯 TỔNG KẾT

**Vấn đề**: Lấy câu tuần tự, không random, không cân bằng

**Giải pháp**: 
1. ✅ Tạo `getRandomQuestionsForTest()` trong DatabaseHelper
2. ✅ Phân loại câu có hình / không có hình
3. ✅ Random và lấy theo tỷ lệ 70-30
4. ✅ Trộn lại để không theo thứ tự cố định
5. ✅ Update PracticeTestActivity dùng method mới

**Kết quả**: Mỗi lần thi đều có bộ câu hỏi khác nhau, cân bằng giữa câu có hình và không có hình! 🎉

