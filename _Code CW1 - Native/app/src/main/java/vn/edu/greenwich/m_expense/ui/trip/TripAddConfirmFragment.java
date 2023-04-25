package vn.edu.greenwich.m_expense.ui.trip;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import vn.edu.greenwich.m_expense.R;
import vn.edu.greenwich.m_expense.database.MExpenseDAO;
import vn.edu.greenwich.m_expense.models.Trip;

public class TripAddConfirmFragment extends DialogFragment {
    protected MExpenseDAO _db;
    protected Trip _trip;
    protected Button fmTripAddConfirmButtonConfirm, fmTripAddConfirmButtonCancel;
    protected TextView fmTripAddConfirmName, fmTripAddConfirmDestination, fmTripAddConfirmStartDate, fmTripAddConfirmEndDate, fmTripAddConfirmDescription, fmTripAddConfirmRisk;

    public TripAddConfirmFragment() {
        _trip = new Trip();
    }

    public TripAddConfirmFragment(Trip trip) {
        _trip = trip;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new MExpenseDAO(getContext());
    }

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
        View view = inflater.inflate(R.layout.fragment_trip_add_confirm, container, false);

        String name = getString(R.string.error_no_info);
        String destination = getString(R.string.error_no_info);
        String startDate = getString(R.string.error_no_info);
        String endDate = getString(R.string.error_no_info);
        String description = getString(R.string.error_no_info);
        String risk = getString(R.string.error_no_info);

        fmTripAddConfirmName = view.findViewById(R.id.fmTripAddConfirmName);
        fmTripAddConfirmDestination = view.findViewById(R.id.fmTripAddConfirmDestination);
        fmTripAddConfirmStartDate = view.findViewById(R.id.fmTripAddConfirmStartDate);
        fmTripAddConfirmEndDate = view.findViewById(R.id.fmTripAddConfirmEndDate);
        fmTripAddConfirmDescription = view.findViewById(R.id.fmTripAddConfirmDescription);
        fmTripAddConfirmRisk = view.findViewById(R.id.fmTripAddConfirmRisk);
        fmTripAddConfirmButtonCancel = view.findViewById(R.id.fmTripAddConfirmButtonCancel);
        fmTripAddConfirmButtonConfirm = view.findViewById(R.id.fmTripAddConfirmButtonConfirm);

        if (_trip.getRisk() != -1) {
            risk = _trip.getRisk() == 1 ? getString(R.string.label_yes) : getString(R.string.label_no);
        }

        if (_trip.getName() != null && !_trip.getName().trim().isEmpty()) {
            name = _trip.getName();
        }

        if (_trip.getDestination() != null && !_trip.getDestination().trim().isEmpty()) {
            destination = _trip.getDestination();
        }

        if (_trip.getStartDate() != null && !_trip.getStartDate().trim().isEmpty()) {
            startDate = _trip.getStartDate();
        }

        if (_trip.getEndDate() != null && !_trip.getEndDate().trim().isEmpty()) {
            endDate = _trip.getEndDate();
        }

        if (_trip.getDescription() != null && !_trip.getDescription().trim().isEmpty()) {
            description = _trip.getDescription();
        }

        fmTripAddConfirmName.setText(name);
        fmTripAddConfirmDestination.setText(destination);
        fmTripAddConfirmStartDate.setText(startDate);
        fmTripAddConfirmEndDate.setText(endDate);
        fmTripAddConfirmDescription.setText(description);
        fmTripAddConfirmRisk.setText(risk);

        fmTripAddConfirmButtonCancel.setOnClickListener(v -> dismiss());
        fmTripAddConfirmButtonConfirm.setOnClickListener(v -> confirm());

        return view;
    }

    protected void confirm() {
        long status = _db.insertTrip(_trip);

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromTripAddConfirmFragment(status);

        dismiss();
    }

    public interface FragmentListener {
        void sendFromTripAddConfirmFragment(long status);
    }
}