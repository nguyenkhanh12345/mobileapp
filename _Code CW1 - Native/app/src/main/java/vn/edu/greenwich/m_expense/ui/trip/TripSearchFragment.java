package vn.edu.greenwich.m_expense.ui.trip;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import vn.edu.greenwich.m_expense.R;
import vn.edu.greenwich.m_expense.models.Trip;
import vn.edu.greenwich.m_expense.ui.dialog.DatePickerFragment;

public class TripSearchFragment extends DialogFragment implements DatePickerFragment.FragmentListener {
    protected EditText fmTripSearchDate, fmTripSearchName, fmTripSearchDestination;
    protected Button fmTripSearchButtonCancel, fmTripSearchButtonSearch;

    public TripSearchFragment() {}

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_search, container, false);

        fmTripSearchDate = view.findViewById(R.id.fmTripSearchDate);
        fmTripSearchName = view.findViewById(R.id.fmTripSearchName);
        fmTripSearchDestination = view.findViewById(R.id.fmTripSearchDestination);
        fmTripSearchButtonCancel = view.findViewById(R.id.fmTripSearchButtonCancel);
        fmTripSearchButtonSearch = view.findViewById(R.id.fmTripSearchButtonSearch);

        fmTripSearchButtonSearch.setOnClickListener(v -> search());
        fmTripSearchButtonCancel.setOnClickListener(v -> dismiss());
        fmTripSearchDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));

        return view;
    }

    protected void search() {
        Trip _trip = new Trip();

        String date = fmTripSearchDate.getText().toString();
        String name = fmTripSearchName.getText().toString();
        String destination = fmTripSearchDestination.getText().toString();

        if (date != null && !date.trim().isEmpty())
            _trip.setStartDate(date);

        if (name != null && !name.trim().isEmpty())
            _trip.setName(name);

        if (destination != null && !destination.trim().isEmpty())
            _trip.setDestination(destination);

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromTripSearchFragment(_trip);

        dismiss();
    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new DatePickerFragment().show(getChildFragmentManager(), null);
        }

        return false;
    }

    @Override
    public void sendFromDatePickerFragment(String date, String key) {
        fmTripSearchDate.setText(date);
    }

    public interface FragmentListener {
        void sendFromTripSearchFragment(Trip trip);
    }
}