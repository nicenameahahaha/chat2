from passlib.context import CryptContext

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

# Bcrypt принимает пароль не длиннее 72 байт
def _truncate_for_bcrypt(password: str) -> str:
    data = password.encode("utf-8")[:72]
    return data.decode("utf-8", errors="ignore")

def get_password_hash(password: str) -> str:
    return pwd_context.hash(_truncate_for_bcrypt(password))

def verify_password(password: str, hashed: str) -> bool:
    return pwd_context.verify(_truncate_for_bcrypt(password), hashed)
