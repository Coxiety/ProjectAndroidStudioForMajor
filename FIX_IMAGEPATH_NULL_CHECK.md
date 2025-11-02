# Sửa Lỗi "Câu hỏi có hình" Hiển thị 242 thay vì 50

## ❌ VẤN ĐỀ

App hiển thị "Câu hỏi có hình: 0/242 thẻ" nhưng phải là "0/50 thẻ".

### Nguyên nhân
Lỗi nằm trong cách xử lý JSON null values:

```java
// CODE CŨ - SAI
String imagePath = question.optString("imagePath", null);
if (imagePath != null && !imagePath.isEmpty()) {
    // Insert flashcard
}
```

**VẤN ĐỀ**: Khi JSON có `"imagePath": null`, phương thức `optString("imagePath", null)` trong Android JSON **KHÔNG** trả về `null`, mà trả về chuỗi `"null"` (4 ký tự).

Kết quả:
- Điều kiện `imagePath != null` luôn đúng
- Điều kiện `!imagePath.isEmpty()` cũng đúng (vì "null" có 4 ký tự)
- Code insert TẤT CẢ 242 câu thay vì chỉ 50 câu có hình

## ✅ GIẢI PHÁP

Sử dụng `isNull()` để check đúng:

```java
// CODE MỚI - ĐÚNG
String imagePath = !question.isNull("imagePath") ? question.getString("imagePath") : null;
if (imagePath != null && !imagePath.isEmpty()) {
    // Insert flashcard
}
```

**HOẠT ĐỘNG**:
- `question.isNull("imagePath")` trả về `true` nếu field là null trong JSON
- Nếu không null, lấy giá trị bằng `getString("imagePath")`
- Nếu null, gán `imagePath = null`
- Chỉ insert khi `imagePath` thực sự có giá trị

## 🔧 CÁC FILE ĐÃ SỬA

### 1. JsonImporter.java

Sửa 2 chỗ:

#### a. Hàm `importFlashcardsFromAssets()` - Đếm số flashcards có hình

```java
// Dòng 99 - TRƯỚC
String imagePath = question.optString("imagePath", null);

// Dòng 99 - SAU
String imagePath = !question.isNull("imagePath") ? question.getString("imagePath") : null;
```

#### b. Hàm `createFlashcardsWithImagesTopic()` - Filter flashcards có hình

```java
// Dòng 163 - TRƯỚC
String imagePath = question.optString("imagePath", null);

// Dòng 163 - SAU
String imagePath = !question.isNull("imagePath") ? question.getString("imagePath") : null;
```

### 2. DatabaseHelper.java

Tăng database version để recreate database:

```java
// TRƯỚC
private static final int DATABASE_VERSION = 6;

// SAU
private static final int DATABASE_VERSION = 7;  // Fixed imagePath null check
```

## 📊 KẾT QUẢ

Sau khi sửa:
- ✅ `totalFlashcardsWithImages` = **50** (đúng!)
- ✅ Topic "Câu hỏi có hình" chứa **50 flashcards**
- ✅ Topic "Đề thi A1_250Q" vẫn chứa **242 flashcards**

## 🚀 CÁCH KIỂM TRA

### BƯỚC 1: UNINSTALL APP CŨ
- Long press icon "Learning App"
- Kéo lên "Uninstall"
- Nhấn "OK"

### BƯỚC 2: CLEAN PROJECT
- Menu **Build** → **Clean Project**
- Đợi hoàn tất

### BƯỚC 3: RUN APP
- Nhấn nút **Run** (tam giác xanh)
- Đợi build và cài đặt

### BƯỚC 4: KIỂM TRA
Mở **Flashcard Hub**, phải thấy:
- ✅ **"Câu hỏi có hình": 0/50 thẻ**
- ✅ **"Đề thi A1_250Q": 0/242 thẻ**

## 🎯 LƯU Ý

### Tại sao phải uninstall?
- Database version tăng từ 6 lên 7
- `onUpgrade()` trong DatabaseHelper sẽ DROP và recreate tables
- Nhưng chỉ khi app được **reinstall hoàn toàn**
- Nếu chỉ rebuild mà không uninstall, database cũ vẫn tồn tại

### Cách hoạt động của isNull()
```java
// JSON: "imagePath": null
question.isNull("imagePath")  // → true
question.optString("imagePath", null)  // → "null" (chuỗi!)

// JSON: "imagePath": "question_image_5.png"
question.isNull("imagePath")  // → false
question.getString("imagePath")  // → "question_image_5.png"
```

## 📝 TỔNG KẾT

**Root cause**: `optString()` không xử lý đúng JSON null values

**Solution**: Dùng `isNull()` để check trước khi `getString()`

**Impact**: Giảm từ 242 → 50 flashcards trong topic "Câu hỏi có hình"

**Database version**: 6 → 7

