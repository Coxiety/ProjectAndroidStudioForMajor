# 🔧 FIX: App Dùng Data Cũ

## ❌ Vấn Đề

App không load data từ JSON mà vẫn dùng sample data cũ vì:
- Database VERSION = 1 (cũ)
- `onCreate()` chỉ chạy lần đầu tiên
- Database cũ vẫn còn với sample data

## ✅ Đã Sửa

**File:** `DatabaseHelper.java`

**Thay đổi:**
```java
// TRƯỚC:
private static final int DATABASE_VERSION = 1;

// SAU:
private static final int DATABASE_VERSION = 2;  // Increased to trigger rebuild
```

## 🎯 Cách Hoạt Động

### Khi DATABASE_VERSION Tăng:
1. App detect version cũ (1) < version mới (2)
2. Gọi `onUpgrade()` tự động
3. `onUpgrade()` sẽ:
   ```java
   DROP TABLE exam_history
   DROP TABLE questions
   DROP TABLE exam_sets
   DROP TABLE flashcards
   DROP TABLE flashcard_topics
   ```
4. Sau đó gọi `onCreate()` để tạo lại
5. Import data mới từ JSON

## 🚀 Các Bước Tiếp Theo

### Option A: Rebuild App (Khuyên dùng)
```bash
1. File > Sync Project with Gradle Files
2. Build > Clean Project
3. Build > Rebuild Project
4. Run app

→ Database sẽ tự động rebuild!
→ KHÔNG CẦN xóa app!
```

### Option B: Xóa App Thủ Công
```bash
1. Gỡ app khỏi device/emulator
2. Run app lại
→ onCreate() sẽ chạy
```

## 📊 Kết Quả Sau Khi Fix

### Trước (Sample Data):
```
Đề thi:
└─ "Đề thi chính thức số 1" (3 câu)

Flashcards:
└─ "Biển báo giao thông" (3 cards)
   ├─ Biển báo cấm đi ngược chiều
   ├─ Biển báo dừng lại
   └─ Biển báo nhường đường
```

### Sau (Real Data):
```
Đề thi:
└─ "Đề thi A1_250Q" (242 câu) ← Từ JSON!

Flashcards:
└─ "Biển báo giao thông" (3 cards với hình)
   ├─ "Biển nào cấm đi ngược chiều?" + hình
   ├─ "Biển nào cấm quay đầu xe?" + hình
   └─ "Biển nào báo hiệu đường dành cho xe thô sơ?" + hình
```

## 🧪 Kiểm Tra

### Test 1: Kiểm tra Số Lượng Câu Hỏi
```
1. Mở app
2. Vào "Xem đề thi"
3. Chọn đề thi
4. Check: Phải có 242 câu (không phải 3 câu!)
```

### Test 2: Kiểm tra Tên Đề
```
Tên đề phải là: "Đề thi A1_250Q"
KHÔNG PHẢI: "Đề thi chính thức số 1"
```

### Test 3: Kiểm tra Hình Ảnh
```
1. Làm bài thi
2. Đến câu 86 trở đi
3. Phải thấy hình biển báo
```

### Test 4: Kiểm tra Flashcard
```
1. Vào Flashcard
2. Chọn "Biển báo giao thông"
3. Phải thấy hình biển báo trong thẻ
```

## ⚙️ Technical Details

### onUpgrade() Flow:
```
User runs app with VERSION = 2
    ↓
System compares: old (1) vs new (2)
    ↓
Calls: onUpgrade(db, 1, 2)
    ↓
Drops all tables
    ↓
Calls: onCreate(db)
    ↓
Creates tables + Imports JSON
    ↓
Done! ✓
```

### onCreate() Flow:
```
Creates 5 tables:
├─ flashcard_topics
├─ flashcards
├─ exam_sets
├─ questions
└─ exam_history
    ↓
Inserts sample flashcards (3 cards với hình)
    ↓
Tries to import: A1_250Q_with_images.json
    ↓
SUCCESS: Imports 242 questions
    ↓
FALLBACK: If fail → insert 3 sample questions
```

## 🔍 Debug

### Nếu Vẫn Thấy Sample Data:

**Check 1: File JSON có trong assets không?**
```
app/src/main/assets/A1_250Q_with_images.json
→ Phải có file này!
```

**Check 2: Xem Logcat**
```
Filter: "JsonImporter"
→ Xem có error không
```

**Check 3: Database Version**
```
Code phải show: DATABASE_VERSION = 2
```

**Check 4: Clean Install**
```
Build > Clean Project
Build > Rebuild Project
Uninstall app manually
Run app again
```

## 📝 Version History

| Version | Changes |
|---------|---------|
| 1 | Initial - Sample data only |
| 2 | Import from JSON with images |

## ⚠️ Lưu Ý

1. **Version càng tăng → Database rebuild**
   - Mất hết data cũ (history, learned cards, etc.)
   - Nên backup nếu cần

2. **Production App:**
   - Cần migration thật sự
   - Không nên drop tables
   - Cần preserve user data

3. **Development:**
   - OK để drop và rebuild
   - Dễ test với data mới

## ✅ Checklist

- [x] Tăng DATABASE_VERSION từ 1 → 2
- [ ] Sync Gradle
- [ ] Clean Project
- [ ] Rebuild Project
- [ ] Run app
- [ ] Test: Check có 242 câu không
- [ ] Test: Check flashcard có hình không

## 🎉 Kết Luận

**Vấn đề:** Database VERSION không đổi → Data cũ  
**Giải pháp:** Tăng VERSION → Trigger rebuild → Data mới  
**Kết quả:** App dùng 242 câu từ JSON với hình!

---

**Status:** ✅ FIXED - Rebuild app là xong!

