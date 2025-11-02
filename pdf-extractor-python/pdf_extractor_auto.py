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
        
    def extract_questions(self, pdf_path):
        print(f"Opening PDF: {pdf_path}")
        doc = fitz.open(pdf_path)
        
        # Extract text
        full_text = ""
        for page_num in range(len(doc)):
            page = doc[page_num]
            full_text += page.get_text()
        
        # Extract images
        self.extract_images(doc)
        
        # Parse questions with underline detection
        questions = self.parse_questions_with_formatting(doc)
        
        doc.close()
        return questions
    
    def extract_images(self, doc):
        print("Extracting images...")
        image_counter = 1
        
        for page_num in range(len(doc)):
            page = doc[page_num]
            image_list = page.get_images()
            
            for img_index, img in enumerate(image_list):
                xref = img[0]
                base_image = doc.extract_image(xref)
                image_bytes = base_image["image"]
                image_ext = base_image["ext"]
                
                image_name = f"question_image_{image_counter}.{image_ext}"
                image_path = os.path.join(self.output_image_dir, image_name)
                
                with open(image_path, "wb") as image_file:
                    image_file.write(image_bytes)
                
                image_counter += 1
        
        print(f"Total images extracted: {image_counter - 1}")
    
    def parse_questions_with_formatting(self, doc):
        """Parse questions and detect underlined text for correct answers"""
        print("Parsing questions with formatting detection...")
        questions = []
        
        for page_num in range(len(doc)):
            page = doc[page_num]
            
            # Get text with formatting
            blocks = page.get_text("dict")["blocks"]
            page_text = page.get_text()
            lines = page_text.split('\n')
            
            current_question = None
            question_text_builder = []
            option_number = 0
            
            # Check for underlined text in this page
            underlined_numbers = self.find_underlined_numbers(page)
            
            for line in lines:
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
                    question_num = question_match.group(1)
                    current_question = {
                        'questionNumber': int(question_num),
                        'questionText': '',
                        'optionA': None,
                        'optionB': None,
                        'optionC': None,
                        'optionD': None,
                        'correctAnswer': None,
                        'explanation': None,
                        'imagePath': None
                    }
                    question_text_builder = []
                    option_number = 0
                    
                    # Extract question text
                    question_text = question_match.group(2).strip()
                    if question_text:
                        question_text_builder.append(question_text)
                
                elif current_question is not None:
                    # Check for answer options
                    option_match = re.match(r'^([1-4])[\.\)]\s*(.+)', line)
                    if option_match:
                        if question_text_builder:
                            current_question['questionText'] = ' '.join(question_text_builder).strip()
                            question_text_builder = []
                        
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
                    
                    # Continue building question or option text
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
        return questions
    
    def find_underlined_numbers(self, page):
        """Find which option numbers are underlined on the page"""
        underlined = {}
        
        # Get text with formatting details
        blocks = page.get_text("dict")["blocks"]
        
        current_question_num = None
        
        for block in blocks:
            if "lines" in block:
                for line in block["lines"]:
                    for span in line["spans"]:
                        text = span["text"].strip()
                        flags = span.get("flags", 0)
                        
                        # Check if text is underlined (flag 4)
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
        
        # Remove questionNumber field before saving (it was only for tracking)
        for q in questions:
            if 'questionNumber' in q:
                del q['questionNumber']
        
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
    print("   PDF Question Extractor (Auto Mode)")
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
    json_path = f"./output/{pdf_name}.json"
    
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
        print(f"Questions with detected correct answer: {questions_with_answers}")
        print(f"Questions without correct answer: {len(questions) - questions_with_answers}")
        print()
        
        # Show sample questions
        print("Sample of extracted questions (first 3):")
        for i in range(min(3, len(questions))):
            q = questions[i]
            print(f"\nQuestion {i + 1}:")
            print(f"  Text: {q['questionText'][:80]}...")
            print(f"  A: {q['optionA'][:60] if q['optionA'] else 'None'}...")
            print(f"  B: {q['optionB'][:60] if q['optionB'] else 'None'}...")
            if q['optionC']:
                print(f"  C: {q['optionC'][:60]}...")
            if q['optionD']:
                print(f"  D: {q['optionD'][:60]}...")
            print(f"  Correct Answer: {q['correctAnswer'] or 'Not detected'}")
        
    except Exception as e:
        print(f"ERROR: {e}")
        import traceback
        traceback.print_exc()


if __name__ == "__main__":
    main()

