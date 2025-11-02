import fitz
import sys

def extract_text_sample(pdf_path, max_pages=3):
    """Extract text from first few pages to see the format"""
    print("Opening PDF to analyze text format...")
    doc = fitz.open(pdf_path)
    
    print(f"Total pages: {len(doc)}")
    print(f"Extracting first {max_pages} pages...\n")
    print("=" * 60)
    
    for page_num in range(min(max_pages, len(doc))):
        page = doc[page_num]
        text = page.get_text()
        
        print(f"\n--- PAGE {page_num + 1} ---")
        print(text[:2000])  # Print first 2000 characters
        print("\n" + "=" * 60)
    
    doc.close()

if __name__ == "__main__":
    if len(sys.argv) > 1:
        pdf_path = sys.argv[1]
    else:
        pdf_path = input("Enter PDF path: ").strip().strip('"')
    
    extract_text_sample(pdf_path)

