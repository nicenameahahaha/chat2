package com.example.ihatemylife.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.ihatemylife.database.entities.MessageEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MessageDao_Impl implements MessageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MessageEntity> __insertionAdapterOfMessageEntity;

  private final EntityDeletionOrUpdateAdapter<MessageEntity> __updateAdapterOfMessageEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsRead;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsDelivered;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMessage;

  public MessageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMessageEntity = new EntityInsertionAdapter<MessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `messages` (`id`,`senderId`,`receiverId`,`content`,`timestamp`,`isDelivered`,`isRead`,`readAt`,`source`,`replyToMessageId`,`syncedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSenderId());
        if (entity.getReceiverId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getReceiverId());
        }
        if (entity.getContent() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getContent());
        }
        statement.bindLong(5, entity.getTimestamp());
        final int _tmp = entity.isDelivered() ? 1 : 0;
        statement.bindLong(6, _tmp);
        final int _tmp_1 = entity.isRead() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        if (entity.getReadAt() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getReadAt());
        }
        if (entity.getSource() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSource());
        }
        if (entity.getReplyToMessageId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getReplyToMessageId());
        }
        statement.bindLong(11, entity.getSyncedAt());
      }
    };
    this.__updateAdapterOfMessageEntity = new EntityDeletionOrUpdateAdapter<MessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `messages` SET `id` = ?,`senderId` = ?,`receiverId` = ?,`content` = ?,`timestamp` = ?,`isDelivered` = ?,`isRead` = ?,`readAt` = ?,`source` = ?,`replyToMessageId` = ?,`syncedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSenderId());
        if (entity.getReceiverId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getReceiverId());
        }
        if (entity.getContent() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getContent());
        }
        statement.bindLong(5, entity.getTimestamp());
        final int _tmp = entity.isDelivered() ? 1 : 0;
        statement.bindLong(6, _tmp);
        final int _tmp_1 = entity.isRead() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        if (entity.getReadAt() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getReadAt());
        }
        if (entity.getSource() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSource());
        }
        if (entity.getReplyToMessageId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getReplyToMessageId());
        }
        statement.bindLong(11, entity.getSyncedAt());
        statement.bindLong(12, entity.getId());
      }
    };
    this.__preparedStmtOfMarkAsRead = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE messages SET isRead = 1, readAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkAsDelivered = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE messages SET isDelivered = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMessage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM messages WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMessage(final MessageEntity message,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMessageEntity.insert(message);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMessages(final List<MessageEntity> messages,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMessageEntity.insert(messages);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMessage(final MessageEntity message,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMessageEntity.handle(message);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsRead(final int messageId, final long readAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsRead.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, readAt);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, messageId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkAsRead.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsDelivered(final int messageId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsDelivered.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, messageId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkAsDelivered.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMessage(final int id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMessage.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteMessage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getMessageById(final int id,
      final Continuation<? super MessageEntity> $completion) {
    final String _sql = "SELECT * FROM messages WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MessageEntity>() {
      @Override
      @Nullable
      public MessageEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfReceiverId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiverId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsDelivered = CursorUtil.getColumnIndexOrThrow(_cursor, "isDelivered");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final MessageEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSenderId;
            _tmpSenderId = _cursor.getInt(_cursorIndexOfSenderId);
            final Integer _tmpReceiverId;
            if (_cursor.isNull(_cursorIndexOfReceiverId)) {
              _tmpReceiverId = null;
            } else {
              _tmpReceiverId = _cursor.getInt(_cursorIndexOfReceiverId);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsDelivered;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDelivered);
            _tmpIsDelivered = _tmp != 0;
            final boolean _tmpIsRead;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp_1 != 0;
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final Integer _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getInt(_cursorIndexOfReplyToMessageId);
            }
            final long _tmpSyncedAt;
            _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            _result = new MessageEntity(_tmpId,_tmpSenderId,_tmpReceiverId,_tmpContent,_tmpTimestamp,_tmpIsDelivered,_tmpIsRead,_tmpReadAt,_tmpSource,_tmpReplyToMessageId,_tmpSyncedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MessageEntity>> getAllMessagesForUser(final int userId) {
    final String _sql = "SELECT * FROM messages WHERE senderId = ? OR receiverId = ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"messages"}, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfReceiverId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiverId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsDelivered = CursorUtil.getColumnIndexOrThrow(_cursor, "isDelivered");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSenderId;
            _tmpSenderId = _cursor.getInt(_cursorIndexOfSenderId);
            final Integer _tmpReceiverId;
            if (_cursor.isNull(_cursorIndexOfReceiverId)) {
              _tmpReceiverId = null;
            } else {
              _tmpReceiverId = _cursor.getInt(_cursorIndexOfReceiverId);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsDelivered;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDelivered);
            _tmpIsDelivered = _tmp != 0;
            final boolean _tmpIsRead;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp_1 != 0;
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final Integer _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getInt(_cursorIndexOfReplyToMessageId);
            }
            final long _tmpSyncedAt;
            _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            _item = new MessageEntity(_tmpId,_tmpSenderId,_tmpReceiverId,_tmpContent,_tmpTimestamp,_tmpIsDelivered,_tmpIsRead,_tmpReadAt,_tmpSource,_tmpReplyToMessageId,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MessageEntity>> getConversation(final int userId1, final int userId2) {
    final String _sql = "SELECT * FROM messages WHERE (senderId = ? AND receiverId = ?) OR (senderId = ? AND receiverId = ?) ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId1);
    _argIndex = 2;
    _statement.bindLong(_argIndex, userId2);
    _argIndex = 3;
    _statement.bindLong(_argIndex, userId2);
    _argIndex = 4;
    _statement.bindLong(_argIndex, userId1);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"messages"}, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfReceiverId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiverId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsDelivered = CursorUtil.getColumnIndexOrThrow(_cursor, "isDelivered");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSenderId;
            _tmpSenderId = _cursor.getInt(_cursorIndexOfSenderId);
            final Integer _tmpReceiverId;
            if (_cursor.isNull(_cursorIndexOfReceiverId)) {
              _tmpReceiverId = null;
            } else {
              _tmpReceiverId = _cursor.getInt(_cursorIndexOfReceiverId);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsDelivered;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDelivered);
            _tmpIsDelivered = _tmp != 0;
            final boolean _tmpIsRead;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp_1 != 0;
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final Integer _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getInt(_cursorIndexOfReplyToMessageId);
            }
            final long _tmpSyncedAt;
            _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            _item = new MessageEntity(_tmpId,_tmpSenderId,_tmpReceiverId,_tmpContent,_tmpTimestamp,_tmpIsDelivered,_tmpIsRead,_tmpReadAt,_tmpSource,_tmpReplyToMessageId,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MessageEntity>> getSentMessages(final int userId) {
    final String _sql = "SELECT * FROM messages WHERE senderId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"messages"}, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfReceiverId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiverId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsDelivered = CursorUtil.getColumnIndexOrThrow(_cursor, "isDelivered");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSenderId;
            _tmpSenderId = _cursor.getInt(_cursorIndexOfSenderId);
            final Integer _tmpReceiverId;
            if (_cursor.isNull(_cursorIndexOfReceiverId)) {
              _tmpReceiverId = null;
            } else {
              _tmpReceiverId = _cursor.getInt(_cursorIndexOfReceiverId);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsDelivered;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDelivered);
            _tmpIsDelivered = _tmp != 0;
            final boolean _tmpIsRead;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp_1 != 0;
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final Integer _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getInt(_cursorIndexOfReplyToMessageId);
            }
            final long _tmpSyncedAt;
            _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            _item = new MessageEntity(_tmpId,_tmpSenderId,_tmpReceiverId,_tmpContent,_tmpTimestamp,_tmpIsDelivered,_tmpIsRead,_tmpReadAt,_tmpSource,_tmpReplyToMessageId,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MessageEntity>> getReceivedMessages(final int userId) {
    final String _sql = "SELECT * FROM messages WHERE receiverId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"messages"}, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfReceiverId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiverId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsDelivered = CursorUtil.getColumnIndexOrThrow(_cursor, "isDelivered");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSenderId;
            _tmpSenderId = _cursor.getInt(_cursorIndexOfSenderId);
            final Integer _tmpReceiverId;
            if (_cursor.isNull(_cursorIndexOfReceiverId)) {
              _tmpReceiverId = null;
            } else {
              _tmpReceiverId = _cursor.getInt(_cursorIndexOfReceiverId);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsDelivered;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDelivered);
            _tmpIsDelivered = _tmp != 0;
            final boolean _tmpIsRead;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp_1 != 0;
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final Integer _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getInt(_cursorIndexOfReplyToMessageId);
            }
            final long _tmpSyncedAt;
            _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            _item = new MessageEntity(_tmpId,_tmpSenderId,_tmpReceiverId,_tmpContent,_tmpTimestamp,_tmpIsDelivered,_tmpIsRead,_tmpReadAt,_tmpSource,_tmpReplyToMessageId,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MessageEntity>> getTelegramMessagesForUser(final int userId) {
    final String _sql = "SELECT * FROM messages WHERE source = 'telegram' AND (senderId = ? OR receiverId = ?) ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"messages"}, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfReceiverId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiverId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsDelivered = CursorUtil.getColumnIndexOrThrow(_cursor, "isDelivered");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSenderId;
            _tmpSenderId = _cursor.getInt(_cursorIndexOfSenderId);
            final Integer _tmpReceiverId;
            if (_cursor.isNull(_cursorIndexOfReceiverId)) {
              _tmpReceiverId = null;
            } else {
              _tmpReceiverId = _cursor.getInt(_cursorIndexOfReceiverId);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsDelivered;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDelivered);
            _tmpIsDelivered = _tmp != 0;
            final boolean _tmpIsRead;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp_1 != 0;
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final Integer _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getInt(_cursorIndexOfReplyToMessageId);
            }
            final long _tmpSyncedAt;
            _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            _item = new MessageEntity(_tmpId,_tmpSenderId,_tmpReceiverId,_tmpContent,_tmpTimestamp,_tmpIsDelivered,_tmpIsRead,_tmpReadAt,_tmpSource,_tmpReplyToMessageId,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MessageEntity>> getMessagesAfter(final int userId, final long fromTimestamp) {
    final String _sql = "SELECT * FROM messages WHERE (senderId = ? OR receiverId = ?) AND timestamp >= ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 3;
    _statement.bindLong(_argIndex, fromTimestamp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"messages"}, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfReceiverId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiverId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsDelivered = CursorUtil.getColumnIndexOrThrow(_cursor, "isDelivered");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSenderId;
            _tmpSenderId = _cursor.getInt(_cursorIndexOfSenderId);
            final Integer _tmpReceiverId;
            if (_cursor.isNull(_cursorIndexOfReceiverId)) {
              _tmpReceiverId = null;
            } else {
              _tmpReceiverId = _cursor.getInt(_cursorIndexOfReceiverId);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsDelivered;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDelivered);
            _tmpIsDelivered = _tmp != 0;
            final boolean _tmpIsRead;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp_1 != 0;
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final Integer _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getInt(_cursorIndexOfReplyToMessageId);
            }
            final long _tmpSyncedAt;
            _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            _item = new MessageEntity(_tmpId,_tmpSenderId,_tmpReceiverId,_tmpContent,_tmpTimestamp,_tmpIsDelivered,_tmpIsRead,_tmpReadAt,_tmpSource,_tmpReplyToMessageId,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRepliesToMessage(final int messageId,
      final Continuation<? super List<MessageEntity>> $completion) {
    final String _sql = "SELECT * FROM messages WHERE replyToMessageId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, messageId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfReceiverId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiverId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsDelivered = CursorUtil.getColumnIndexOrThrow(_cursor, "isDelivered");
          final int _cursorIndexOfIsRead = CursorUtil.getColumnIndexOrThrow(_cursor, "isRead");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSenderId;
            _tmpSenderId = _cursor.getInt(_cursorIndexOfSenderId);
            final Integer _tmpReceiverId;
            if (_cursor.isNull(_cursorIndexOfReceiverId)) {
              _tmpReceiverId = null;
            } else {
              _tmpReceiverId = _cursor.getInt(_cursorIndexOfReceiverId);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsDelivered;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDelivered);
            _tmpIsDelivered = _tmp != 0;
            final boolean _tmpIsRead;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRead);
            _tmpIsRead = _tmp_1 != 0;
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final Integer _tmpReplyToMessageId;
            if (_cursor.isNull(_cursorIndexOfReplyToMessageId)) {
              _tmpReplyToMessageId = null;
            } else {
              _tmpReplyToMessageId = _cursor.getInt(_cursorIndexOfReplyToMessageId);
            }
            final long _tmpSyncedAt;
            _tmpSyncedAt = _cursor.getLong(_cursorIndexOfSyncedAt);
            _item = new MessageEntity(_tmpId,_tmpSenderId,_tmpReceiverId,_tmpContent,_tmpTimestamp,_tmpIsDelivered,_tmpIsRead,_tmpReadAt,_tmpSource,_tmpReplyToMessageId,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLastActivityTimestampForUser(final int userId,
      final Continuation<? super Long> $completion) {
    final String _sql = "SELECT MAX(timestamp) FROM messages WHERE senderId = ? OR receiverId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getDistinctUserIdsFromMessages(
      final Continuation<? super List<Integer>> $completion) {
    final String _sql = "SELECT DISTINCT senderId FROM messages UNION SELECT DISTINCT receiverId FROM messages WHERE receiverId IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Integer>>() {
      @Override
      @NonNull
      public List<Integer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<Integer> _result = new ArrayList<Integer>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Integer _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getInt(0);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
