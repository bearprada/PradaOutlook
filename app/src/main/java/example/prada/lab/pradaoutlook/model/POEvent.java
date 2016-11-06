package example.prada.lab.pradaoutlook.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.util.Date;
import com.github.sundeepk.compactcalendarview.domain.Event;

import example.prada.lab.pradaoutlook.db.OutlookDbHelper;

/**
 * Created by prada on 10/27/16.
 */
public class POEvent {
    private final String mTitle;
    private final String mLabel;
    private final Date mFrom;
    private final Date mTo;
    private Long mId;

    public POEvent(String title, String label, Date from, Date to) {
        mTitle = title;
        mLabel = label;
        mFrom = from;
        mTo = to;
        mId = -1L;
    }

    public POEvent(ContentValues values) {
        mId = values.getAsLong(OutlookDbHelper.EVENT_ID);
        mTitle = values.getAsString(OutlookDbHelper.EVENT_TITLE);
        mLabel = values.getAsString(OutlookDbHelper.EVENT_LABEL);
        mFrom = new Date(values.getAsLong(OutlookDbHelper.EVENT_START_TIME));
        mTo = new Date(values.getAsLong(OutlookDbHelper.EVENT_END_TIME));
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLabel() {
        return mLabel;
    }

    public Date getFrom() {
        return mFrom;
    }

    public Date getTo() {
        return mTo;
    }

    // TODO Test caes, expected format : 2h30m
    public String getDurationString() {
        long seconds = (mTo.getTime() - mFrom.getTime()) / 1000;
        if (seconds < 60) { // it's less then 1 min.
            return null;
        }
        int hour = (int) (seconds / 3600);
        int min = (int) ((seconds % 3600) / 60);
        if (hour <= 0 && min > 0) {
            return min + "m";
        }
        if (min <= 0 && hour > 0) {
            return hour + "h";
        }
        return hour + "h" + min + "m";
    }

    public Event toEvent() {
        return new Event(getColorRes(), getFrom().getTime(), this);
    }

    private @ColorInt int getColorRes() {
        // return Color.parseColor("#FF4081"); // TODO support different color later
        return Color.parseColor("#00FF00");
    }

    public static POEvent createFromCursor(Cursor cursor) {
        ContentValues values = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, values);
        return new POEvent(values);
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(OutlookDbHelper.EVENT_TITLE, mTitle);
        cv.put(OutlookDbHelper.EVENT_LABEL, mLabel);
        cv.put(OutlookDbHelper.EVENT_START_TIME, mFrom.getTime());
        cv.put(OutlookDbHelper.EVENT_END_TIME, mTo.getTime());
        return cv;
    }

    public Long getId() {
        return mId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        POEvent poEvent = (POEvent) o;

        if (mTitle != null ? !mTitle.equals(poEvent.mTitle) : poEvent.mTitle != null) return false;
        if (mLabel != null ? !mLabel.equals(poEvent.mLabel) : poEvent.mLabel != null) return false;
        if (mFrom != null ? !mFrom.equals(poEvent.mFrom) : poEvent.mFrom != null) return false;
        if (mTo != null ? !mTo.equals(poEvent.mTo) : poEvent.mTo != null) return false;
        return mId != null ? mId.equals(poEvent.mId) : poEvent.mId == null;

    }

    @Override
    public int hashCode() {
        int result = mTitle != null ? mTitle.hashCode() : 0;
        result = 31 * result + (mLabel != null ? mLabel.hashCode() : 0);
        result = 31 * result + (mFrom != null ? mFrom.hashCode() : 0);
        result = 31 * result + (mTo != null ? mTo.hashCode() : 0);
        result = 31 * result + (mId != null ? mId.hashCode() : 0);
        return result;
    }

    public void setId(long id) {
        mId = id;
    }
}
