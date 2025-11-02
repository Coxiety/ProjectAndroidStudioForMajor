# PDF Question Extractor - Python Version

Phiên bản Python đơn giản hơn, không cần Maven!

## Yêu cầu (Requirements)

- Python 3.7 trở lên
- Các thư viện: PyPDF2, PyMuPDF, Pillow

## Cách cài đặt (Installation)

### Bước 1: Kiểm tra Python

```bash
python --version
```

Nếu chưa có Python, tải tại: https://www.python.org/downloads/

### Bước 2: Cài đặt thư viện

```bash
cd pdf-extractor-python
pip install -r requirements.txt
```

## Cách sử dụng (Usage)

### Cách 1: Chạy tự động (Windows)

Double-click file: **setup_and_run.bat**

### Cách 2: Chạy thủ công

```bash
cd pdf-extractor-python
python pdf_extractor.py
```

Sau đó nhập:
- Đường dẫn file PDF
- Thư mục lưu ảnh
- Đường dẫn file JSON output

### Cách 3: Với tham số dòng lệnh

```bash
python pdf_extractor.py "path/to/pdf.pdf" "./output/images" "./output/questions.json"
```

## Ví dụ (Example)

```bash
python pdf_extractor.py "../app/src/main/assets/1105395282_250 câu hỏi sát hạch xe Mô tô.pdf" "./output/images" "./output/questions.json"
```

## Kết quả (Output)

Sau khi chạy, bạn sẽ có:

```
output/
├── images/
│   ├── question_image_1.png
│   ├── question_image_2.png
│   └── ...
└── questions.json
```

## Sử dụng với Android App

1. Copy `output/questions.json` vào `app/src/main/assets/questions.json`
2. Copy ảnh từ `output/images/` vào `app/src/main/assets/images/`

## Khắc phục sự cố (Troubleshooting)

**Lỗi: Python không tìm thấy**
- Cài Python từ python.org
- Đảm bảo chọn "Add Python to PATH" khi cài

**Lỗi: pip không hoạt động**
```bash
python -m pip install -r requirements.txt
```

**Lỗi: Không trích xuất được câu hỏi**
- Kiểm tra PDF không bị mã hóa
- Đảm bảo PDF đúng định dạng tiếng Việt

## So sánh với Java Version

| Tính năng | Python | Java |
|-----------|--------|------|
| Cài đặt | Dễ | Cần Maven |
| Tốc độ | Nhanh | Nhanh hơn |
| Dung lượng | Nhỏ | Lớn hơn |
| Phụ thuộc | Python + libs | JDK + Maven |
| Database | Chưa có | Có SQLite |

## Ghi chú

- Python version dễ chạy hơn, phù hợp cho sử dụng nhanh
- Java version mạnh hơn, có database, phù hợp cho production
- Cả hai đều tạo ra JSON format giống nhau

---

**Tạo bởi:** ProjectMonLapTrinhAndroid  
**Mục đích:** Trích xuất câu hỏi thi xe máy cho ứng dụng học tập

