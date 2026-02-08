# Инструкция по настройке

## 1. Получение Telegram Bot Token

1. Откройте Telegram и найдите бота **@BotFather**
2. Отправьте команду `/newbot`
3. Следуйте инструкциям: придумайте имя и username для бота
4. BotFather выдаст вам токен вида: `123456789:ABCdefGHIjklMNOpqrsTUVwxyz`
5. Скопируйте этот токен

## 2. Создание файла .env

Создайте файл `.env` в корне проекта (там же, где `main.py`) со следующим содержимым:

```env
TG_BOT_TOKEN=ваш_токен_от_BotFather
DATABASE_URL=sqlite+aiosqlite:///./messenger.db
DEBUG=True
```

**Пример:**
```env
TG_BOT_TOKEN=123456789:ABCdefGHIjklMNOpqrsTUVwxyz
DATABASE_URL=sqlite+aiosqlite:///./messenger.db
DEBUG=True
```

## 3. База данных

**БД создается автоматически!** При первом запуске приложения все таблицы будут созданы автоматически.

Файл базы данных `messenger.db` появится в папке проекта после первого запуска.

Если хотите использовать другую БД (PostgreSQL, MySQL), измените `DATABASE_URL` в `.env`:
- PostgreSQL: `postgresql+asyncpg://user:password@localhost/dbname`
- MySQL: `mysql+aiomysql://user:password@localhost/dbname`

## 4. Установка зависимостей

```bash
pip install -r requirements.txt
```

## 5. Запуск приложения

```bash
uvicorn main:app --reload
```

## 6. Интеграция с Telegram (приём и отправка сообщений)

### Что уже есть в бэкенде

А - **ТГ → приложение:** Telegram присылает обновления на webhook. Бэкенд сохраняет сообщения и рассылает по WebSocket (`type: "telegram_message"`).
- **ТГ → конкретный пользователь:** Если в Telegram написать боту **«@bob привет»** или **«bob: привет»**, сообщение сохраняется как личное (отправитель — вы в ТГ, получатель — bob), доставляется bob в приложение по WebSocket и в Telegram (если у bob привязан ТГ).
- **Приложение → ТГ:** При отправке сообщения получателю через API или WebSocket, если у получателя заполнен `telegram_id`, бот отправляет копию ему в Telegram.

### Что нужно настроить

1. **Токен бота** — уже в `.env` (`TG_BOT_TOKEN`).

2. **Приём сообщений из Telegram (webhook):**
   - Ваш бэкенд должен быть доступен по **публичному HTTPS-URL** (локально Telegram не пришлёт запросы).
   - В `.env` добавьте этот URL без слэша в конце:
     ```env
     BASE_URL=https://your-domain.com
     ```
   - При старте приложения webhook регистрируется автоматически: Telegram будет присылать входящие сообщения на `{BASE_URL}/webhook/telegram`.
   - Для локальной разработки используйте туннель (ngrok, localtunnel и т.п.) и укажите его URL в `BASE_URL`.

3. **Кто получает сообщения в ТГ:**
   - Сообщение из приложения уходит в Telegram только если у **получателя** в БД есть `telegram_id`.
   - Пользователь получает `telegram_id`, когда **хотя бы раз написал боту** в Telegram: тогда создаётся пользователь с этим `telegram_id` (username из ТГ или `tg_123456`).
   - Чтобы отправлять в ТГ существующему пользователю приложения (например, `bob`), привяжите ему Telegram:
     - После того как пользователь написал боту, в БД появится запись с его `telegram_id` (посмотреть можно в списке пользователей или в таблице users).
     - Вызовите **PATCH /users/{username}/telegram** с телом `{"telegram_id": 123456789}` — тогда сообщения, отправленные в приложении пользователю `username`, будут дублироваться ему в Telegram.

## 7. Тесты

Из папки `backend` запустите все тесты: `python -m pytest tests/ -v` (или используйте `run_all_tests.bat`). Только тесты Telegram: `python -m pytest tests/test_telegram_integration.py -v`.