"""
Проверка импортов и базовый тест модулей (без реального подключения к БД).
Для полного теста с БД запустите: python test_run.py --live
"""
import asyncio
import sys


def test_imports():
    """Проверка, что все модули импортируются и функции существуют."""
    errors = []
    try:
        from Connection import register_user
    except Exception as e:
        errors.append(f"Connection: {e}")
    try:
        from importlib import import_module
        find_user = import_module("find user")  # имя с пробелом
        login_user = getattr(find_user, "login_user")
    except Exception as e:
        errors.append(f"find user: {e}")
    try:
        from history import get_chat_history
    except Exception as e:
        errors.append(f"history: {e}")
    try:
        from messages import send_message
    except Exception as e:
        errors.append(f"messages: {e}")

    if errors:
        print("Ошибки импорта:")
        for e in errors:
            print("  -", e)
        return False
    print("Все модули импортированы успешно.")
    return True


def test_argon2_hash_verify():
    """Проверка хэширования и верификации пароля (без БД)."""
    from argon2 import PasswordHasher
    ph = PasswordHasher()
    password = "test_password_123"
    h = ph.hash(password)
    assert ph.verify(h, password), "Верификация пароля не сработала"
    print("Argon2 hash/verify: OK")
    return True


async def test_live_db():
    """Живой тест с БД (регистрация, логин, история). Требует доступ к БД."""
    from Connection import register_user
    from importlib import import_module
    find_user = import_module("find user")
    login_user = find_user.login_user
    from history import get_chat_history
    from messages import send_message

    test_user = "test_user_" + str(asyncio.get_event_loop().time())[:10]
    test_pass = "TestPass123"
    test_email = "test@example.com"

    try:
        await register_user(test_user, test_pass, test_email)
        print("register_user: OK")
    except Exception as e:
        print("register_user FAIL:", e)
        return False

    try:
        user_id = await login_user(test_user, test_pass)
        if user_id:
            print("login_user: OK, user_id =", user_id)
        else:
            print("login_user: FAIL (вернулся None)")
            return False
    except Exception as e:
        print("login_user FAIL:", e)
        return False

    try:
        rows = await get_chat_history(user_id)
        print("get_chat_history: OK, записей:", len(rows))
    except Exception as e:
        print("get_chat_history FAIL:", e)
        return False

    try:
        await send_message(user_id, test_user, "Test message to self")
        print("send_message: OK")
    except Exception as e:
        print("send_message FAIL:", e)
        return False

    print("Все живые тесты с БД пройдены.")
    return True


if __name__ == "__main__":
    print("=== Проверка импортов ===")
    if not test_imports():
        sys.exit(1)
    print("\n=== Проверка Argon2 ===")
    if not test_argon2_hash_verify():
        sys.exit(1)
    if "--live" in sys.argv:
        print("\n=== Живой тест с БД ===")
        if not asyncio.run(test_live_db()):
            sys.exit(1)
    else:
        print("\n(Для теста с реальной БД запустите: python test_run.py --live)")
    print("\nГотово.")
