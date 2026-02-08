D# Smart Client Architecture (2 clients + 1 backend)

This document describes how the app achieves a "smart server-side database" feel (global users, presence, interactivity) **without** changing the backend, or with only minimal optional backend changes.

## Principles

- **Backend = source of truth**: All persistent data (users, contacts, messages) is written to the backend when possible. Local storage is an offline-first cache.
- **Local storage = offline-first layer**: Room holds users, contacts, messages, chats. SharedPreferences hold current user and settings. DatabaseHelper (in-memory) holds registered users and chats for the current session; data is also persisted to Room where applicable.
- **Pseudo-global user list**: There is no backend endpoint to list all users. The app builds a "global" list client-side from: (1) Room users (registered on this device + users discovered from synced messages), (2) DatabaseHelper (in-memory registered users). User search is **local only** over this merged list.
- **Presence (last seen)**: Derived from message timestamps. For each user, "last seen" = latest message timestamp where they are sender or receiver. No backend presence endpoint required.
- **Typing indicators**: Implemented via a **system message**: content `[TYPING]` sent while user is typing; filtered out from the displayed message list; recent receipt shows "X is typing". Uses existing send-message API.
- **Delivered/read states**: Use existing PATCH endpoints; sync messages on resume so both devices see updates.

## Data flow

```
┌─────────────────────────────────────────────────────────────────┐
│  UI (Activity / Composable)                                      │
│  - NewChatActivity: pseudo-global user list, local search        │
│  - ChatActivity: messages, typing, read/delivered, sync on open    │
│  - ChatsActivity: chat list, sync on resume                      │
└───────────────────────────┬─────────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────────┐
│  ViewModels (ChatViewModel, ChatsViewModel, etc.)                 │
│  - Load data from repositories, trigger sync, filter typing msgs  │
└───────────────────────────┬─────────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────────┐
│  Repositories (User, Contact, Message, Chat)                      │
│  - Room (primary local cache) + API (source of truth)            │
│  - MessageRepository: after sync, discover users from messages  │
│  - Pseudo-global list: UserRepository + DatabaseHelper merge     │
└───────────────────────────┬─────────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────────┐
│  Room DAOs + ApiService                                           │
│  - No new API endpoints required                                 │
└─────────────────────────────────────────────────────────────────┘
```

## Pseudo-global user list

- **Sources**: (1) `UserDao.getAllUsersFlow()` — users in Room (registered + placeholders from message sync). (2) `DatabaseHelper.getAllUsers()` — in-memory registered users.
- **Merge**: Room users are the primary list (includes "discovered" users from sync with placeholder username `User<id>` until backend optionally provides usernames in message payload). DatabaseHelper users are merged so that same-device registered users appear even before they are in Room (e.g. before first sync).
- **User discovery on sync**: When `MessageRepository.syncMessages(username)` runs, it inserts messages. For each distinct `sender_id` and `receiver_id` in the synced messages, if the user is not in Room, we insert `UserEntity(id, "User$id")`. So the other device's user appears in the list after sync.
- **Search**: Filter the merged list by query (username, email, etc.) locally. No API calls.

## Presence (last seen)

- **Source**: Message table. For a given user ID, last activity = `MAX(timestamp)` over messages where that user is sender or receiver.
- **Implementation**: `MessageDao.getLastActivityTimestampForUser(userId)` returns this max timestamp. UI shows "Last seen at X" or "Active now" (e.g. if within last 2 minutes).
- **No backend change**: Backend already returns message timestamps; we derive presence client-side.

## Typing indicators

- **Mechanism**: Send a normal message with content exactly `[TYPING]` when the user is typing (debounced, e.g. every 2 s while typing). The other device receives it via sync or real-time send; we **filter out** any message with `content == "[TYPING]"` from the displayed list. If we have such a message from the other user in the last 5–10 seconds, show "X is typing".
- **Storage**: These messages are stored on the backend like any other message. Optional: backend could strip or ignore `[TYPING]` messages to avoid clutter; not required for basic behavior.

## Delivered / read states

- **Existing API**: `PATCH messages/{id}/read/{username}`, `PATCH messages/{id}/delivered`.
- **Usage**: When the user opens a conversation, mark received messages as read. When our app receives a message, mark as delivered. Sync on resume so the other device sees read/delivered updates.

## Sync between devices

- **When**: On ChatsActivity resume and when opening a chat (ChatActivity), call `MessageRepository.syncMessages(currentUsername)` so we pull latest messages (and thus presence, typing, read/delivered) from the backend.
- **Polling**: For 2 devices, periodic sync (e.g. every 30 s when chat is open) improves liveness; optional and simple.

## Optional minimal backend changes

| Change | Why | Without it | Example |
|--------|-----|------------|--------|
| Add `sender_username` / `receiver_username` to message response | So we can show real names for "discovered" users instead of `User<id>` | We show placeholder "User\<id\>" for users we only know from message IDs | In `ApiMessageOut`: `sender_username: String?`, `receiver_username: String?`; on sync, client can upsert `UserEntity(id, username)` when present. |
| (Optional) Ignore or don’t persist messages with `content == "[TYPING]"` | Keeps message history clean | We store typing as normal messages; filter in client only | Backend: if content is `[TYPING]`, return 200 but don’t insert |

No other backend changes are required. No new endpoints, no global user list, no presence API.

## Classes modified (summary)

| Layer | Classes | Changes |
|-------|---------|--------|
| **DAO** | `MessageDao` | Added `getLastActivityTimestampForUser(userId)` for presence; `getDistinctUserIdsFromMessages()` for user discovery after sync. |
| **Repository** | `MessageRepository` | After sync, calls `ensureUsersFromMessages()` so every sender/receiver exists in `UserDao` (placeholder `UserEntity(id, "User$id")`). Added `getLastActivityTimestampForUser()`. |
| **ViewModel** | `ChatViewModel` | Filters out `[TYPING]` in `applyMessageList()`; exposes `otherUserTyping` and `otherUserLastSeen`; calls `syncMessages()` on load; `setUserTyping(true)` / `stopTyping()` with debounced send of `[TYPING]`; marks received messages as read when opening chat. |
| **Activity** | `NewChatActivity` | Pseudo-global list: `roomUsers` (UserRepository.getAllUsersFlow()) + DatabaseHelper + roomContacts, merged and distinct by username/email/id. Local search over that list. |
| **Activity** | `ChatsActivity` | Calls `MessageRepository.syncMessages(username)` in `onResume()` so chat list stays fresh. |
| **Activity** | `ChatActivity` | Subtitle shows "typing...", "active now", or "last seen X"; LaunchedEffect/DisposableEffect for typing indicator; sync on open is done in ViewModel init. |
| **Constants** | `MessengerConstants` | `TYPING_INDICATOR_CONTENT`, `TYPING_INDICATOR_ACTIVE_MS`, `TYPING_SEND_DEBOUNCE_MS`, `PRESENCE_ACTIVE_NOW_MS`. |

## UX priorities

- **Simplicity**: One merged user list, one sync entry point (messages), presence from existing data.
- **Predictability**: Always show last known state; sync on resume so both devices converge.
- **Liveness**: Optional periodic sync when chat is open; typing via system message gives immediate feel without new APIs.
