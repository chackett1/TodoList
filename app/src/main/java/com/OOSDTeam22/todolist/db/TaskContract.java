package com.OOSDTeam22.todolist.db;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.OOSDTeam22.todolist.db";
    public static final int DB_VERSION = 2;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "lists";

        public static final String COL_LIST_NAME = "list";
        public static final String COL_LIST_ITEM = "item";
    }
}
