import json
import os
import re
from pathlib import Path
import sys

try:
    import fitz  # PyMuPDF
except ImportError:
    print("ERROR: Missing PyMuPDF library!")
    print("Please install: pip install PyMuPDF")
    sys.exit(1)


class PDFQuestionExtractor:
    def __init__(self, output_image_dir="output/images"):
        self.output_image_dir = output_image_dir
        Path(output_image_dir).mkdir(parents=True, exist_ok=True)
        self.image_map = {}  # Map page_num -> list of image filenames
        
        # Danh sach cau liet (cau phai tra loi dung de dau)
        self.liet_questions = {
            19, 20, 21, 22, 24, 26, 27, 28, 30,
            47, 48, 52, 53, 63, 64, 65, 68, 70, 71, 72
        }
        
    def extract_questions(self, pdf_path):
        print(f"Opening PDF: {pdf_path}")
        doc = fitz.open(pdf_path)
        
        # Extract images with page tracking
        self.extract_images_with_pages(doc)
        
        # Parse questions with image linking
        questions = self.parse_questions_with_images(doc)
        
        doc.close()
        return questions
    
    def extract_images_with_pages(self, doc):
        """Extract images and track which page they're on"""
        print("Extracting images and tracking pages...")
        image_counter = 1
        
        for page_num in range(len(doc)):
            page = doc[page_num]
            image_list = page.get_images()
            
            if not image_list:
                continue
            
            page_images = []
            
            for img_index, img in enumerate(image_list):
                xref = img[0]
                base_image = doc.extract_image(xref)
                image_bytes = base_image["image"]
                image_ext = base_image["ext"]
                
                # Get image position on page
                img_rects = page.get_image_rects(xref)
                
                image_name = f"question_image_{image_counter}.{image_ext}"
                image_path = os.path.join(self.output_image_dir, image_name)
                
                with open(image_path, "wb") as image_file:
                    image_file.write(image_bytes)
                
                # Store image info with position
                image_info = {
                    'filename': image_name,
                    'page': page_num,
                    'position': img_rects[0] if img_rects else None
                }
                page_images.append(image_info)
                
                print(f"  Page {page_num + 1}: Extracted {image_name}")
                image_counter += 1
            
            if page_images:
                self.image_map[page_num] = page_images
        
        print(f"Total images extracted: {image_counter - 1}")
        print(f"Images distributed across {len(self.image_map)} pages")
    
    def parse_questions_with_images(self, doc):
        """Parse questions and auto-link with images on the same page"""
        print("Parsing questions and linking with images...")
        questions = []
        
        for page_num in range(len(doc)):
            page = doc[page_num]
            page_text = page.get_text()
            lines = page_text.split('\n')
            
            # Get images on this page
            page_images = self.image_map.get(page_num, [])
            current_image_index = 0
            
            # Detect underlined text for correct answers
            underlined_numbers = self.find_underlined_numbers(page)
            
            current_question = None
            question_text_builder = []
            option_number = 0
            last_question_y = 0  # Track Y position of question
            
            # Get text blocks with positions
            blocks = page.get_text("dict")["blocks"]
            
            for block in blocks:
                if block.get("type") != 0:  # Not a text block
                    continue
                
                block_text = ""
                block_y = block.get("bbox", [0, 0, 0, 0])[1]  # Y position
                
                for line in block.get("lines", []):
                    for span in line.get("spans", []):
                        block_text += span.get("text", "")
                    block_text += "\n"
                
                lines_in_block = block_text.strip().split('\n')
                
                for line in lines_in_block:
                    line = line.strip()
                    
                    if not line:
                        continue
                    
                    # Check if this is a new question
                    question_match = re.match(r'^Câu\s+(\d+)[:\.]?\s*(.+)', line, re.IGNORECASE)
                    if question_match:
                        # Save previous question
                        if current_question and current_question.get('questionText'):
                            questions.append(current_question)
                        
                        # Start new question
                        question_num = int(question_match.group(1))
                        current_question = {
                            'questionNumber': question_num,
                            'questionText': '',
                            'optionA': None,
                            'optionB': None,
                            'optionC': None,
                            'optionD': None,
                            'correctAnswer': None,
                            'imagePath': None,
                            'isLiet': question_num in self.liet_questions,
                            '_page': page_num,
                            '_y_position': block_y
                        }
                        question_text_builder = []
                        option_number = 0
                        last_question_y = block_y
                        
                        # Extract question text
                        question_text = question_match.group(2).strip()
                        if question_text:
                            question_text_builder.append(question_text)
                        
                        # Try to link with image
                        # If there's an image on this page that hasn't been assigned
                        if current_image_index < len(page_images):
                            # Check if question mentions image
                            if self.question_references_image(line):
                                current_question['imagePath'] = page_images[current_image_index]['filename']
                                current_image_index += 1
                    
                    elif current_question is not None:
                        # Check for answer options
                        option_match = re.match(r'^([1-4])[\.\)]\s*(.+)', line)
                        if option_match:
                            if question_text_builder:
                                full_question = ' '.join(question_text_builder).strip()
                                current_question['questionText'] = full_question
                                question_text_builder = []
                                
                                # If question text mentions image and no image assigned yet
                                if not current_question['imagePath'] and self.question_references_image(full_question):
                                    if current_image_index < len(page_images):
                                        current_question['imagePath'] = page_images[current_image_index]['filename']
                                        current_image_index += 1
                            
                            option_num = option_match.group(1)
                            option_text = option_match.group(2).strip()
                            
                            if option_num == '1':
                                current_question['optionA'] = option_text
                                option_number = 1
                            elif option_num == '2':
                                current_question['optionB'] = option_text
                                option_number = 2
                            elif option_num == '3':
                                current_question['optionC'] = option_text
                                option_number = 3
                            elif option_num == '4':
                                current_question['optionD'] = option_text
                                option_number = 4
                            
                            # Check if this option is underlined (correct answer)
                            if current_question['questionNumber'] in underlined_numbers:
                                if option_num == str(underlined_numbers[current_question['questionNumber']]):
                                    option_letters = {'1': 'A', '2': 'B', '3': 'C', '4': 'D'}
                                    current_question['correctAnswer'] = option_letters[option_num]
                        
                        # Continue building question text or option text
                        else:
                            if not current_question['questionText'] or question_text_builder:
                                question_text_builder.append(line)
                            elif option_number > 0:
                                option_keys = ['optionA', 'optionB', 'optionC', 'optionD']
                                if option_number <= len(option_keys):
                                    option_key = option_keys[option_number - 1]
                                    if current_question[option_key]:
                                        current_question[option_key] += ' ' + line
            
            # Add last question on this page
            if current_question and current_question.get('questionText'):
                questions.append(current_question)
        
        print(f"Total questions parsed: {len(questions)}")
        
        # Statistics
        questions_with_images = sum(1 for q in questions if q.get('imagePath'))
        print(f"Questions with linked images: {questions_with_images}")
        
        return questions
    
    def question_references_image(self, text):
        """Check if question text references an image"""
        image_keywords = [
            'hình',
            'biển báo',
            'biển',
            'sa hình',
            'dưới đây',
            'trên đây',
            'hình vẽ',
            'sơ đồ',
            'tình huống',
            'hình minh họa',
            'hình ảnh',
            'như hình',
            'theo hình'
        ]
        
        text_lower = text.lower()
        return any(keyword in text_lower for keyword in image_keywords)
    
    def find_underlined_numbers(self, page):
        """Find which option numbers are underlined on the page"""
        underlined = {}
        blocks = page.get_text("dict")["blocks"]
        current_question_num = None
        
        for block in blocks:
            if "lines" in block:
                for line in block["lines"]:
                    for span in line["spans"]:
                        text = span["text"].strip()
                        flags = span.get("flags", 0)
                        is_underlined = (flags & 4) != 0
                        
                        # Check for question number
                        q_match = re.match(r'^Câu\s+(\d+)', text)
                        if q_match:
                            current_question_num = int(q_match.group(1))
                        
                        # Check for underlined option number
                        if is_underlined and current_question_num:
                            opt_match = re.match(r'^([1-4])[\.\)]', text)
                            if opt_match:
                                underlined[current_question_num] = int(opt_match.group(1))
        
        return underlined
    
    def save_to_json(self, questions, exam_metadata, output_path):
        print(f"Saving to JSON: {output_path}")
        
        # Clean up internal fields
        for q in questions:
            if 'questionNumber' in q:
                del q['questionNumber']
            if '_page' in q:
                del q['_page']
            if '_y_position' in q:
                del q['_y_position']
        
        exam_data = {
            "examSet": exam_metadata,
            "questions": questions
        }
        
        output_dir = os.path.dirname(output_path)
        if output_dir:
            Path(output_dir).mkdir(parents=True, exist_ok=True)
        
        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump([exam_data], f, ensure_ascii=False, indent=2)
        
        print(f"Successfully saved {len(questions)} questions to {output_path}")


def main():
    print("=" * 60)
    print("   PDF Question Extractor (With Image Linking)")
    print("=" * 60)
    print()
    
    # Get PDF path
    if len(sys.argv) > 1:
        pdf_path = sys.argv[1]
    else:
        pdf_path = input("Enter PDF file path: ").strip().strip('"')
    
    if not os.path.exists(pdf_path):
        print(f"ERROR: PDF file not found: {pdf_path}")
        return
    
    # Auto-generate output paths
    pdf_name = os.path.splitext(os.path.basename(pdf_path))[0]
    image_dir = f"./output/{pdf_name}_images"
    json_path = f"./output/{pdf_name}_with_images.json"
    
    print(f"PDF file: {pdf_path}")
    print(f"Image output: {image_dir}")
    print(f"JSON output: {json_path}")
    print()
    
    # Create extractor
    extractor = PDFQuestionExtractor(image_dir)
    
    # Extract questions
    try:
        questions = extractor.extract_questions(pdf_path)
        
        if not questions:
            print("WARNING: No questions were extracted!")
            return
        
        # Auto exam metadata
        exam_metadata = {
            "name": f"Đề thi {pdf_name}",
            "description": f"{len(questions)} câu hỏi sát hạch lái xe",
            "isOfficial": True,
            "category": "A1" if "A1" in pdf_name else "B2"
        }
        
        # Save to JSON
        extractor.save_to_json(questions, exam_metadata, json_path)
        
        # Print summary
        print()
        print("=" * 60)
        print("           Extraction Complete")
        print("=" * 60)
        print(f"Total questions extracted: {len(questions)}")
        print(f"Images saved to: {image_dir}")
        print(f"JSON data saved to: {json_path}")
        print()
        
        # Statistics
        questions_with_answers = sum(1 for q in questions if q.get('correctAnswer'))
        questions_with_images = sum(1 for q in questions if q.get('imagePath'))
        liet_questions_count = sum(1 for q in questions if q.get('isLiet'))
        print(f"Questions with detected correct answer: {questions_with_answers}")
        print(f"Questions with linked images: {questions_with_images}")
        print(f"Questions without images: {len(questions) - questions_with_images}")
        print(f"Liet questions (must answer correctly): {liet_questions_count}")
        print()
        
        # Show sample questions with images
        print("Sample of questions WITH images (first 5):")
        count = 0
        for i, q in enumerate(questions):
            if q.get('imagePath') and count < 5:
                print(f"\nQuestion {i + 1}:")
                print(f"  Text: {q['questionText'][:80]}...")
                print(f"  Image: {q['imagePath']}")
                print(f"  Correct Answer: {q['correctAnswer'] or 'Not detected'}")
                count += 1
        
    except Exception as e:
        print(f"ERROR: {e}")
        import traceback
        traceback.print_exc()


if __name__ == "__main__":
    main()

