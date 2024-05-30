CREATE TABLE "user_chat_id"
(
    "chat_id"    BIGINT PRIMARY KEY UNIQUE,
    "created_at" TIMESTAMP
);

CREATE TABLE "search"
(
    "id"           UUID PRIMARY KEY,
    "value"        TEXT,
    "user_chat_id" BIGINT REFERENCES "user_chat_id" ("chat_id"),
    "created_at"   TIMESTAMP
);

CREATE TABLE "selected_dictionary"
(
    "id"           UUID PRIMARY KEY,
    "dictionary"   TEXT,
    "user_chat_id" BIGINT REFERENCES "user_chat_id" ("chat_id") UNIQUE,
    "created_at"   TIMESTAMP
);