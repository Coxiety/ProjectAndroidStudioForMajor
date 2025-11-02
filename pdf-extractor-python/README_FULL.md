# PDF Question Extractor - Hướng Dẫn Đầy Đủ

## 📦 Tổng Quan

Tool extract câu hỏi thi bằng lái xe từ PDF với 3 phiên bản:

| Phiên bản | Tính năng | Tốc độ | Khuyên dùng |
|-----------|-----------|--------|-------------|
| `pdf_extractor.py` | Cơ bản, cần input | Nhanh | ⭐ Nếu muốn tùy chỉnh |
| `pdf_extractor_auto.py` | Tự động, không link hình | Nhanh | ⭐⭐ Nếu không cần hình |
| `pdf_extractor_with_images.py` | Tự động + link hình | Nhanh | ⭐⭐⭐ KHUYÊN DÙNG NHẤT |

## 🚀 Cài Đặt

### Bước 1: Cài Python
- Download: https://www.python.org/downloads/
- ✅ Tick "Add Python to PATH" khi cài

### Bước 2: Cài thư viện
```bash
cd pdf-extractor-python
pip install PyMuPDF Pillow
```

## 💻 Cách Sử Dụng

### Phiên bản 1: Cơ bản (Cần nhập thông tin)
```bash
python pdf_extractor.py "file.pdf"
```
- Sẽ hỏi thông tin đề thi
- Hỏi đường dẫn output
- Thích hợp để tùy chỉnh

### Phiên bản 2: Tự động (Không link hình)
```bash
python pdf_extractor_auto.py "file.pdf"
```
- Tự động mọi thứ
- Không cần nhập gì
- imagePath luôn = null

### Phiên bản 3: Tự động + Link hình ⭐
```bash
python pdf_extractor_with_images.py "file.pdf"
```
- Tự động mọi thứ
- **TỰ ĐỘNG link hình với câu hỏi**
- Phát hiện thông minh

## 📊 Kết Quả Thực Tế

Test với file `A1_250Q.pdf`:

```
✅ Total questions: 242
✅ Questions with images: 50 (biển báo, sa hình)
✅ Questions without images: 192 (lý thuyết)
✅ Correct answers detected: 242/242 (100%)
✅ Images extracted: 80
```

## 🎯 Tính Năng Image Linking

### Cách hoạt động:
1. **Track vị trí** - Biết hình nào ở trang nào
2. **Phát hiện keyword** - Tìm câu có từ "biển", "hình", "sa hình"
3. **Auto link** - Gán hình cùng trang cho câu hỏi phù hợp

### Từ khóa được nhận diện:
- "biển báo", "biển"
- "hình", "hình vẽ", "hình ảnh"
- "sa hình", "sơ đồ"
- "dưới đây", "trên đây"
- "tình huống"

### Ví dụ kết quả:
```json
{
  "questionText": "Biển nào cấm quay đầu xe?",
  "optionA": "Biển 1",
  "optionB": "Biển 2", 
  "optionC": "Biển 3",
  "correctAnswer": "C",
  "imagePath": "question_image_7.png" ← TỰ ĐỘNG!
}
```

## 📁 Cấu Trúc Output

```
output/
├── A1_250Q_with_images.json    # File JSON với link hình
├── A1_250Q.json                # File JSON không link hình
└── A1_250Q_images/             # Thư mục hình ảnh
    ├── question_image_1.jpeg
    ├── question_image_2.jpeg
    └── ...
```

## 🛠️ Công Cụ Hỗ Trợ

### Kiểm tra kết quả link hình:
```bash
python check_images.py
```

### Debug text trong PDF:
```bash
python debug_pdf_text_page.py "file.pdf" 4
```

## 📱 Tích Hợp Android App

### Copy files:
1. Copy `output/A1_250Q_with_images.json` → `app/src/main/assets/questions.json`
2. Copy `output/A1_250Q_images/*` → `app/src/main/assets/images/`

### Code Android:
```java
// Load question với hình
if (question.getImagePath() != null && !question.getImagePath().isEmpty()) {
    imageView.setVisibility(View.VISIBLE);
    String imagePath = "file:///android_asset/images/" + question.getImagePath();
    Glide.with(context)
         .load(imagePath)
         .into(imageView);
} else {
    imageView.setVisibility(View.GONE);
}
```

## ⚙️ Tùy Chỉnh

### Thêm từ khóa mới:
Edit file `pdf_extractor_with_images.py`, tìm hàm `question_references_image()`:

```python
def question_references_image(self, text):
    image_keywords = [
        'hình',
        'biển báo',
        'từ_khóa_mới_của_bạn',  # Thêm ở đây
    ]
    # ...
```

### Sửa thủ công:
Mở file JSON và edit trực tiếp:
```json
{
  "questionText": "...",
  "imagePath": "question_image_10.jpeg"  ← Sửa ở đây
}
```

## 🔍 Troubleshooting

### Không extract được câu hỏi:
- ✅ Check PDF không bị encrypted
- ✅ Check format PDF đúng chuẩn
- ✅ Dùng `debug_pdf_text_page.py` xem text

### Link hình không chính xác:
- ✅ Dùng `check_images.py` kiểm tra
- ✅ Thêm từ khóa vào `question_references_image()`
- ✅ Sửa thủ công trong JSON

### Thiếu hình:
- ✅ Check thư mục `output/[tên]_images/`
- ✅ PDF có thể có hình dạng vector (không extract được)
- ✅ Một số hình có thể bị duplicate

## 📈 Performance

| File | Pages | Questions | Images | Time |
|------|-------|-----------|--------|------|
| A1_250Q.pdf | 61 | 242 | 80 | ~8s |
| B2_600Q.pdf | 120 | 600 | 150 | ~15s |

## 🎓 Yêu Cầu Hệ Thống

- Python 3.7+
- Windows/Linux/Mac
- RAM: 512MB+
- Disk: 100MB+ (cho output)

## 📚 Tài Liệu

- `CACH_DUNG_DON_GIAN.txt` - Hướng dẫn ngắn gọn
- `HUONG_DAN.txt` - Hướng dẫn chi tiết
- `HUONG_DAN_IMAGE_LINKING.txt` - Hướng dẫn tính năng link hình
- `README_FULL.md` - Tài liệu này

## 🤝 Hỗ Trợ

Nếu gặp vấn đề:
1. Đọc tài liệu trong thư mục
2. Chạy debug scripts
3. Check format PDF

## ✨ Tóm Tắt

**TL;DR: Dùng cái này là được:**
```bash
python pdf_extractor_with_images.py "file.pdf"
```

Kết quả:
- ✅ Tự động extract câu hỏi
- ✅ Tự động extract hình
- ✅ Tự động link hình với câu hỏi
- ✅ Tự động detect đáp án đúng
- ✅ File JSON sẵn sàng cho Android app

---

**Created for:** ProjectMonLapTrinhAndroid  
**Purpose:** Extract motorcycle exam questions for learning app  
**Version:** 3.0 (With Image Linking)

