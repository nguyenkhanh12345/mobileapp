package vn.edu.greenwich.m_expense.ui.trip;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.switchmaterial.SwitchMaterial;
import vn.edu.greenwich.m_expense.R;
import vn.edu.greenwich.m_expense.database.MExpenseDAO;
import vn.edu.greenwich.m_expense.models.Trip;
import vn.edu.greenwich.m_expense.ui.dialog.DatePickerFragment;

public class TripAddFragment extends Fragment
        implements TripAddConfirmFragment.FragmentListener, DatePickerFragment.FragmentListener {
    public static final String ARG_PARAM_TRIP = "trip";

    protected EditText fmTripAddName, fmTripAddDestination, fmTripAddStartDate, fmTripAddEndDate, fmTripAddDescription;
    protected LinearLayout fmTripAddLinearLayout;
    protected SwitchMaterial fmTripAddRisk;
    protected TextView fmTripAddError;
    protected Button fmTripAddButton;

    protected MExpenseDAO _db;

    public TripAddFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new MExpenseDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_add, container, false);

        fmTripAddError = view.findViewById(R.id.fmTripAddError);
        fmTripAddName = view.findViewById(R.id.fmTripAddName);
        fmTripAddDestination = view.findViewById(R.id.fmTripAddDestination);
        fmTripAddStartDate = view.findViewById(R.id.fmTripAddStartDate);
        fmTripAddEndDate = view.findViewById(R.id.fmTripAddEndDate);
        fmTripAddDescription = view.findViewById(R.id.fmTripAddDescription);
        fmTripAddRisk = view.findViewById(R.id.fmTripAddRisk);
        fmTripAddButton = view.findViewById(R.id.fmTripAddButton);
        fmTripAddLinearLayout = view.findViewById(R.id.fmTripAddLinearLayout);

        // Show Calendar for choosing a date.
        fmTripAddStartDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent, fmTripAddStartDate.getHint().toString()));
        fmTripAddEndDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent, fmTripAddEndDate.getHint().toString()));

        // Update current trip.
        if (getArguments() != null) {
            Trip trip = (Trip) getArguments().getSerializable(ARG_PARAM_TRIP);

            fmTripAddName.setText(trip.getName());
            fmTripAddDestination.setText(trip.getDestination());
            fmTripAddStartDate.setText(trip.getStartDate());
            fmTripAddEndDate.setText(trip.getEndDate());
            fmTripAddDescription.setText(trip.getDescription());
            fmTripAddRisk.setChecked(trip.getRisk() == 1 ? true : false);

            fmTripAddButton.setText(R.string.label_update);
            fmTripAddButton.setOnClickListener(v -> update(trip.getId()));

            return view;
        }

        // Create new trip.
        fmTripAddButton.setOnClickListener(v -> add());

        return view;
    }

    protected void add() {
        if (isValidForm()) {
            Trip trip = getTripFromInput(-1);

            new TripAddConfirmFragment(trip).show(getChildFragmentManager(), null);

            return;
        }

        moveButton();
    }

    protected void update(long id) {
        if (isValidForm()) {
            Trip trip = getTripFromInput(id);

            long status = _db.updateTrip(trip);

            FragmentListener listener = (FragmentListener) getParentFragment();
            listener.sendFromTripAddFragment(status);

            return;
        }

        moveButton();
    }

    protected boolean showCalendar(MotionEvent motionEvent, String key) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new DatePickerFragment(key).show(getChildFragmentManager(), null);
        }

        return false;
    }

    protected Trip getTripFromInput(long id) {
        Trip trip = new Trip();

        trip.setId(id);
        trip.setName(fmTripAddName.getText().toString());
        trip.setDestination(fmTripAddDestination.getText().toString());
        trip.setStartDate(fmTripAddStartDate.getText().toString());
        trip.setEndDate(fmTripAddEndDate.getText().toString());
        trip.setDescription(fmTripAddDescription.getText().toString());
        trip.setRisk(fmTripAddRisk.isChecked() ? 1 : 0);

        return trip;
    }

    protected boolean isValidForm() {
        boolean isValid = true;

        String error = "";
        Trip trip = getTripFromInput(-1);

        if (trip.getName() == null || trip.getName().trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_name) + "\n";
            isValid = false;
        }

        if (trip.getDestination() == null || trip.getDestination().trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_destination) + "\n";
            isValid = false;
        }

        if (trip.getStartDate() == null || trip.getStartDate().trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_start_date) + "\n";
            isValid = false;
        }

        if (trip.getEndDate() == null || trip.getEndDate().trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_end_date) + "\n";
            isValid = false;
        }

        fmTripAddError.setText(error);

        return isValid;
    }

    protected void moveButton() {
        LinearLayout.LayoutParams btnParams = (LinearLayout.LayoutParams) fmTripAddButton.getLayoutParams();

        int linearLayoutPaddingLeft = fmTripAddLinearLayout.getPaddingLeft();
        int linearLayoutPaddingRight = fmTripAddLinearLayout.getPaddingRight();
        int linearLayoutWidth = fmTripAddLinearLayout.getWidth() - linearLayoutPaddingLeft - linearLayoutPaddingRight;

        btnParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        btnParams.topMargin += fmTripAddButton.getHeight();
        btnParams.leftMargin = btnParams.leftMargin == 0 ? linearLayoutWidth - fmTripAddButton.getWidth() : 0;

        fmTripAddButton.setLayoutParams(btnParams);
    }

    @Override
    public void sendFromTripAddConfirmFragment(long status) {
        switch ((int) status) {
            case -1:
                Toast.makeText(getContext(), R.string.notification_create_fail, Toast.LENGTH_SHORT).show();
                return;

            default:
                Toast.makeText(getContext(), R.string.notification_create_success, Toast.LENGTH_SHORT).show();

                fmTripAddName.setText("");
                fmTripAddDestination.setText("");
                fmTripAddStartDate.setText("");
                fmTripAddEndDate.setText("");
                fmTripAddDescription.setText("");
                fmTripAddRisk.setChecked(false);

                fmTripAddName.requestFocus();
        }
    }

    @Override
    public void sendFromDatePickerFragment(String date, String key) {
        if (key == fmTripAddStartDate.getHint().toString()) {
            fmTripAddStartDate.setText(date);
            return;
        }

        fmTripAddEndDate.setText(date);
    }

    public interface FragmentListener {
        void sendFromTripAddFragment(long status);
    }
}