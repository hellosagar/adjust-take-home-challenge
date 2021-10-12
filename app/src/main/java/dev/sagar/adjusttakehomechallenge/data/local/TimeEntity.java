package dev.sagar.adjusttakehomechallenge.data.local;

/**
 * This is a model class which is we're saving in the SQLite
 */
public class TimeEntity {
    public static final String TABLE_NAME = "second";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SECOND = "second";
    public static final String COLUMN_IS_SYNCED = "isSynced";
    public static final String COLUMN_CREATED_TIME = "timestamp";

    private int id;
    private int second;
    private int isSynced;
    private long createdTime;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_SECOND + " TEXT, "
                    + COLUMN_IS_SYNCED + " INTEGER, "
                    + COLUMN_CREATED_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP "
                    + ")";

    // Constructors
    public TimeEntity() {
    }

    public TimeEntity(int id, int second, int isSynced, long createdTime) {
        this.id = id;
        this.second = second;
        this.isSynced = isSynced;
        this.createdTime = createdTime;
    }

    // Getters and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
