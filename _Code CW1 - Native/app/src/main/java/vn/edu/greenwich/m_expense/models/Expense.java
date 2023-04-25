package vn.edu.greenwich.m_expense.models;

import java.io.Serializable;

public class Expense implements Serializable {
    protected long _id;
    protected String _type;
    protected double _amount;
    protected String _date;
    protected String _time;
    protected String _comment;
    protected long _tripId;

    public Expense() {
        _id = -1;
        _type = null;
        _amount = -1;
        _date = null;
        _time = null;
        _comment = null;
        _tripId = -1;
    }

    public long getId() {
        return _id;
    }
    public void setId(long id) {
        _id = id;
    }

    public String getType() {
        return _type;
    }
    public void setType(String type) {
        _type = type;
    }

    public double getAmount() {
        return _amount;
    }
    public void setAmount(double amount) {
        _amount = amount;
    }

    public String getDate() {
        return _date;
    }
    public void setDate(String date) {
        _date = date;
    }

    public String getTime() {
        return _time;
    }
    public void setTime(String time) {
        _time = time;
    }

    public String getComment() {
        return _comment;
    }
    public void setComment(String comment) {
        _comment = comment;
    }

    public long getTripId() {
        return _tripId;
    }
    public void setTripId(long tripId) {
        _tripId = tripId;
    }

    public boolean isEmpty() {
        if (-1 == _id && null == _type && -1 == _amount && null == _date && null == _time && null == _comment && -1 == _tripId)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "[" + _type + "][" + _date + " " + _time + "] " + _amount + " (" + _comment + ")";
    }
}