import json

# Load the JSON file
with open('output/A1_250Q_with_images.json', encoding='utf-8') as f:
    data = json.load(f)

questions = data[0]['questions']
questions_with_images = [q for q in questions if q.get('imagePath')]

print(f"Total questions: {len(questions)}")
print(f"Questions with images: {len(questions_with_images)}")
print(f"Questions without images: {len(questions) - len(questions_with_images)}")
print("\n" + "="*70)
print("Questions WITH images (first 15):")
print("="*70)

for i, q in enumerate(questions_with_images[:15], 1):
    print(f"\n{i}. {q['questionText'][:70]}...")
    print(f"   Image: {q['imagePath']}")
    print(f"   Correct Answer: {q['correctAnswer']}")

