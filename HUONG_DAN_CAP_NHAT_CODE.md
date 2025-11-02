# 📱 HƯỚNG DẪN CẬP NHẬT CODE ANDROID APP

## ✅ Các Thay Đổi Đã Thực Hiện

### 1. **Cập nhật JsonImporter** ✓
**File:** `app/src/main/java/com/example/learningapp/utils/JsonImporter.java`

**Thay đổi:**
- Thêm import field `imagePath` từ JSON
- Thêm dòng: `questionValues.put("image_path", question.optString("imagePath", null));`
- Sửa `optString` để xử lý null values an toàn hơn

**Mục đích:** Import đầy đủ dữ liệu từ JSON bao gồm cả đường dẫn hình ảnh

---

### 2. **Cập nhật DatabaseHelper** ✓
**File:** `app/src/main/java/com/example/learningapp/database/DatabaseHelper.java`

**Thay đổi:**
- Đổi tên file JSON từ `"questions.json"` → `"A1_250Q_with_images.json"`

**Mục đích:** Sử dụng file JSON mới có link hình ảnh

---

### 3. **Tạo ImageHelper Utility** ✓
**File mới:** `app/src/main/java/com/example/learningapp/utils/ImageHelper.java`

**Chức năng:**
- `loadQuestionImage()`: Load hình từ assets và hiển thị
- `hasImage()`: Check xem câu hỏi có hình không
- Tự động ẩn/hiện ImageView

**Mục đích:** Helper class để load ảnh từ assets một cách dễ dàng

---

### 4. **Cập nhật Layout XML** ✓
**File:** `app/src/main/res/layout/activity_practice_test.xml`

**Thay đổi:**
- Thêm `ImageView` với id `ivQuestionImage`
- Đặt giữa TextView câu hỏi và RadioGroup
- Mặc định `visibility="gone"` (sẽ hiện khi có hình)

**Mục đích:** UI để hiển thị hình ảnh câu hỏi

---

### 5. **Cập nhật Strings** ✓
**File:** `app/src/main/res/values/strings.xml`

**Thay đổi:**
- Thêm: `<string name="question_image">Hình ảnh câu hỏi</string>`

**Mục đích:** Content description cho ImageView

---

### 6. **Cập nhật PracticeTestActivity** ✓
**File:** `app/src/main/java/com/example/learningapp/activities/PracticeTestActivity.java`

**Thay đổi:**
- Thêm import: `ImageView` và `ImageHelper`
- Khai báo field: `private ImageView ivQuestionImage;`
- Initialize trong `initializeViews()`
- Load hình trong `displayQuestion()` với: `ImageHelper.loadQuestionImage(this, ivQuestionImage, question.getImagePath());`

**Mục đích:** Hiển thị hình ảnh khi làm bài thi

---

## 📂 Cấu Trúc File Cần Có

```
app/src/main/
├── assets/
│   ├── A1_250Q_with_images.json    ← File JSON chính (đã copy)
│   └── images/                      ← Thư mục hình ảnh
│       ├── question_image_1.jpeg
│       ├── question_image_2.jpeg
│       ├── question_image_3.jpeg
│       ├── ...
│       └── question_image_80.jpeg
│
├── java/com/example/learningapp/
│   ├── activities/
│   │   └── PracticeTestActivity.java  ← ✓ Đã update
│   ├── database/
│   │   └── DatabaseHelper.java        ← ✓ Đã update
│   ├── models/
│   │   ├── Question.java              ← ✓ Đã có imagePath
│   │   └── ExamSet.java               ← ✓ OK
│   └── utils/
│       ├── JsonImporter.java          ← ✓ Đã update
│       └── ImageHelper.java           ← ✓ File mới
│
└── res/
    ├── layout/
    │   └── activity_practice_test.xml ← ✓ Đã thêm ImageView
    └── values/
        └── strings.xml                 ← ✓ Đã thêm string
```

---

## 🔄 Flow Hoạt Động

### Khi App Khởi Động Lần Đầu:

1. **DatabaseHelper.onCreate()** được gọi
2. JsonImporter đọc file `A1_250Q_with_images.json` từ assets
3. Parse JSON và insert vào SQLite database
4. Tất cả 242 câu hỏi + 50 links hình được lưu vào DB

### Khi Làm Bài Thi:

1. **PracticeTestActivity** load câu hỏi từ database
2. Mỗi câu hỏi có field `imagePath` (có thể null)
3. `displayQuestion()` được gọi
4. `ImageHelper.loadQuestionImage()` check imagePath:
   - Nếu có → Load hình từ `assets/images/xxx.jpeg` → Hiển thị
   - Nếu null → Ẩn ImageView

---

## 🎯 Kết Quả

### Câu Hỏi KHÔNG CÓ Hình (192 câu - Lý thuyết):
- imagePath = null
- ImageView.visibility = GONE
- Chỉ hiển thị text câu hỏi

### Câu Hỏi CÓ Hình (50 câu - Biển báo/Sa hình):
- imagePath = "question_image_3.jpeg"
- ImageView.visibility = VISIBLE
- Hiển thị cả text và hình

---

## ✨ Ưu Điểm

1. **Tự động**: Không cần code thủ công cho từng câu
2. **Linh hoạt**: Dễ dàng thêm/bớt câu hỏi qua JSON
3. **Tiết kiệm**: Chỉ load hình khi cần
4. **An toàn**: Handle null an toàn, không crash
5. **Clean**: Code gọn gàng, dễ maintain

---

## 🚀 Các Bước Tiếp Theo (Tùy Chọn)

### 1. Update Các Activity Khác (Nếu Cần):

**QuestionDetailActivity** - Xem chi tiết câu hỏi:
```java
// Thêm ImageView vào layout
// Load ảnh tương tự PracticeTestActivity
ImageHelper.loadQuestionImage(this, imageView, question.getImagePath());
```

**ReviewMistakesActivity** - Ôn lỗi:
```java
// Thêm ImageView 
// Load ảnh cho câu sai
```

**ExamDetailActivity** - Browse câu hỏi:
```java
// Có thể hiển thị thumbnail
// Hoặc indicator cho biết câu có hình
```

---

### 2. Tối Ưu Hiệu Suất (Nếu Cần):

**Caching Images:**
```java
// Sử dụng Glide hoặc Picasso
implementation 'com.github.bumptech.glide:glide:4.16.0'

// Trong ImageHelper
Glide.with(context)
    .load("file:///android_asset/images/" + imagePath)
    .into(imageView);
```

**Lazy Loading:**
```java
// Chỉ load khi scroll đến
// Đã implement sẵn (load theo demand)
```

---

### 3. Test Cases:

- [ ] Test câu hỏi không có hình (imagePath = null)
- [ ] Test câu hỏi có hình (imagePath = "xxx.jpeg")
- [ ] Test với file ảnh bị thiếu (handle error)
- [ ] Test performance với nhiều câu hỏi
- [ ] Test layout trên nhiều kích thước màn hình

---

## 📝 Checklist Hoàn Thành

- [x] Copy file JSON vào assets
- [x] Copy 80 files ảnh vào assets/images/
- [x] Update JsonImporter - import imagePath
- [x] Update DatabaseHelper - đổi tên file JSON
- [x] Tạo ImageHelper utility class
- [x] Update layout - thêm ImageView
- [x] Update strings.xml
- [x] Update PracticeTestActivity - load ảnh
- [ ] Build và test app
- [ ] Test với câu có hình và không có hình
- [ ] (Optional) Update các Activity khác

---

## 🐛 Troubleshooting

### Lỗi: ImageView không hiển thị
**Nguyên nhân:** File ảnh không có trong assets/images/
**Giải pháp:** Check đúng path và tên file

### Lỗi: App crash khi load ảnh
**Nguyên nhân:** OutOfMemoryError với ảnh lớn
**Giải pháp:** 
```java
// Trong ImageHelper, thêm options để resize
BitmapFactory.Options options = new BitmapFactory.Options();
options.inSampleSize = 2; // Giảm 50% kích thước
```

### Lỗi: Database không có data
**Nguyên nhân:** File JSON không đúng format hoặc path
**Giải pháp:** 
- Check file `A1_250Q_with_images.json` có trong assets không
- Xóa app và cài lại để trigger onCreate()

---

## 💡 Tips

1. **Development**: Dùng emulator/device có tốc độ tốt để test
2. **Assets**: File ảnh nên < 200KB mỗi file
3. **Format**: JSON phải đúng format, không có trailing comma
4. **Testing**: Test trên nhiều API level khác nhau

---

**Hoàn thành bởi:** AI Assistant
**Ngày cập nhật:** 2024
**Status:** ✅ READY TO BUILD

