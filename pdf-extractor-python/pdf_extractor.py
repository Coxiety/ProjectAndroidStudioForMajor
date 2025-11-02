import json
import os
import re
from pathlib import Path
import sys

try:
    import PyPDF2
    from PIL import Image
    import fitz  # PyMuPDF
except ImportError:
    print("ERROR: Missing required libraries!")
    print("Please install them by running:")
    print("  pip install PyPDF2 PyMuPDF Pillow")
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
        
        # Parse questions
        questions = self.parse_questions(full_text)
        
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
                
                print(f"  Extracted: {image_name}")
                image_counter += 1
        
        print(f"Total images extracted: {image_counter - 1}")
    
    def parse_questions(self, text):
        print("Parsing questions from text...")
        questions = []
        lines = text.split('\n')
        
        current_question = None
        question_text_builder = []
        option_number = 0
        
        for line in lines:
            line = line.strip()
            
            if not line:
                continue
            
            # Check if this is a new question (Câu 1:, Câu 2:, etc.)
            question_match = re.match(r'^Câu\s+(\d+)[:\.]?\s*(.+)', line, re.IGNORECASE)
            if question_match:
                # Save previous question
                if current_question and current_question.get('questionText'):
                    questions.append(current_question)
                
                # Start new question
                current_question = {
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
                
                # Extract question text (after "Câu X:")
                question_text = question_match.group(2).strip()
                if question_text:
                    question_text_builder.append(question_text)
            
            elif current_question is not None:
                # Check for answer options (numbered 1., 2., 3., 4.)
                option_match = re.match(r'^([1-4])[\.\)]\s*(.+)', line)
                if option_match:
                    if question_text_builder:
                        current_question['questionText'] = ' '.join(question_text_builder).strip()
                        question_text_builder = []
                    
                    option_num = option_match.group(1)
                    option_text = option_match.group(2).strip()
                    
                    # Convert number to letter (not used but kept for reference)
                    option_letters = {'1': 'A', '2': 'B', '3': 'C', '4': 'D'}
                    
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
                
                # Check for correct answer
                elif re.match(r'(?i)Đáp\s*án\s*đúng[:\s]*[1-4A-D]', line):
                    match = re.search(r'(?i)Đáp\s*án\s*đúng[:\s]*([1-4A-D])', line)
                    if match:
                        answer = match.group(1).upper()
                        # Convert number to letter if needed
                        if answer in ['1', '2', '3', '4']:
                            answer = {'1': 'A', '2': 'B', '3': 'C', '4': 'D'}[answer]
                        current_question['correctAnswer'] = answer
                
                # Check for explanation
                elif re.match(r'(?i)Giải\s*thích[:\s]*.+', line):
                    explanation = re.sub(r'(?i)Giải\s*thích[:\s]*', '', line)
                    current_question['explanation'] = explanation
                
                # Continue building question text or option text
                else:
                    if not current_question['questionText'] or question_text_builder:
                        # Still building question text
                        question_text_builder.append(line)
                    elif option_number > 0:
                        # Continuing previous option text
                        option_keys = ['optionA', 'optionB', 'optionC', 'optionD']
                        if option_number <= len(option_keys):
                            option_key = option_keys[option_number - 1]
                            if current_question[option_key]:
                                current_question[option_key] += ' ' + line
        
        # Add last question
        if current_question and current_question.get('questionText'):
            questions.append(current_question)
        
        print(f"Total questions parsed: {len(questions)}")
        return questions
    
    def save_to_json(self, questions, exam_metadata, output_path):
        print(f"Saving to JSON: {output_path}")
        
        exam_data = {
            "examSet": exam_metadata,
            "questions": questions
        }
        
        # Create output directory if needed
        output_dir = os.path.dirname(output_path)
        if output_dir:
            Path(output_dir).mkdir(parents=True, exist_ok=True)
        
        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump([exam_data], f, ensure_ascii=False, indent=2)
        
        print(f"Successfully saved {len(questions)} questions to {output_path}")


def main():
    print("=" * 60)
    print("   PDF Question Extractor (Python Version)")
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
    
    # Get output directories
    if len(sys.argv) > 2:
        image_dir = sys.argv[2]
    else:
        image_dir = input("Enter output directory for images (default: ./output/images): ").strip()
        if not image_dir:
            image_dir = "./output/images"
    
    if len(sys.argv) > 3:
        json_path = sys.argv[3]
    else:
        json_path = input("Enter output JSON file path (default: ./output/questions.json): ").strip()
        if not json_path:
            json_path = "./output/questions.json"
    
    print()
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
        
        # Get exam metadata
        print()
        exam_name = input("Enter exam set name (default: Extracted Exam): ").strip()
        if not exam_name:
            exam_name = "Extracted Exam"
        
        exam_desc = input("Enter exam description: ").strip()
        category = input("Enter exam category (e.g., B2, A1): ").strip()
        
        is_official_input = input("Is this an official exam? (yes/no, default: yes): ").strip().lower()
        is_official = is_official_input in ['', 'yes', 'y']
        
        exam_metadata = {
            "name": exam_name,
            "description": exam_desc,
            "isOfficial": is_official,
            "category": category
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
        
        # Show sample questions
        print("Sample of extracted questions:")
        for i in range(min(3, len(questions))):
            q = questions[i]
            print(f"\nQuestion {i + 1}:")
            print(f"  Text: {q['questionText']}")
            print(f"  A: {q['optionA']}")
            print(f"  B: {q['optionB']}")
            if q['optionC']:
                print(f"  C: {q['optionC']}")
            if q['optionD']:
                print(f"  D: {q['optionD']}")
            print(f"  Correct Answer: {q['correctAnswer']}")
        
    except Exception as e:
        print(f"ERROR: {e}")
        import traceback
        traceback.print_exc()


if __name__ == "__main__":
    main()

