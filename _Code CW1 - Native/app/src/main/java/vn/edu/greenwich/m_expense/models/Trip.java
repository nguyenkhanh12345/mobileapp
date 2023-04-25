package vn.edu.greenwich.m_expense.models;

import java.io.Serializable;

public class Trip implements Serializable {
    protected long _id;
    protected String _name;
    protected String _destination;
    protected String _startDate;
    protected String _endDate;
    protected int _risk;
    protected String _description;

    public Trip() {
        _id = -1;
        _name = null;
        _destination = null;
        _startDate = null;
        _endDate = null;
        _risk = -1;
        _description = null;
    }

    public long getId() { return _id; }
    public void setId(long id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }
    public void setName(String name) {
        _name = name;
    }

    public String getDestination() {
        return _destination;
    }
    public void setDestination(String destination) {
        _destination = destination;
    }

    public String getStartDate() {
        return _startDate;
    }
    public void setStartDate(String startDate) {
        _startDate = startDate;
    }

    public String getEndDate() {
        return _endDate;
    }
    public void setEndDate(String endDate) {
        _endDate = endDate;
    }

    public int getRisk() {
        return _risk;
    }
    public void setRisk(int risk) {
        _risk = risk;
    }

    public String getDescription() {
        return _description;
    }
    public void setDescription(String description) {
        _description = description;
    }

    public boolean isEmpty() {
        if (-1 == _id && null == _name && null == _destination && null == _startDate && null == _endDate && -1 == _risk && null == _description)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "[" + _startDate + "-" + _endDate + "] " + _name + " (" + _destination + ")";
    }
}