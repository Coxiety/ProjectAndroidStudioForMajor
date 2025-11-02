# ✅ CHECKLIST BUILD ANDROID APP

## 📋 Checklist Hoàn Chỉnh

### Bước 1: Copy Files ✓
- [x] Copy `pdf-extractor-python/output/A1_250Q_with_images.json` → `app/src/main/assets/A1_250Q_with_images.json`
- [x] Tạo thư mục `app/src/main/assets/images/`
- [ ] Copy tất cả 80 files từ `pdf-extractor-python/output/A1_250Q_images/` → `app/src/main/assets/images/`

### Bước 2: Update Code ✓
- [x] Update `JsonImporter.java` - Thêm import imagePath
- [x] Update `DatabaseHelper.java` - Đổi tên file JSON
- [x] Tạo `ImageHelper.java` - Helper load ảnh
- [x] Update `activity_practice_test.xml` - Thêm ImageView
- [x] Update `strings.xml` - Thêm string resource
- [x] Update `PracticeTestActivity.java` - Load và hiển thị ảnh

### Bước 3: Build & Test
- [ ] Sync Gradle
- [ ] Build project (Ctrl+F9)
- [ ] Chạy app trên emulator/device
- [ ] Test câu hỏi không có hình
- [ ] Test câu hỏi có hình (từ câu 86 trở đi)
- [ ] Kiểm tra hình có load đúng không

---

## 🚨 LƯU Ý QUAN TRỌNG

### 1. File Assets
```
✅ ĐÚNG:
app/src/main/assets/
├── A1_250Q_with_images.json  (tên đúng)
└── images/
    ├── question_image_1.jpeg
    ├── question_image_2.jpeg
    └── ...

❌ SAI:
- File JSON đặt sai vị trí
- Thiếu thư mục images/
- Tên file ảnh không khớp với JSON
```

### 2. Xóa Database Cũ
**LẦN ĐẦU CHẠY:** Cần xóa app và cài lại để database rebuild với data mới!

```
Cách 1: Trong Android Studio
- Build > Clean Project
- Run > Delete App (trên device/emulator)
- Run app lại

Cách 2: Thủ công
- Gỡ app khỏi device
- Cài lại app
```

### 3. Build Variants
```
Build > Select Build Variant > debug
```

---

## 🔧 Các Lệnh Build

### Sync Gradle:
```
File > Sync Project with Gradle Files
```

### Clean Build:
```
Build > Clean Project
Build > Rebuild Project
```

### Run App:
```
Run > Run 'app'
hoặc Shift+F10
```

---

## 🧪 Test Cases

### Test 1: Câu Hỏi Không Có Hình
1. Mở app
2. Vào "Thi thử"
3. Chọn đề thi
4. Xem câu 1-85 (lý thuyết)
5. **Kỳ vọng:** Chỉ có text, không có hình

### Test 2: Câu Hỏi Có Hình
1. Tiếp tục làm bài
2. Xem câu 86-135 (biển báo)
3. **Kỳ vọng:** Có text + hình biển báo

### Test 3: Navigation
1. Next/Previous giữa các câu
2. **Kỳ vọng:** Hình load/unload đúng

### Test 4: Performance
1. Làm full 242 câu
2. **Kỳ vọng:** Không lag, không crash

---

## 📊 Data Đã Có

```
✅ 242 câu hỏi
   ├─ 192 câu KHÔNG có hình (Lý thuyết)
   └─ 50 câu CÓ hình (Biển báo, Sa hình)

✅ 80 files hình ảnh
   ├─ JPEG: 73 files
   └─ PNG: 7 files

✅ 100% đáp án đúng đã detect
```

---

## 🐛 Troubleshooting

### Lỗi 1: "Cannot resolve symbol 'ImageHelper'"
**Giải pháp:**
```
File > Invalidate Caches / Restart
```

### Lỗi 2: "java.io.FileNotFoundException: A1_250Q_with_images.json"
**Giải pháp:**
- Check file có trong `app/src/main/assets/` không
- Tên file phải CHÍNH XÁC
- Rebuild project

### Lỗi 3: ImageView không hiển thị
**Giải pháp:**
- Check thư mục `images/` có trong assets không
- Check tên file ảnh trong JSON khớp với file thực tế
- Check ImageView có được add vào layout không

### Lỗi 4: Database vẫn dùng data cũ
**Giải pháp:**
```
Xóa app khỏi device và cài lại!
```

### Lỗi 5: OutOfMemoryError
**Giải pháp:**
- Ảnh quá lớn, cần resize
- Thêm trong AndroidManifest.xml:
```xml
<application
    android:largeHeap="true"
    ...>
```

---

## 📱 Test Devices

Nên test trên:
- ✅ Android 8.0+ (API 26+)
- ✅ Màn hình 5-6 inch
- ✅ RAM 2GB+

---

## ✨ Expected Results

### Sau Khi Build Thành Công:

1. **Database:**
   - 1 exam set "Đề thi A1_250Q"
   - 242 questions
   - 50 questions có imagePath
   - 192 questions imagePath = null

2. **UI:**
   - Câu hỏi hiển thị đẹp
   - Hình load nhanh
   - Không bị lag khi next/previous

3. **Functionality:**
   - Làm bài thi bình thường
   - Timer hoạt động
   - Submit test OK
   - Xem kết quả OK

---

## 🎯 Next Steps (Sau Khi Test OK)

### 1. Update Các Activity Khác:
- [ ] QuestionDetailActivity - Xem chi tiết câu hỏi
- [ ] ReviewMistakesActivity - Ôn lỗi
- [ ] ExamDetailActivity - Browse questions

### 2. Add Features (Optional):
- [ ] Zoom hình khi click
- [ ] Placeholder image khi load
- [ ] Progress indicator khi load
- [ ] Cache images để load nhanh hơn

### 3. Optimization:
- [ ] Compress images nếu app quá nặng
- [ ] Implement Glide cho better performance
- [ ] Lazy loading images

---

## 📖 Tài Liệu Tham Khảo

- `HUONG_DAN_CAP_NHAT_CODE.md` - Chi tiết tất cả thay đổi
- `HUONG_DAN_ANDROID_INTEGRATION.txt` - Hướng dẫn tích hợp
- `pdf-extractor-python/README_FULL.md` - Về tool extract

---

## ✅ Final Check

Trước khi release:
- [ ] Tất cả features hoạt động
- [ ] Không có crash
- [ ] Performance tốt
- [ ] UI đẹp, responsive
- [ ] Data chính xác

---

**Status:** 🟡 READY TO BUILD

**Sau bước này:** Build app và test!

**Câu hỏi?** Xem file `HUONG_DAN_CAP_NHAT_CODE.md`

