# TAI LIEU MO TA CHI TIET DU AN LEARNING APP

## Ung dung on thi ly thuyet lai xe hang A1

---

# MUC LUC

1. [TONG QUAN DU AN](#1-tong-quan-du-an)
2. [CONG NGHE SU DUNG](#2-cong-nghe-su-dung)
3. [KIEN TRUC HE THONG](#3-kien-truc-he-thong)
4. [CAU TRUC DU AN](#4-cau-truc-du-an)
5. [MO HINH DU LIEU](#5-mo-hinh-du-lieu)
6. [CO SO DU LIEU SQLITE](#6-co-so-du-lieu-sqlite)
7. [HE THONG DIEU HUONG](#7-he-thong-dieu-huong)
8. [TINH NANG FLASHCARD](#8-tinh-nang-flashcard)
9. [TINH NANG POMODORO TIMER](#9-tinh-nang-pomodoro-timer)
10. [TINH NANG THI THU](#10-tinh-nang-thi-thu)
11. [TINH NANG XEM DE THI](#11-tinh-nang-xem-de-thi)
12. [TINH NANG LICH SU](#12-tinh-nang-lich-su)
13. [TINH NANG CAI DAT](#13-tinh-nang-cai-dat)
14. [HE THONG XU LY HINH ANH](#14-he-thong-xu-ly-hinh-anh)
15. [GIAO DIEN NGUOI DUNG](#15-giao-dien-nguoi-dung)
16. [LUONG HOAT DONG](#16-luong-hoat-dong)
17. [TONG KET VA DANH GIA](#17-tong-ket-va-danh-gia)

---

# 1. TONG QUAN DU AN

## 1.1. Gioi thieu

**Learning App** la mot ung dung Android duoc phat trien nham ho tro nguoi dung on tap ly thuyet thi bang lai xe hang A1. Ung dung cung cap nhieu phuong phap hoc tap hieu qua, tu hoc flashcard, lam bai thi thu, den quan ly thoi gian hoc tap voi Pomodoro Timer.

## 1.2. Muc tieu du an

- Ho tro nguoi dung on tap 250 cau hoi ly thuyet thi bang lai xe A1
- Cung cap nhieu phuong phap hoc tap: Flashcard, Thi thu, Xem de thi
- Theo doi tien do va lich su hoc tap
- Quan ly thoi gian hoc tap hieu qua voi Pomodoro Timer
- Giao dien than thien, de su dung

## 1.3. Doi tuong nguoi dung

- Nguoi hoc ly thuyet thi bang lai xe hang A1
- Nguoi muon on tap truoc khi thi sat hach
- Nguoi can cong cu ho tro hoc tap co he thong

## 1.4. Tinh nang chinh

| STT | Tinh nang | Mo ta |
|-----|-----------|-------|
| 1 | Flashcard | Hoc voi the ghi nho, ho tro hinh anh |
| 2 | Pomodoro Timer | Quan ly thoi gian hoc tap theo ky thuat Pomodoro |
| 3 | Xem de thi | Duyet va xem toan bo cau hoi trong de thi |
| 4 | Thi thu | Lam bai thi thu co dem gio va cham diem |
| 5 | Lich su | Xem lai ket qua cac lan thi truoc |
| 6 | Cai dat | Tuy chinh ung dung theo y thich |

---

# 2. CONG NGHE SU DUNG

## 2.1. Ngon ngu lap trinh

### Java
- **Phien ban**: Java 8 (1.8)
- **Ly do chon**: Java la ngon ngu chinh thuc cua Android, co he sinh thai phong phu va tai lieu day du

```
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
```

## 2.2. Android SDK

### Cau hinh SDK
| Thong so | Gia tri | Mo ta |
|----------|---------|-------|
| compileSdk | 34 | Phien ban SDK de bien dich |
| minSdk | 24 | Android 7.0 (Nougat) - ho tro toi thieu |
| targetSdk | 34 | Android 14 - phien ban muc tieu |

### Ly do chon minSdk 24:
- Ho tro da so thiet bi Android hien tai (tren 95%)
- Cung cap cac API hien dai
- Dam bao tinh tuong thich tot

## 2.3. Android Gradle Plugin

- **Phien ban**: 8.13.0
- Cong cu build moi nhat cua Android

```groovy
plugins {
    id 'com.android.application' version '8.13.0' apply false
}
```

## 2.4. Thu vien AndroidX

### 2.4.1. AppCompat
```groovy
implementation 'androidx.appcompat:appcompat:1.6.1'
```
- Cung cap tinh tuong thich nguoc cho cac tinh nang moi
- ActionBar, Theme, Resources

### 2.4.2. Material Components
```groovy
implementation 'com.google.android.material:material:1.11.0'
```
- Thu vien Material Design 3
- Cung cap cac component UI hien dai
- Button, Card, TextField, Dialog

### 2.4.3. ConstraintLayout
```groovy
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
```
- Layout linh hoat va hieu suat cao
- Ho tro responsive design

### 2.4.4. CardView
```groovy
implementation 'androidx.cardview:cardview:1.0.0'
```
- Tao hieu ung the (card) voi bong do
- Su dung cho cac item trong danh sach

### 2.4.5. RecyclerView
```groovy
implementation 'androidx.recyclerview:recyclerview:1.3.2'
```
- Hien thi danh sach lon mot cach hieu qua
- Ho tro View Holder pattern
- Tai su dung view

## 2.5. Navigation Component

```groovy
implementation 'androidx.navigation:navigation-fragment:2.7.6'
implementation 'androidx.navigation:navigation-ui:2.7.6'
```

### Tinh nang:
- **Fragment Navigation**: Dieu huong giua cac Fragment
- **Safe Args**: Truyen du lieu an toan giua cac man hinh
- **Deep Linking**: Ho tro lien ket sau
- **Animation**: Hieu ung chuyen man hinh

### Loi ich:
- Quan ly back stack tu dong
- Tich hop voi ActionBar
- Type-safe argument passing
- Giam boilerplate code

## 2.6. Glide - Thu vien tai hinh anh

```groovy
implementation 'com.github.bumptech.glide:glide:4.16.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'
```

### Tinh nang:
- Tai hinh anh tu assets, file, URL
- Cache hinh anh thong minh
- Resize va transform hinh anh
- Xu ly bo nho hieu qua

## 2.7. SQLite Database

- **Co so du lieu noi bo** cua Android
- Luu tru cau hoi, flashcard, lich su thi
- Khong can thu vien ben ngoai

## 2.8. View Binding

```groovy
buildFeatures {
    viewBinding true
}
```

### Loi ich:
- Truy cap view an toan, khong con NullPointerException
- Type-safe, khong can ep kieu
- Compile-time checking
- Thay the findViewById()

---

# 3. KIEN TRUC HE THONG

## 3.1. Mo hinh kien truc

Ung dung su dung kien truc **Single-Activity Multiple-Fragments** ket hop voi **Navigation Component**.

```
+--------------------------------------------------+
|                   MainActivity                     |
|  +----------------------------------------------+  |
|  |              NavHostFragment                  |  |
|  |  +------------------------------------------+ |  |
|  |  |                                          | |  |
|  |  |          HomeFragment                    | |  |
|  |  |              |                           | |  |
|  |  |   +----+----+----+----+----+----+       | |  |
|  |  |   |    |    |    |    |    |    |       | |  |
|  |  |   v    v    v    v    v    v    v       | |  |
|  |  | Flash Pomo Exam Prac Hist Sett         | |  |
|  |  | card  doro List Test ory  ings         | |  |
|  |  |                                          | |  |
|  |  +------------------------------------------+ |  |
|  +----------------------------------------------+  |
+--------------------------------------------------+
```

## 3.2. Cac lop (Layers)

### 3.2.1. Presentation Layer (Fragments + Adapters)

```
+------------------------+
|    Presentation Layer  |
+------------------------+
| - HomeFragment         |
| - FlashcardHubFragment |
| - PomodoroHubFragment  |
| - ExamListFragment     |
| - PracticeTestFragment |
| - HistoryFragment      |
| - SettingsFragment     |
+------------------------+
| - QuestionAdapter      |
| - FlashcardTopicAdapter|
| - HistoryAdapter       |
+------------------------+
```

### 3.2.2. Data Layer (Database + Models)

```
+------------------------+
|      Data Layer        |
+------------------------+
|    DatabaseHelper      |
|  (SQLiteOpenHelper)    |
+------------------------+
| - Question             |
| - Flashcard            |
| - FlashcardTopic       |
| - ExamSet              |
| - ExamHistory          |
| - UserAnswer           |
| - PomodoroSession      |
+------------------------+
```

### 3.2.3. Utility Layer

```
+------------------------+
|     Utility Layer      |
+------------------------+
| - JsonImporter         |
| - ImageHelper          |
| - PreferencesHelper    |
+------------------------+
```

## 3.3. Luong du lieu

```
+-------------+     +-------------+     +-------------+
|   Assets    | --> | JsonImporter| --> | SQLite DB   |
| (JSON/Image)|     |             |     |             |
+-------------+     +-------------+     +-------------+
                                              |
                                              v
+-------------+     +-------------+     +-------------+
|  Fragment   | <-- | Adapter     | <-- |DatabaseHelper|
|   (UI)      |     | (ViewHolder)|     |   (DAO)     |
+-------------+     +-------------+     +-------------+
```

---

# 4. CAU TRUC DU AN

## 4.1. Cau truc thu muc chinh

```
ProjectMonLapTrinhAndroid/
|
+-- app/
|   +-- src/
|   |   +-- main/
|   |       +-- java/com/example/learningapp/
|   |       |   +-- activities/
|   |       |   |   +-- MainActivity.java
|   |       |   |
|   |       |   +-- adapters/
|   |       |   |   +-- ExamSetAdapter.java
|   |       |   |   +-- FlashcardTopicAdapter.java
|   |       |   |   +-- HistoryAdapter.java
|   |       |   |   +-- QuestionAdapter.java
|   |       |   |   +-- QuestionOverviewAdapter.java
|   |       |   |   +-- ReviewMistakesAdapter.java
|   |       |   |
|   |       |   +-- database/
|   |       |   |   +-- DatabaseHelper.java
|   |       |   |
|   |       |   +-- fragments/
|   |       |   |   +-- HomeFragment.java
|   |       |   |   +-- dethi/
|   |       |   |   |   +-- ExamListFragment.java
|   |       |   |   |   +-- ExamDetailFragment.java
|   |       |   |   |   +-- QuestionDetailFragment.java
|   |       |   |   +-- flashcard/
|   |       |   |   |   +-- FlashcardHubFragment.java
|   |       |   |   |   +-- FlashcardSessionFragment.java
|   |       |   |   +-- history/
|   |       |   |   |   +-- HistoryFragment.java
|   |       |   |   |   +-- HistoryDetailFragment.java
|   |       |   |   +-- pomodoro/
|   |       |   |   |   +-- PomodoroHubFragment.java
|   |       |   |   |   +-- PomodoroSessionFragment.java
|   |       |   |   +-- settings/
|   |       |   |   |   +-- SettingsFragment.java
|   |       |   |   +-- thithu/
|   |       |   |       +-- PracticeTestConfigFragment.java
|   |       |   |       +-- PracticeTestFragment.java
|   |       |   |       +-- TestResultFragment.java
|   |       |   |       +-- ReviewMistakesFragment.java
|   |       |   |
|   |       |   +-- models/
|   |       |   |   +-- Question.java
|   |       |   |   +-- Flashcard.java
|   |       |   |   +-- FlashcardTopic.java
|   |       |   |   +-- ExamSet.java
|   |       |   |   +-- ExamHistory.java
|   |       |   |   +-- UserAnswer.java
|   |       |   |   +-- PomodoroSession.java
|   |       |   |
|   |       |   +-- utils/
|   |       |       +-- JsonImporter.java
|   |       |       +-- ImageHelper.java
|   |       |       +-- PreferencesHelper.java
|   |       |
|   |       +-- res/
|   |       |   +-- layout/          (23 XML layout files)
|   |       |   +-- navigation/      (nav_graph.xml)
|   |       |   +-- values/          (colors, strings, styles)
|   |       |   +-- drawable/        (icons, shapes)
|   |       |   +-- anim/            (slide animations)
|   |       |
|   |       +-- assets/
|   |           +-- A1_250Q_with_images.json
|   |           +-- A1_250Q_images/  (80 hinh anh)
|   |
|   +-- build.gradle
|
+-- build.gradle
+-- settings.gradle
+-- gradle.properties
```

## 4.2. Mo ta cac package

### 4.2.1. Package `activities`
Chua Activity chinh cua ung dung. Theo Single-Activity Architecture, chi co 1 MainActivity.

### 4.2.2. Package `adapters`
Chua cac Adapter cho RecyclerView:
- **ExamSetAdapter**: Hien thi danh sach de thi
- **FlashcardTopicAdapter**: Hien thi cac chu de flashcard
- **HistoryAdapter**: Hien thi lich su thi
- **QuestionAdapter**: Hien thi danh sach cau hoi
- **QuestionOverviewAdapter**: Hien thi tong quan cau hoi khi thi
- **ReviewMistakesAdapter**: Hien thi danh sach cau sai

### 4.2.3. Package `database`
Chua DatabaseHelper - lop quan ly toan bo thao tac voi SQLite database.

### 4.2.4. Package `fragments`
Chua cac Fragment theo nhom chuc nang:
- **dethi**: Xem de thi va cau hoi
- **flashcard**: Hoc flashcard
- **history**: Lich su thi
- **pomodoro**: Pomodoro Timer
- **settings**: Cai dat
- **thithu**: Thi thu

### 4.2.5. Package `models`
Chua cac lop du lieu (POJO):
- Question, Flashcard, FlashcardTopic
- ExamSet, ExamHistory, UserAnswer
- PomodoroSession

### 4.2.6. Package `utils`
Chua cac lop tien ich:
- **JsonImporter**: Import du lieu tu JSON
- **ImageHelper**: Xu ly hinh anh
- **PreferencesHelper**: Quan ly SharedPreferences

---

# 5. MO HINH DU LIEU

## 5.1. Class Question

```java
public class Question {
    private int id;
    private String questionText;    // Noi dung cau hoi
    private String optionA;         // Dap an A
    private String optionB;         // Dap an B
    private String optionC;         // Dap an C (co the null)
    private String optionD;         // Dap an D (co the null)
    private String correctAnswer;   // Dap an dung: "A", "B", "C", "D"
    private String imagePath;       // Duong dan hinh anh
    private boolean isLiet;         // Cau liet (sai la truot)
    private int examSetId;          // ID de thi chua cau hoi
}
```

### Dac diem:
- Ho tro 2-4 lua chon tuy cau hoi
- Co co `isLiet` danh dau cau hoi quan trong
- Lien ket voi ExamSet qua `examSetId`

## 5.2. Class Flashcard

```java
public class Flashcard {
    private int id;
    private String front;           // Mat truoc (cau hoi + lua chon)
    private String back;            // Mat sau (dap an dung)
    private String explanation;     // Giai thich
    private String imagePath;       // Duong dan hinh anh
    private int topicId;            // ID chu de
    private boolean isLearned;      // Da hoc chua
    private int reviewCount;        // So lan xem lai
    private long lastReviewTime;    // Lan xem cuoi
}
```

### Dac diem:
- Theo doi tien do hoc (`isLearned`, `reviewCount`)
- Ghi nhan thoi gian hoc cuoi (`lastReviewTime`)
- Ho tro hinh anh minh hoa

## 5.3. Class FlashcardTopic

```java
public class FlashcardTopic {
    private int id;
    private String name;            // Ten chu de
    private String description;     // Mo ta
    private int totalCards;         // Tong so the
    private int learnedCards;       // So the da hoc
    private boolean isImageOnly;    // Chi chua the co hinh
}
```

### Dac diem:
- Nhom cac flashcard theo chu de
- Theo doi tien do hoc cua tung chu de
- Co loai dac biet chi chua cau hoi co hinh

## 5.4. Class ExamSet

```java
public class ExamSet {
    private int id;
    private String name;            // Ten de thi
    private String description;     // Mo ta
    private int questionCount;      // So cau hoi
    private boolean isOfficial;     // De chinh thuc/luyen tap
    private String category;        // Danh muc
}
```

## 5.5. Class ExamHistory

```java
public class ExamHistory {
    private int id;
    private int examSetId;          // ID de thi
    private String examName;        // Ten lan thi
    private int totalQuestions;     // Tong so cau
    private int correctAnswers;     // So cau dung
    private int wrongAnswers;       // So cau sai
    private int score;              // Diem (%)
    private boolean isPassed;       // Dat/Khong dat
    private long testDate;          // Ngay thi
    private int durationMinutes;    // Thoi gian lam bai
    private String answersJson;     // Chi tiet cau tra loi (JSON)
}
```

### Dac diem:
- Luu tru toan bo thong tin lan thi
- `answersJson` luu chi tiet tung cau tra loi
- Tinh toan ket qua DAT/TRUOT

## 5.6. Class UserAnswer

```java
public class UserAnswer {
    private int questionId;         // ID cau hoi
    private String selectedAnswer;  // Dap an da chon
    private String correctAnswer;   // Dap an dung
    private boolean markedForReview;// Danh dau xem lai
}
```

## 5.7. Class PomodoroSession

```java
public class PomodoroSession {
    private int workDuration;       // Thoi gian lam viec (phut)
    private int shortBreakDuration; // Nghi ngan (phut)
    private int longBreakDuration;  // Nghi dai (phut)
    private int totalCycles;        // Tong so chu ky
}
```

---

# 6. CO SO DU LIEU SQLITE

## 6.1. Tong quan

- **Ten database**: `learning_app.db`
- **Phien ban**: 13
- **So bang**: 5

## 6.2. Cau truc cac bang

### 6.2.1. Bang `flashcard_topics`

```sql
CREATE TABLE flashcard_topics (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    total_cards INTEGER DEFAULT 0,
    learned_cards INTEGER DEFAULT 0,
    is_image_only INTEGER DEFAULT 0
);
```

### 6.2.2. Bang `flashcards`

```sql
CREATE TABLE flashcards (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    front TEXT NOT NULL,
    back TEXT NOT NULL,
    image_path TEXT,
    topic_id INTEGER,
    is_learned INTEGER DEFAULT 0,
    review_count INTEGER DEFAULT 0,
    last_review_time INTEGER,
    FOREIGN KEY(topic_id) REFERENCES flashcard_topics(id)
);
```

### 6.2.3. Bang `exam_sets`

```sql
CREATE TABLE exam_sets (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    question_count INTEGER DEFAULT 0,
    is_official INTEGER DEFAULT 0,
    category TEXT
);
```

### 6.2.4. Bang `questions`

```sql
CREATE TABLE questions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    question_text TEXT NOT NULL,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT,
    option_d TEXT,
    correct_answer TEXT NOT NULL,
    image_path TEXT,
    is_liet INTEGER DEFAULT 0,
    exam_set_id INTEGER,
    FOREIGN KEY(exam_set_id) REFERENCES exam_sets(id)
);
```

### 6.2.5. Bang `exam_history`

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

## 6.3. So do quan he

```
+------------------+       +------------------+
|  exam_sets       |       | flashcard_topics |
+------------------+       +------------------+
| id (PK)          |       | id (PK)          |
| name             |       | name             |
| description      |       | description      |
| question_count   |       | total_cards      |
| is_official      |       | learned_cards    |
| category         |       | is_image_only    |
+------------------+       +------------------+
        |                          |
        | 1:N                      | 1:N
        v                          v
+------------------+       +------------------+
|    questions     |       |   flashcards     |
+------------------+       +------------------+
| id (PK)          |       | id (PK)          |
| question_text    |       | front            |
| option_a         |       | back             |
| option_b         |       | image_path       |
| option_c         |       | topic_id (FK)    |
| option_d         |       | is_learned       |
| correct_answer   |       | review_count     |
| image_path       |       | last_review_time |
| is_liet          |       +------------------+
| exam_set_id (FK) |
+------------------+
        |
        | 1:N
        v
+------------------+
|   exam_history   |
+------------------+
| id (PK)          |
| exam_set_id (FK) |
| exam_name        |
| total_questions  |
| correct_answers  |
| wrong_answers    |
| score            |
| is_passed        |
| test_date        |
| duration_minutes |
| answers_json     |
+------------------+
```

## 6.4. DatabaseHelper - Cac phuong thuc chinh

### 6.4.1. Khoi tao va nang cap

```java
@Override
public void onCreate(SQLiteDatabase db) {
    // Tao cac bang
    db.execSQL(createFlashcardTopicsTable);
    db.execSQL(createFlashcardsTable);
    db.execSQL(createExamSetsTable);
    db.execSQL(createQuestionsTable);
    db.execSQL(createExamHistoryTable);
    
    // Import du lieu tu JSON
    JsonImporter.importQuestionsFromAssets(context, db, "A1_250Q_with_images.json");
    JsonImporter.importFlashcardsFromAssets(context, db, "A1_250Q_with_images.json");
}
```

### 6.4.2. Cac phuong thuc truy van

| Phuong thuc | Mo ta |
|-------------|-------|
| `getAllFlashcardTopics()` | Lay tat ca chu de flashcard |
| `getFlashcardsByTopic(int topicId)` | Lay flashcard theo chu de |
| `getAllExamSets()` | Lay tat ca de thi |
| `getQuestionsByExamSet(int examSetId)` | Lay cau hoi theo de thi |
| `getRandomQuestionsForTest(int examSetId, int totalQuestions)` | Lay cau hoi ngau nhien cho bai thi |
| `getAllExamHistory()` | Lay lich su thi |

### 6.4.3. Cac phuong thuc cap nhat

| Phuong thuc | Mo ta |
|-------------|-------|
| `updateFlashcardLearned(int id, boolean isLearned)` | Cap nhat trang thai flashcard |
| `saveExamHistory(ExamHistory history)` | Luu lich su thi |

---

# 7. HE THONG DIEU HUONG

## 7.1. Navigation Graph

Ung dung su dung **Navigation Component** voi mot Navigation Graph duy nhat (`nav_graph.xml`).

### 7.1.1. Cau truc Navigation Graph

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_graph"
    app:startDestination="@id/homeFragment">
    
    <!-- Cac fragment va action -->
    
</navigation>
```

### 7.1.2. Start Destination

- **HomeFragment** la man hinh khoi dong
- Tu HomeFragment co the di den 6 man hinh chinh

## 7.2. So do dieu huong

```
                    +---------------+
                    | HomeFragment  |
                    +---------------+
                           |
    +---------+---------+--+--+---------+---------+
    |         |         |     |         |         |
    v         v         v     v         v         v
+-------+ +-------+ +------+ +-------+ +-------+ +-------+
|Flash  | |Pomo   | |Exam  | |Pract  | |Hist   | |Sett   |
|cardHub| |doroHub| |List  | |iceTest| |ory    | |ings   |
+-------+ +-------+ +------+ |Config | +-------+ +-------+
    |         |         |    +-------+     |
    v         v         v        |         v
+-------+ +-------+ +------+     v    +-------+
|Flash  | |Pomo   | |Exam  | +-------+|History|
|cardSes| |doroSes| |Detail| |Practi | |Detail |
|sion   | |sion   | +------+ |ceTest | +-------+
+-------+ +-------+     |    +-------+
                        v        |
                   +--------+    v
                   |Question|+-------+
                   |Detail  ||TestRe |
                   +--------+|sult   |
                             +-------+
                                  |
                                  v
                             +-------+
                             |Review |
                             |Mistakes|
                             +-------+
```

## 7.3. Animations

Ung dung su dung slide animations cho cac chuyen doi:

```xml
<!-- Enter: slide tu phai -->
<anim/slide_in_right.xml>

<!-- Exit: slide ra trai -->
<anim/slide_out_left.xml>

<!-- Pop Enter: slide tu trai -->
<anim/slide_in_left.xml>

<!-- Pop Exit: slide ra phai -->
<anim/slide_out_right.xml>
```

### Cau hinh animation trong action:

```xml
<action
    android:id="@+id/action_homeFragment_to_flashcardHubFragment"
    app:destination="@id/flashcardHubFragment"
    app:enterAnim="@anim/slide_in_right"
    app:exitAnim="@anim/slide_out_left"
    app:popEnterAnim="@anim/slide_in_left"
    app:popExitAnim="@anim/slide_out_right" />
```

## 7.4. Truyen du lieu giua Fragments

### Su dung Bundle:

```java
// Gui du lieu
Bundle args = new Bundle();
args.putInt("topic_id", topicId);
args.putBoolean("is_image_only", isImageOnly);
navController.navigate(R.id.action_to_destination, args);

// Nhan du lieu
Bundle args = getArguments();
if (args != null) {
    int topicId = args.getInt("topic_id", -1);
    boolean isImageOnly = args.getBoolean("is_image_only", false);
}
```

---

# 8. TINH NANG FLASHCARD

## 8.1. Tong quan

Tinh nang Flashcard cho phep nguoi dung hoc cau hoi theo phuong phap the ghi nho. Nguoi dung xem cau hoi o mat truoc, sau do lat the de xem dap an o mat sau.

## 8.2. Cau truc

### 8.2.1. FlashcardHubFragment

```
+----------------------------------+
|         Flashcard Hub            |
+----------------------------------+
| +------------------------------+ |
| |   Chu de: Bo 250 cau A1     | |
| |   Tien do: 0/250            | |
| |   [ Bat dau hoc ]           | |
| +------------------------------+ |
| +------------------------------+ |
| |   Chu de: Cau hoi co hinh   | |
| |   Tien do: 0/80             | |
| |   [ Bat dau hoc ]           | |
| +------------------------------+ |
+----------------------------------+
```

**Chuc nang:**
- Hien thi danh sach chu de flashcard
- Hien thi tien do hoc cua tung chu de
- Dieu huong den FlashcardSessionFragment

### 8.2.2. FlashcardSessionFragment

```
+----------------------------------+
|           1/250                  |
+----------------------------------+
|  +----------------------------+  |
|  |                            |  |
|  |     [Hinh anh cau hoi]     |  |
|  |                            |  |
|  |  Cau hoi: ...?             |  |
|  |                            |  |
|  |  A. ...                    |  |
|  |  B. ...                    |  |
|  |  C. ...                    |  |
|  |  D. ...                    |  |
|  |                            |  |
|  |     (Cham de lat the)      |  |
|  +----------------------------+  |
|                                  |
|  [ Chua ro ]      [ Nho roi ]   |
+----------------------------------+
```

**Chuc nang:**
- Hien thi flashcard voi mat truoc (cau hoi + lua chon)
- Lat the de xem mat sau (dap an dung)
- Danh dau "Nho roi" hoac "Chua ro"
- Tron ngau nhien thu tu the

## 8.3. Luong hoat dong

```
1. Nguoi dung chon chu de
         |
         v
2. Lay danh sach flashcard tu database
         |
         v
3. Tron ngau nhien thu tu
         |
         v
4. Hien thi the dau tien (mat truoc)
         |
         v
5. Nguoi dung cham de lat the
         |
         v
6. Hien thi mat sau (dap an dung)
         |
         v
7. Nguoi dung chon "Nho roi" / "Chua ro"
         |
         v
8. Cap nhat trang thai vao database
         |
         v
9. Chuyen sang the tiep theo
         |
         v
10. Lap lai cho den het
         |
         v
11. Hien thong bao hoan thanh
```

## 8.4. Xu ly hinh anh

```java
// Hien thi hinh anh tren flashcard
ImageHelper.loadFlashcardImage(requireContext(), ivFlashcardImage, card.getImagePath());

// Khi lat the, an hinh anh
ivFlashcardImage.setVisibility(View.GONE);
```

---

# 9. TINH NANG POMODORO TIMER

## 9.1. Tong quan

Pomodoro la ky thuat quan ly thoi gian, chia thoi gian lam viec thanh cac khoang ngan (thuong 25 phut), xen ke voi cac khoang nghi ngan.

## 9.2. Cau hinh mac dinh

| Thong so | Gia tri mac dinh |
|----------|------------------|
| Thoi gian lam viec | 25 phut |
| Nghi ngan | 5 phut |
| Nghi dai | 15 phut |
| So chu ky | 4 |

## 9.3. Cau truc

### 9.3.1. PomodoroHubFragment

```
+----------------------------------+
|       Pomodoro Timer             |
+----------------------------------+
|                                  |
|  [ Bat dau voi Preset 25/5/4 ]   |
|                                  |
|  --- Hoac tuy chinh ---          |
|                                  |
|  Thoi gian lam viec: [ 25 ] phut |
|  Nghi ngan:          [ 5  ] phut |
|  Nghi dai:           [ 15 ] phut |
|  So chu ky:          [ 4  ]      |
|                                  |
|  [ Bat dau voi cai dat tuy chinh]|
|                                  |
+----------------------------------+
```

### 9.3.2. PomodoroSessionFragment

```
+----------------------------------+
|          Lam viec                |
|        Chu ky 1/4                |
+----------------------------------+
|                                  |
|            25:00                 |
|                                  |
+----------------------------------+
|                                  |
|  [ Tam dung ]    [ Ket thuc ]    |
|                                  |
+----------------------------------+
```

## 9.4. Luong hoat dong

```
1. Nguoi dung chon preset hoac tuy chinh
         |
         v
2. Bat dau phien lam viec (25 phut)
         |
         v
3. Dem nguoc thoi gian
         |
         v
4. Het gio lam viec -> Thong bao
         |
         v
5. Bat dau nghi ngan (5 phut)
         |
         v
6. Het gio nghi -> Chuyen sang chu ky tiep
         |
         v
7. Lap lai (4 chu ky)
         |
         v
8. Sau chu ky cuoi -> Nghi dai (15 phut)
         |
         v
9. Hoan thanh -> Quay ve man hinh chinh
```

## 9.5. CountDownTimer

```java
private void startTimer() {
    countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            timeLeftInMillis = millisUntilFinished;
            updateTimer();
        }
        
        @Override
        public void onFinish() {
            onTimerFinished();
        }
    }.start();
}
```

## 9.6. Chuyen trang thai

```java
private void onTimerFinished() {
    if (isWorkTime) {
        // Het gio lam viec -> Nghi
        isWorkTime = false;
        if (currentCycle == session.getTotalCycles()) {
            // Nghi dai sau chu ky cuoi
            timeLeftInMillis = session.getLongBreakDuration() * 60 * 1000L;
        } else {
            // Nghi ngan
            timeLeftInMillis = session.getShortBreakDuration() * 60 * 1000L;
        }
    } else {
        // Het gio nghi -> Lam viec
        isWorkTime = true;
        currentCycle++;
        
        if (currentCycle > session.getTotalCycles()) {
            // Hoan thanh tat ca chu ky
            return;
        }
        
        timeLeftInMillis = session.getWorkDuration() * 60 * 1000L;
    }
}
```

---

# 10. TINH NANG THI THU

## 10.1. Tong quan

Tinh nang Thi thu mo phong bai thi sat hach ly thuyet lai xe A1, voi:
- Gioi han thoi gian
- Cham diem tu dong
- Xac dinh dau/truot
- Xem lai cau sai

## 10.2. Quy tac cham diem

| Tieu chi | Yeu cau |
|----------|---------|
| Tong so cau | 25-30 cau |
| Thoi gian | 19-45 phut |
| So cau sai toi da | 3 cau |
| Cau liet | Sai 1 cau = Truot |

## 10.3. Cau truc

### 10.3.1. PracticeTestConfigFragment

```
+----------------------------------+
|      Cau hinh thi thu            |
+----------------------------------+
|                                  |
|  So cau hoi:                     |
|  ( ) 25 cau                      |
|  (*) 30 cau                      |
|                                  |
|  Thoi gian:                      |
|  ( ) 19 phut                     |
|  (*) 30 phut                     |
|  ( ) 45 phut                     |
|                                  |
|        [ Bat dau thi ]           |
|                                  |
+----------------------------------+
```

### 10.3.2. PracticeTestFragment

```
+----------------------------------+
|  05:30          8/30   [Tong quan]|
+----------------------------------+
|           Cau 8                  |
+----------------------------------+
|  [Hinh anh cau hoi]              |
|                                  |
|  Noi dung cau hoi...?            |
|                                  |
|  ( ) A. Lua chon A               |
|  (*) B. Lua chon B               |
|  ( ) C. Lua chon C               |
|  ( ) D. Lua chon D               |
|                                  |
|  [ ] Danh dau xem lai            |
|                                  |
+----------------------------------+
|  [<< Truoc]            [Sau >>]  |
+----------------------------------+
```

### 10.3.3. Dialog Tong quan cau hoi

```
+----------------------------------+
|       Tong quan cau hoi          |
+----------------------------------+
|  +--+--+--+--+--+                |
|  | 1| 2| 3| 4| 5|                |
|  +--+--+--+--+--+                |
|  | 6| 7| 8| 9|10|                |
|  +--+--+--+--+--+                |
|  |11|12|13|14|15|                |
|  +--+--+--+--+--+                |
|  ...                             |
|                                  |
|  Chu thich:                      |
|  [ ] Chua lam                    |
|  [x] Da tra loi                  |
|  [!] Danh dau xem lai            |
+----------------------------------+
```

### 10.3.4. TestResultFragment

```
+----------------------------------+
|          Ket qua                 |
+----------------------------------+
|                                  |
|           27/30                  |
|                                  |
|            DAU                   |
|                                  |
|  Cau dung: 27                    |
|  Cau sai:  3                     |
|                                  |
+----------------------------------+
|  [ Xem cau sai ]                 |
|  [ Xem tat ca dap an ]           |
|  [ Hoan thanh ]                  |
+----------------------------------+
```

## 10.4. Luong hoat dong

```
1. Nguoi dung cau hinh bai thi
         |
         v
2. Lay cau hoi ngau nhien tu database
   (30% cau co hinh, 70% cau thuong)
         |
         v
3. Bat dau dem gio
         |
         v
4. Nguoi dung tra loi tung cau
         |
         v
5. Co the danh dau cau de xem lai
         |
         v
6. Nop bai (tu dong khi het gio)
         |
         v
7. Tinh diem va ket qua
         |
         v
8. Luu vao lich su
         |
         v
9. Hien thi ket qua
         |
         v
10. Xem lai cau sai (tuy chon)
```

## 10.5. Lay cau hoi ngau nhien

```java
public List<Question> getRandomQuestionsForTest(int examSetId, int totalQuestions) {
    // Phan loai cau hoi
    List<Question> questionsWithImage = new ArrayList<>();
    List<Question> questionsWithoutImage = new ArrayList<>();
    
    // Tinh ty le: 30% cau co hinh, 70% cau thuong
    int numImageQuestions = (int) Math.round(totalQuestions * 0.3);
    int numNormalQuestions = totalQuestions - numImageQuestions;
    
    // Tron ngau nhien va lay cau hoi
    Collections.shuffle(questionsWithoutImage);
    Collections.shuffle(questionsWithImage);
    
    // Gop va tron lai
    List<Question> selectedQuestions = new ArrayList<>();
    // ... them cau hoi
    Collections.shuffle(selectedQuestions);
    
    return selectedQuestions;
}
```

## 10.6. Tinh ket qua

```java
private void calculateResults() {
    for (int i = 0; i < selectedAnswers.size(); i++) {
        String selected = selectedAnswers.get(i);
        String correct = correctAnswersList.get(i);
        boolean isLiet = isLietList.get(i);
        
        if (selected != null && selected.equals(correct)) {
            correctAnswers++;
        } else {
            wrongAnswers++;
            if (isLiet) {
                failedDueToLiet = true;  // Sai cau liet = Truot
            }
        }
    }
}

// Xac dinh dau/truot
boolean isPassed = wrongAnswers < 4 && !failedDueToLiet;
```

---

# 11. TINH NANG XEM DE THI

## 11.1. Tong quan

Tinh nang cho phep nguoi dung duyet qua tat ca cau hoi trong de thi ma khong can lam bai. Huu ich de hoc va on tap.

## 11.2. Cau truc

### 11.2.1. ExamListFragment

```
+----------------------------------+
|        Danh sach de thi          |
+----------------------------------+
| +------------------------------+ |
| | Bo 250 cau A1                | |
| | 250 cau hoi                  | |
| | [Xem chi tiet]               | |
| +------------------------------+ |
+----------------------------------+
```

### 11.2.2. ExamDetailFragment

```
+----------------------------------+
|        Bo 250 cau A1             |
+----------------------------------+
| +------------------------------+ |
| | Cau 1         [LIET]         | |
| | Noi dung cau hoi...          | |
| | A. ... B. ... C. ... D. ...  | |
| | Dap an dung: A               | |
| +------------------------------+ |
| +------------------------------+ |
| | Cau 2                        | |
| | [Hinh anh]                   | |
| | Noi dung cau hoi...          | |
| | ...                          | |
| +------------------------------+ |
| ...                              |
+----------------------------------+
```

### 11.2.3. QuestionDetailFragment

```
+----------------------------------+
|        Chi tiet cau hoi          |
+----------------------------------+
|                                  |
|  [Hinh anh cau hoi]              |
|                                  |
|  Cau hoi: ...?                   |
|                                  |
|  A. Lua chon A                   |
|  B. Lua chon B  (highlighted)    |
|  C. Lua chon C                   |
|  D. Lua chon D                   |
|                                  |
|  Dap an dung: B                  |
|                                  |
|  [LIET] Day la cau diem liet     |
|                                  |
+----------------------------------+
```

## 11.3. Hien thi dap an dung

```java
// Highlight dap an dung bang mau xanh
switch (question.getCorrectAnswer()) {
    case "A": 
        tvOptionA.setBackgroundColor(Color.parseColor("#C8E6C9")); 
        break;
    case "B": 
        tvOptionB.setBackgroundColor(Color.parseColor("#C8E6C9")); 
        break;
    // ...
}
```

## 11.4. Hien thi badge LIET

```java
if (question.isLiet()) {
    tvLietBadge.setVisibility(View.VISIBLE);
} else {
    tvLietBadge.setVisibility(View.GONE);
}
```

---

# 12. TINH NANG LICH SU

## 12.1. Tong quan

Tinh nang luu tru va hien thi lich su cac lan thi truoc, cho phep nguoi dung theo doi tien do va on lai.

## 12.2. Cau truc

### 12.2.1. HistoryFragment

```
+----------------------------------+
|          Lich su thi             |
+----------------------------------+
| +------------------------------+ |
| | De thi thu (30 cau)          | |
| | 22/12/2025 10:30             | |
| | Ket qua: 28/30 - DAU         | |
| | [Xem chi tiet]               | |
| +------------------------------+ |
| +------------------------------+ |
| | De thi thu (25 cau)          | |
| | 21/12/2025 14:15             | |
| | Ket qua: 20/25 - TRUOT       | |
| | [Xem chi tiet]               | |
| +------------------------------+ |
+----------------------------------+
```

### 12.2.2. HistoryDetailFragment

```
+----------------------------------+
|      Chi tiet lan thi            |
+----------------------------------+
|                                  |
|  Ten de: De thi thu (30 cau)     |
|  Ngay thi: 22/12/2025 10:30      |
|  Thoi gian: 25 phut              |
|                                  |
|  Ket qua: DAU                    |
|  Diem: 93% (28/30)               |
|                                  |
|  Cau dung: 28                    |
|  Cau sai: 2                      |
|                                  |
|  [ Xem chi tiet cau tra loi ]    |
|                                  |
+----------------------------------+
```

## 12.3. Luu lich su

```java
private void saveToHistory() {
    ExamHistory history = new ExamHistory();
    history.setExamSetId(examSetId);
    history.setExamName(examName);
    history.setTotalQuestions(totalQuestions);
    history.setCorrectAnswers(correctAnswers);
    history.setWrongAnswers(wrongAnswers);
    history.setScore((correctAnswers * 100) / totalQuestions);
    history.setPassed(wrongAnswers < 4 && !failedDueToLiet);
    history.setTestDate(System.currentTimeMillis());
    history.setDurationMinutes(durationMinutes);
    
    // Luu chi tiet cau tra loi dang JSON
    JSONArray answersArray = new JSONArray();
    for (int i = 0; i < questionIds.size(); i++) {
        JSONObject answerObj = new JSONObject();
        answerObj.put("question_id", questionIds.get(i));
        answerObj.put("selected", selectedAnswers.get(i));
        answerObj.put("correct", correctAnswersList.get(i));
        answersArray.put(answerObj);
    }
    history.setAnswersJson(answersArray.toString());
    
    databaseHelper.saveExamHistory(history);
}
```

---

# 13. TINH NANG CAI DAT

## 13.1. Tong quan

Tinh nang cho phep nguoi dung tuy chinh ung dung theo nhu cau.

## 13.2. Cac tuy chon

| Tuy chon | Mo ta |
|----------|-------|
| Thong bao | Bat/Tat thong bao nhac nho |
| Thoi gian nhac | Chon gio nhan thong bao |
| Muc tieu hang ngay | So cau hoi can hoc moi ngay |

## 13.3. PreferencesHelper

```java
public class PreferencesHelper {
    private static final String PREF_NAME = "LearningAppPrefs";
    
    private static final String KEY_ONBOARDING_COMPLETED = "onboarding_completed";
    private static final String KEY_SELECTED_GOAL = "selected_goal";
    private static final String KEY_NOTIFICATION_ENABLED = "notification_enabled";
    private static final String KEY_NOTIFICATION_TIME = "notification_time";
    private static final String KEY_DAILY_GOAL = "daily_goal";
    
    // Getter va Setter methods
    public boolean isNotificationEnabled() {
        return preferences.getBoolean(KEY_NOTIFICATION_ENABLED, false);
    }
    
    public void setNotificationEnabled(boolean enabled) {
        preferences.edit()
            .putBoolean(KEY_NOTIFICATION_ENABLED, enabled)
            .apply();
    }
    
    // ...
}
```

---

# 14. HE THONG XU LY HINH ANH

## 14.1. Tong quan

Ung dung co 80 hinh anh minh hoa cho cac cau hoi. Hinh anh duoc luu trong thu muc `assets/A1_250Q_images/`.

## 14.2. Dinh dang hinh anh

| Dinh dang | So luong |
|-----------|----------|
| JPEG | 58 |
| PNG | 22 |
| **Tong** | **80** |

## 14.3. ImageHelper

```java
public class ImageHelper {
    
    public static void loadQuestionImage(Context context, ImageView imageView, String imagePath) {
        if (imagePath != null && !imagePath.isEmpty() && !imagePath.equals("null")) {
            try {
                // Tao duong dan day du
                String fullPath = "A1_250Q_images/" + imagePath;
                
                // Mo file tu assets
                InputStream inputStream = context.getAssets().open(fullPath);
                
                // Giai ma thanh Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
                
                inputStream.close();
            } catch (IOException e) {
                imageView.setVisibility(View.GONE);
            }
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
    
    public static void loadFlashcardImage(Context context, ImageView imageView, String imagePath) {
        loadQuestionImage(context, imageView, imagePath);
    }
    
    public static boolean hasImage(String imagePath) {
        return imagePath != null && !imagePath.isEmpty();
    }
}
```

## 14.4. Luong xu ly hinh anh

```
1. Kiem tra imagePath co hop le khong
         |
         v
2. Tao duong dan day du (A1_250Q_images/ + imagePath)
         |
         v
3. Mo InputStream tu assets
         |
         v
4. Giai ma thanh Bitmap
         |
         v
5. Gan vao ImageView
         |
         v
6. Dong InputStream
```

---

# 15. GIAO DIEN NGUOI DUNG

## 15.1. Theme va Style

### 15.1.1. Theme chinh

```xml
<style name="Theme.LearningApp" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
    <item name="colorPrimary">@color/primary</item>         <!-- #1976D2 -->
    <item name="colorPrimaryDark">@color/primary_dark</item> <!-- #1565C0 -->
    <item name="colorAccent">@color/accent</item>           <!-- #FF4081 -->
    <item name="android:windowBackground">@color/background</item>
</style>
```

### 15.1.2. Bang mau

| Ten | Ma mau | Su dung |
|-----|--------|---------|
| primary | #1976D2 | Mau chinh (xanh duong) |
| primary_dark | #1565C0 | ActionBar |
| accent | #FF4081 | Mau nhan (hong) |
| success | #4CAF50 | Dap an dung, Dau |
| error | #F44336 | Dap an sai, Truot |
| background | #FAFAFA | Nen ung dung |
| card_background | #FFFFFF | Nen card |
| pomodoro_work | #D32F2F | Pomodoro - Lam viec |
| pomodoro_break | #388E3C | Pomodoro - Nghi |

### 15.1.3. Typography

```xml
<style name="TitleText">
    <item name="android:textSize">24sp</item>
    <item name="android:textStyle">bold</item>
</style>

<style name="SubtitleText">
    <item name="android:textSize">18sp</item>
</style>

<style name="BodyText">
    <item name="android:textSize">16sp</item>
</style>

<style name="CaptionText">
    <item name="android:textSize">14sp</item>
    <item name="android:textColor">@color/text_secondary</item>
</style>
```

## 15.2. CardStyle

```xml
<style name="CardStyle">
    <item name="android:layout_margin">8dp</item>
    <item name="cardCornerRadius">8dp</item>
    <item name="cardElevation">4dp</item>
    <item name="android:background">@color/card_background</item>
</style>
```

## 15.3. Button Styles

```xml
<style name="ButtonPrimary" parent="Widget.MaterialComponents.Button">
    <item name="android:textColor">@color/text_white</item>
    <item name="backgroundTint">@color/primary</item>
    <item name="cornerRadius">8dp</item>
</style>

<style name="ButtonOutlined" parent="Widget.MaterialComponents.Button.OutlinedButton">
    <item name="android:textColor">@color/primary</item>
    <item name="strokeColor">@color/primary</item>
    <item name="cornerRadius">8dp</item>
</style>
```

## 15.4. Layout chinh

### 15.4.1. Home Screen

```
+----------------------------------+
|  Header (Primary Color)          |
|  - Title: "Trang chu"            |
|  - Subtitle: Chao mung           |
+----------------------------------+
|  ScrollView                      |
|  +----------------------------+  |
|  |     Card: Flashcard        |  |
|  |     "Hoc voi the ghi nho"  |  |
|  +----------------------------+  |
|  +----------------------------+  |
|  |     Card: Pomodoro         |  |
|  |     "Quan ly thoi gian"    |  |
|  +----------------------------+  |
|  +----------------------------+  |
|  |     Card: Xem de thi       |  |
|  |     "Xem cau hoi"          |  |
|  +----------------------------+  |
|  +----------------------------+  |
|  |     Card: Thi thu          |  |
|  |     "Luyen tap"            |  |
|  +----------------------------+  |
|  +----------------------------+  |
|  |     Card: Lich su          |  |
|  |     "Xem lai ket qua"      |  |
|  +----------------------------+  |
|  +----------------------------+  |
|  |     Card: Cai dat          |  |
|  |     "Cau hinh ung dung"    |  |
|  +----------------------------+  |
+----------------------------------+
```

---

# 16. LUONG HOAT DONG

## 16.1. Luong khoi dong ung dung

```
1. Nguoi dung mo ung dung
         |
         v
2. MainActivity.onCreate()
         |
         v
3. Load NavHostFragment
         |
         v
4. Navigate to HomeFragment
         |
         v
5. Hien thi man hinh chinh
```

## 16.2. Luong import du lieu

```
1. DatabaseHelper.onCreate() duoc goi (lan dau cai dat)
         |
         v
2. Tao cac bang trong SQLite
         |
         v
3. Goi JsonImporter.importQuestionsFromAssets()
         |
         v
4. Doc file JSON tu assets
         |
         v
5. Parse JSON thanh cac doi tuong
         |
         v
6. Insert vao bang exam_sets va questions
         |
         v
7. Goi JsonImporter.importFlashcardsFromAssets()
         |
         v
8. Tao flashcard_topics va flashcards
         |
         v
9. Tao topic dac biet "Cau hoi co hinh"
         |
         v
10. Hoan thanh import
```

## 16.3. Luong lam bai thi thu

```
1. Nguoi dung chon "Thi thu" tu Home
         |
         v
2. Hien thi PracticeTestConfigFragment
         |
         v
3. Nguoi dung cau hinh (so cau, thoi gian)
         |
         v
4. Nhan "Bat dau thi"
         |
         v
5. Lay cau hoi ngau nhien tu database
         |
         v
6. Hien thi PracticeTestFragment
         |
         v
7. Bat dau dem gio (CountDownTimer)
         |
         v
8. Nguoi dung tra loi tung cau
         |
         +<----------------+
         |                 |
9. Chuyen cau (Next/Previous)
         |                 |
         +-----------------+
         |
         v
10. Nop bai (hoac het gio)
         |
         v
11. Huy CountDownTimer
         |
         v
12. Tinh diem va ket qua
         |
         v
13. Navigate to TestResultFragment
         |
         v
14. Luu vao lich su
         |
         v
15. Hien thi ket qua
         |
         v
16. (Tuy chon) Xem lai cau sai
         |
         v
17. Quay ve Home
```

## 16.4. Luong hoc Flashcard

```
1. Nguoi dung chon "Flashcard" tu Home
         |
         v
2. Hien thi FlashcardHubFragment
         |
         v
3. Lay danh sach topics tu database
         |
         v
4. Nguoi dung chon 1 topic
         |
         v
5. Lay flashcards theo topic
         |
         v
6. Tron ngau nhien
         |
         v
7. Hien thi FlashcardSessionFragment
         |
         v
8. Hien thi the dau tien (mat truoc)
         |
         +<----------------------+
         |                       |
9. Nguoi dung cham de lat the   |
         |                       |
         v                       |
10. Hien thi mat sau (dap an)   |
         |                       |
         v                       |
11. Nguoi dung chon:            |
    - "Nho roi" -> isLearned=true
    - "Chua ro" -> isLearned=false
         |                       |
         v                       |
12. Cap nhat database           |
         |                       |
         v                       |
13. Chuyen the tiep theo --------+
         |
         v
14. Het the -> Thong bao hoan thanh
         |
         v
15. Quay ve FlashcardHubFragment
```

---

# 17. TONG KET VA DANH GIA

## 17.1. Diem manh cua he thong

### 17.1.1. Kien truc

- **Single-Activity Architecture**: De quan ly, hieu suat tot
- **Navigation Component**: Dieu huong ro rang, type-safe
- **Fragment-based UI**: Linh hoat, tai su dung duoc

### 17.1.2. Database

- **SQLite**: Nhe, khong can thu vien ben ngoai
- **Import tu JSON**: De cap nhat noi dung
- **Quan he ro rang**: Foreign key dam bao toan ven du lieu

### 17.1.3. UX/UI

- **Material Design**: Giao dien hien dai, nhat quan
- **Animations**: Trai nghiem muot ma
- **Responsive**: Tuong thich nhieu kich co man hinh

### 17.1.4. Tinh nang

- **Da phuong phap hoc**: Flashcard, Thi thu, Xem de thi
- **Theo doi tien do**: Lich su, trang thai flashcard
- **Quan ly thoi gian**: Pomodoro Timer

## 17.2. Han che

### 17.2.1. Ky thuat

- Chua su dung Room (ORM) cho database
- Chua co ViewModel/LiveData (MVVM)
- Chua co Dependency Injection

### 17.2.2. Tinh nang

- Chua co dong bo du lieu len cloud
- Chua co che do offline hoan chinh
- Chua co thong ke chi tiet

## 17.3. Huong phat trien

### 17.3.1. Ngan han

- Them bieu do thong ke ket qua
- Them tinh nang nhac nho hoc tap
- Toi uu hieu suat tai hinh anh

### 17.3.2. Dai han

- Chuyen sang Kotlin
- Ap dung MVVM + Clean Architecture
- Them dong bo Firebase
- Ho tro nhieu loai bang lai (A2, B1, B2)

## 17.4. Thong tin ky thuat tong hop

| Thong so | Gia tri |
|----------|---------|
| Ngon ngu | Java 8 |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |
| So Activity | 1 |
| So Fragment | 15 |
| So Adapter | 6 |
| So Model | 7 |
| So bang database | 5 |
| So file layout | 23 |
| So cau hoi | 250 |
| So hinh anh | 80 |

---

## TAI LIEU THAM KHAO

1. Android Developers Documentation - https://developer.android.com
2. Material Design Guidelines - https://material.io
3. Navigation Component Guide - https://developer.android.com/guide/navigation
4. SQLite Documentation - https://sqlite.org/docs.html

---

**Document Version**: 1.0  
**Last Updated**: December 22, 2025  
**Author**: Learning App Development Team

---

*Tai lieu nay mo ta chi tiet ve cong nghe, thiet ke va cach hoat dong cua ung dung Learning App - Ung dung on thi ly thuyet lai xe hang A1.*

