# Ẩn Options Null Thay Vì Hiển Thị "D. null"

## ❌ VẤN ĐỀ

Khi option C hoặc D có giá trị null, app vẫn hiển thị:
- "C. null"
- "D. null"

Điều này gây nhầm lẫn cho người dùng.

## ✅ GIẢI PHÁP

Thêm kiểm tra chuỗi "null" và ẩn hoàn toàn option đó nếu:
- Giá trị là `null`
- Giá trị là chuỗi rỗng `""`
- Giá trị là chuỗi `"null"`

## 🔧 CÁC FILE ĐÃ SỬA

### 1. ExamDetailActivity.java

**Màn hình xem đề thi**

```java
// TRƯỚC - Chỉ check null và empty
if (question.getOptionC() != null && !question.getOptionC().isEmpty()) {
    tvOptionC.setVisibility(View.VISIBLE);
    tvOptionC.setText("C. " + question.getOptionC());
} else {
    tvOptionC.setVisibility(View.GONE);
}

// SAU - Thêm check chuỗi "null"
if (question.getOptionC() != null && 
    !question.getOptionC().isEmpty() && 
    !question.getOptionC().equals("null")) {
    tvOptionC.setVisibility(View.VISIBLE);
    tvOptionC.setText("C. " + question.getOptionC());
} else {
    tvOptionC.setVisibility(View.GONE);
}
```

Áp dụng tương tự cho option D.

### 2. PracticeTestActivity.java

**Màn hình thi thử**

```java
// TRƯỚC
if (question.getOptionD() != null && !question.getOptionD().isEmpty()) {
    radioOptionD.setVisibility(View.VISIBLE);
    radioOptionD.setText("D. " + question.getOptionD());
} else {
    radioOptionD.setVisibility(View.GONE);
}

// SAU - Thêm check chuỗi "null"
if (question.getOptionD() != null && 
    !question.getOptionD().isEmpty() && 
    !question.getOptionD().equals("null")) {
    radioOptionD.setVisibility(View.VISIBLE);
    radioOptionD.setText("D. " + question.getOptionD());
} else {
    radioOptionD.setVisibility(View.GONE);
}
```

Áp dụng tương tự cho option C.

### 3. QuestionDetailActivity.java

**Đã đúng từ trước** - Có sẵn helper method:

```java
private boolean isNullOrEmpty(String text) {
    return text == null || 
           text.trim().isEmpty() || 
           text.equalsIgnoreCase("null");  // ← Đã check "null"
}
```

## 📊 LOGIC KIỂM TRA

### Ba điều kiện phải thỏa để hiển thị option:

1. **!= null**: Không phải null value
2. **!isEmpty()**: Không phải chuỗi rỗng ""
3. **!equals("null")**: Không phải chuỗi "null" (4 ký tự)

### Nếu một trong ba điều kiện sai → Ẩn option

```
Option value → Hiển thị?
--------------------------
null         → ẨN (null value)
""           → ẨN (empty string)
"null"       → ẨN (string "null")
"Đáp án A"   → HIỆN (có nội dung)
```

## 🎯 KẾT QUẢ

### Before:
```
A. Đáp án A
B. Đáp án B
C. Đáp án C
D. null        ← Hiển thị "null"
```

### After:
```
A. Đáp án A
B. Đáp án B
C. Đáp án C
               ← Option D bị ẩn hoàn toàn
```

## 📱 CÁC MÀN HÌNH ĐƯỢC SỬA

1. ✅ **ExamDetailActivity** - Xem đề thi
2. ✅ **PracticeTestActivity** - Thi thử
3. ✅ **QuestionDetailActivity** - Chi tiết câu hỏi (đã đúng từ trước)
4. ℹ️ **ReviewMistakesActivity** - Không cần sửa (chỉ hiển thị chữ cái A, B, C, D)

## 🚀 CÁCH TEST

1. Build và chạy app
2. Vào **Thi thử** > Chọn đề
3. Xem các câu hỏi → Chỉ thấy A, B, C (không có D. null)
4. Vào **Đề thi A1_250Q** 
5. Kiểm tra danh sách câu hỏi → Không thấy "D. null"

## 💡 LƯU Ý

### Tại sao xảy ra vấn đề "null" string?

Trong Android, khi sử dụng `JSONObject.optString()`:
```java
// JSON: "optionD": null
String optionD = question.optString("optionD", null);
// Kết quả: optionD = "null" (chuỗi 4 ký tự!)
```

Giải pháp đã áp dụng trong `JsonImporter.java`:
```java
// Sử dụng isNull() thay vì optString()
String optionD = !question.isNull("optionD") 
    ? question.getString("optionD") 
    : null;
```

Nhưng để đảm bảo, vẫn cần check thêm `!equals("null")` ở UI layer.

## 🎯 BEST PRACTICE

### Helper method (như QuestionDetailActivity):
```java
private boolean isValidOption(String option) {
    return option != null && 
           !option.trim().isEmpty() && 
           !option.equalsIgnoreCase("null");
}

// Sử dụng:
if (isValidOption(question.getOptionC())) {
    tvOptionC.setVisibility(View.VISIBLE);
    tvOptionC.setText("C. " + question.getOptionC());
} else {
    tvOptionC.setVisibility(View.GONE);
}
```

## 📝 TỔNG KẾT

**Vấn đề**: Hiển thị "D. null" gây nhầm lẫn

**Giải pháp**: Thêm check `!equals("null")` 

**Files sửa**: ExamDetailActivity.java, PracticeTestActivity.java

**Kết quả**: Options null được ẩn hoàn toàn, giao diện sạch đẹp hơn!

