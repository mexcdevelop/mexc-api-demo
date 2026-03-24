# conftest.py
import sys
import os

sys.path.insert(0, os.path.abspath(os.path.dirname(__file__)))

print(f"Python path: {sys.path[0]}")