import fitz
import sys

def extract_specific_pages(pdf_path, start_page, num_pages=3):
    """Extract text from specific pages"""
    print("Opening PDF to analyze text format...")
    doc = fitz.open(pdf_path)
    
    print(f"Total pages: {len(doc)}")
    print(f"Extracting pages {start_page} to {start_page + num_pages - 1}...\n")
    print("=" * 60)
    
    for page_num in range(start_page - 1, min(start_page - 1 + num_pages, len(doc))):
        page = doc[page_num]
        text = page.get_text()
        
        print(f"\n--- PAGE {page_num + 1} ---")
        print(text[:3000])  # Print first 3000 characters
        print("\n" + "=" * 60)
    
    doc.close()

if __name__ == "__main__":
    if len(sys.argv) > 1:
        pdf_path = sys.argv[1]
        start_page = int(sys.argv[2]) if len(sys.argv) > 2 else 4
    else:
        pdf_path = input("Enter PDF path: ").strip().strip('"')
        start_page = int(input("Enter start page (default 4): ").strip() or "4")
    
    extract_specific_pages(pdf_path, start_page, 3)

