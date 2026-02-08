"""Запуск тестов интеграции с Telegram.

В терминале Cursor может падать из-за кодировки PowerShell.
Запустите в обычном cmd или PowerShell из папки проекта:
    python run_telegram_tests.py
либо:
    python -m pytest tests/test_telegram_integration.py -v
"""
import sys
import os

if __name__ == "__main__":
    os.chdir(os.path.dirname(os.path.abspath(__file__)))
    import pytest
    tests_path = os.path.join(os.path.dirname(__file__), "tests", "test_telegram_integration.py")
    sys.exit(pytest.main([tests_path, "-v"]))
