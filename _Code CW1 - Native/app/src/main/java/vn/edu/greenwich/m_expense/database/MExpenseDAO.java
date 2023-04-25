package vn.edu.greenwich.m_expense.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.edu.greenwich.m_expense.models.Expense;
import vn.edu.greenwich.m_expense.models.Trip;

public class MExpenseDAO {
    protected MExpenseDbHelper mexpenseDbHelper;
    protected SQLiteDatabase dbWrite, dbRead;

    public MExpenseDAO(Context context) {
        mexpenseDbHelper = new MExpenseDbHelper(context);

        dbRead = mexpenseDbHelper.getReadableDatabase();
        dbWrite = mexpenseDbHelper.getWritableDatabase();
    }

    public void reset() {
        mexpenseDbHelper.onUpgrade(dbWrite, 0, 0);
    }

    // Trip.

    public long insertTrip(Trip trip) {
        ContentValues values = getTripValues(trip);

        return dbWrite.insert(TripEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Trip> getTripList(Trip trip, String[] orderByColumns, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumns, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (null != trip) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (trip.getName() != null && !trip.getName().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_NAME + " LIKE ?";
                conditionList.add("%" + trip.getName() + "%");
            }

            if (trip.getDestination() != null && !trip.getDestination().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_DESTINATION + " LIKE ?";
                conditionList.add("%" + trip.getDestination() + "%");
            }

            if (trip.getStartDate() != null && !trip.getStartDate().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_START_DATE + " = ?";
                conditionList.add(trip.getStartDate());
            }

            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }

            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }

        return getTripFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public Trip getTripById(long id) {
        String selection = TripEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getTripFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }

    public long updateTrip(Trip trip) {
        ContentValues values = getTripValues(trip);

        String selection = TripEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(trip.getId())};

        return dbWrite.update(TripEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public long deleteTrip(long id) {
        String selection = TripEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return dbWrite.delete(TripEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected String getOrderByString(String[] orderByColumns, boolean isDesc) {
        if (orderByColumns == null || orderByColumns.length == 0)
            return null;

        String columns = "";
        for (String column : orderByColumns) {
            columns += ", " + column;
        }

        if (!columns.trim().isEmpty())
            columns = columns.substring(2);

        if (isDesc)
            return columns + " DESC";

        return columns;
    }

    protected ContentValues getTripValues(Trip trip) {
        ContentValues values = new ContentValues();

        values.put(TripEntry.COL_NAME, trip.getName());
        values.put(TripEntry.COL_DESTINATION, trip.getDestination());
        values.put(TripEntry.COL_START_DATE, trip.getStartDate());
        values.put(TripEntry.COL_END_DATE, trip.getEndDate());
        values.put(TripEntry.COL_RISK, trip.getRisk());
        values.put(TripEntry.COL_DESCRIPTION, trip.getDescription());

        return values;
    }

    protected ArrayList<Trip> getTripFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Trip> list = new ArrayList<>();

        Cursor cursor = dbRead.query(TripEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            Trip tripItem = new Trip();

            tripItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(TripEntry.COL_ID)));
            tripItem.setName(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_NAME)));
            tripItem.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_DESTINATION)));
            tripItem.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_START_DATE)));
            tripItem.setEndDate(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_END_DATE)));
            tripItem.setRisk(cursor.getInt(cursor.getColumnIndexOrThrow(TripEntry.COL_RISK)));
            tripItem.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_DESCRIPTION)));

            list.add(tripItem);
        }

        cursor.close();

        return list;
    }

    // Expense.

    public long insertExpense(Expense expense) {
        ContentValues values = getExpenseValues(expense);

        return dbWrite.insert(ExpenseEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Expense> getExpenseList(Expense expense, String[] orderByColumns, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumns, isDesc);

        String selection = ExpenseEntry.COL_TRIP_ID + " = ?";
        String[] selectionArgs = {String.valueOf(expense.getTripId())};

        return getExpenseFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public Expense getExpenseById(long id) {
        String selection = ExpenseEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getExpenseFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }

    public long updateExpense(Expense expense) {
        ContentValues values = getExpenseValues(expense);

        String selection = ExpenseEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(expense.getId())};

        return dbWrite.update(ExpenseEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public long deleteExpense(long id) {
        String selection = ExpenseEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return dbWrite.delete(ExpenseEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected ContentValues getExpenseValues(Expense expense) {
        ContentValues values = new ContentValues();

        values.put(ExpenseEntry.COL_TYPE, expense.getType());
        values.put(ExpenseEntry.COL_AMOUNT, expense.getAmount());
        values.put(ExpenseEntry.COL_DATE, expense.getDate());
        values.put(ExpenseEntry.COL_TIME, expense.getTime());
        values.put(ExpenseEntry.COL_COMMENT, expense.getComment());
        values.put(ExpenseEntry.COL_TRIP_ID, expense.getTripId());

        return values;
    }

    protected ArrayList<Expense> getExpenseFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Expense> list = new ArrayList<>();

        Cursor cursor = dbRead.query(ExpenseEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            Expense expenseItem = new Expense();

            expenseItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_ID)));
            expenseItem.setType(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_TYPE)));
            expenseItem.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_AMOUNT)));
            expenseItem.setDate(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_DATE)));
            expenseItem.setTime(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_TIME)));
            expenseItem.setComment(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_COMMENT)));
            expenseItem.setTripId(cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_TRIP_ID)));

            list.add(expenseItem);
        }

        cursor.close();

        return list;
    }

    public Map<String, Object> getTotalExpenseByType() {
        Map<String, Object> result = new HashMap<>();

        String groupBy = ExpenseEntry.COL_TYPE;
        String colTotal = "total";
        String[] columns = {
                "SUM(" + ExpenseEntry.COL_AMOUNT + ") AS " + colTotal,
                ExpenseEntry.COL_TYPE
        };

        Cursor cursor = dbRead.query(ExpenseEntry.TABLE_NAME, columns, null, null, groupBy, null, null);

        while (cursor.moveToNext()) {
            float total = cursor.getFloat(cursor.getColumnIndexOrThrow(colTotal));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_TYPE));

            result.put(type, total);
        }

        cursor.close();

        return result;
    }
}