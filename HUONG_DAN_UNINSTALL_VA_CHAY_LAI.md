# Hướng Dẫn Uninstall App và Chạy Lại

## ⚠️ VẤN ĐỀ HIỆN TẠI
App đang hiển thị "Câu hỏi có hình: 0/242 thẻ" nhưng phải là "0/50 thẻ".

**NGUYÊN NHÂN**: Đã tìm ra lỗi trong code! `optString("imagePath", null)` trả về chuỗi "null" thay vì null value, nên code đã insert tất cả 242 câu thay vì 50 câu.

**ĐÃ SỬA**: Thay đổi logic thành `!question.isNull("imagePath")` để check đúng. Database version đã tăng lên **7**.

## ✅ GIẢI PHÁP - LÀM THEO TỪNG BƯỚC

### BƯỚC 1: UNINSTALL APP CŨ (QUAN TRỌNG NHẤT!)

#### Trên Emulator:
1. Mở emulator/thiết bị ảo
2. Tìm icon app **"Learning App"**
3. **Giữ icon lâu** (long press) khoảng 2 giây
4. Kéo icon lên góc trên màn hình vào chữ **"Uninstall"** hoặc icon thùng rác
5. Nhấn **"OK"** để xác nhận gỡ cài đặt

#### Trên Thiết Bị Thật:
1. Vào **Cài đặt** (Settings)
2. Chọn **Ứng dụng** (Apps)
3. Tìm **"Learning App"**
4. Nhấn vào app
5. Nhấn **"Gỡ cài đặt"** (Uninstall)
6. Xác nhận **"OK"**

### BƯỚC 2: XÓA BUILD CACHE TRONG ANDROID STUDIO

1. Vào menu **Build** (thanh menu trên cùng)
2. Chọn **Clean Project**
3. Đợi nó chạy xong (nhìn thanh progress bên dưới)

**Nếu không có "Rebuild Project":**
- Đó là bình thường! Chỉ cần Clean là đủ

### BƯỚC 3: RUN APP MỚI

1. Nhấn nút **Run** (tam giác xanh) ở thanh toolbar
   - Hoặc nhấn phím tắt **Shift + F10**
2. Chọn emulator/thiết bị
3. Đợi app build và cài đặt

### BƯỚC 4: KIỂM TRA KẾT QUẢ

Mở app > Vào **Flashcard Hub**

**KẾT QUẢ ĐÚNG:**
- ✅ "Câu hỏi có hình" - **0/50 thẻ** (không phải 0/242)
- ✅ "Đề thi A1_2500" - **0/242 thẻ**

**Nếu vẫn thấy 0/242:**
- ⚠️ Bạn chưa uninstall app cũ! Quay lại BƯỚC 1

---

## 🔍 CÁCH KIỂM TRA ĐÃ UNINSTALL CHƯA

Trước khi Run app mới:
1. Mở emulator/thiết bị
2. Vuốt lên để xem tất cả app
3. Tìm "Learning App"
4. **Nếu vẫn thấy icon app** → Chưa uninstall, làm lại BƯỚC 1
5. **Nếu không thấy icon app** → Đã uninstall thành công, tiếp tục BƯỚC 2

---

## 🎯 TẠI SAO PHẢI UNINSTALL?

Khi bạn uninstall app:
- ✅ Database cũ bị XÓA HOÀN TOÀN
- ✅ Khi cài app mới, `DatabaseHelper` sẽ tạo database MỚI (version 7)
- ✅ Database mới sẽ import đúng: CHỈ 50 flashcards có hình vào topic "Câu hỏi có hình"

Nếu KHÔNG uninstall:
- ❌ Database cũ vẫn TỒN TẠI với cấu trúc cũ
- ❌ Android không cho phép downgrade/upgrade database khi app đã cài
- ❌ App sẽ dùng database cũ với 242 flashcards

---

## 📱 CÁCH UNINSTALL NHANH TRÊN EMULATOR

**Cách 1**: Kéo thả (Drag & Drop)
1. Long press icon "Learning App"
2. Kéo lên góc màn hình
3. Thả vào "Uninstall"

**Cách 2**: Qua App Info
1. Long press icon "Learning App"
2. Chọn "App info" (icon chữ i)
3. Nhấn "Uninstall"

**Cách 3**: Qua Recent Apps
1. Nhấn nút "Recent Apps" (vuông)
2. Tìm "Learning App"
3. Vuốt lên để đóng app
4. Long press icon → Uninstall

---

## ❓ CÂU HỎI THƯỜNG GẶP

**Q: Tôi đã Clean Project rồi nhưng vẫn 0/242?**
A: Vì bạn chưa uninstall app. PHẢI uninstall mới xóa được database cũ.

**Q: Sau khi uninstall, dữ liệu học sẽ mất không?**
A: Có, dữ liệu học (tiến độ) sẽ reset về 0/50 và 0/242. Đây là điều cần thiết để rebuild database.

**Q: Tại sao không có "Rebuild Project"?**
A: Một số phiên bản Android Studio không có nút này. Chỉ cần "Clean Project" rồi "Run" là đủ.

**Q: Làm sao biết database đã update lên version 7 với fix mới?**
A: Sau khi uninstall và run lại, nếu thấy "0/50 thẻ" thay vì "0/242 thẻ" là đã thành công.

---

## 🚀 TÓM TẮT NHANH

1. **UNINSTALL APP CŨ** (quan trọng nhất!)
2. Clean Project trong Android Studio
3. Run app (nhấn nút tam giác xanh)
4. Kiểm tra: "Câu hỏi có hình" phải là **0/50 thẻ**

**Chỉ cần 4 bước này!**

