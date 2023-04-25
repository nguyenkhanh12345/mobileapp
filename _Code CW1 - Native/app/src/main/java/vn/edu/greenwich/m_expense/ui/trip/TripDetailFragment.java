package vn.edu.greenwich.m_expense.ui.trip;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomappbar.BottomAppBar;
import vn.edu.greenwich.m_expense.R;
import vn.edu.greenwich.m_expense.database.MExpenseDAO;
import vn.edu.greenwich.m_expense.models.Expense;
import vn.edu.greenwich.m_expense.models.Trip;
import vn.edu.greenwich.m_expense.ui.dialog.DeleteConfirmFragment;
import vn.edu.greenwich.m_expense.ui.expense.ExpenseAddFragment;
import vn.edu.greenwich.m_expense.ui.expense.list.ExpenseListFragment;

public class TripDetailFragment extends Fragment
        implements DeleteConfirmFragment.FragmentListener, ExpenseAddFragment.FragmentListener {
    public static final String ARG_PARAM_TRIP = "trip";

    protected MExpenseDAO _db;
    protected Trip _trip;
    protected Button fmTripDetailExpenseButton;
    protected BottomAppBar fmTripDetailBottomAppBar;
    protected FragmentContainerView fmTripDetailExpenseList;
    protected TextView fmTripDetailName, fmTripDetailDestination, fmTripDetailDate, fmTripDetailRisk, fmTripDetailDescription;

    public TripDetailFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new MExpenseDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_detail, container, false);

        fmTripDetailName = view.findViewById(R.id.fmTripDetailName);
        fmTripDetailDestination = view.findViewById(R.id.fmTripDetailDestination);
        fmTripDetailDate = view.findViewById(R.id.fmTripDetailDate);
        fmTripDetailRisk = view.findViewById(R.id.fmTripDetailRisk);
        fmTripDetailDescription = view.findViewById(R.id.fmTripDetailDescription);
        fmTripDetailBottomAppBar = view.findViewById(R.id.fmTripDetailBottomAppBar);
        fmTripDetailExpenseButton = view.findViewById(R.id.fmTripDetailExpenseButton);
        fmTripDetailExpenseList = view.findViewById(R.id.fmTripDetailExpenseList);

        fmTripDetailBottomAppBar.setOnMenuItemClickListener(item -> menuItemSelected(item));
        fmTripDetailExpenseButton.setOnClickListener(v -> showAddExpenseFragment());

        showDetails();
        showExpenseList();

        return view;
    }

    protected void showDetails() {
        String name = getString(R.string.error_not_found);
        String destination = getString(R.string.error_not_found);
        String date = getString(R.string.error_not_found);
        String risk = getString(R.string.error_not_found);
        String description = getString(R.string.error_not_found);

        if (getArguments() != null) {
            _trip = (Trip) getArguments().getSerializable(ARG_PARAM_TRIP);
            _trip = _db.getTripById(_trip.getId()); // Retrieve data from Database.

            name = _trip.getName();
            destination = _trip.getDestination();
            date = _trip.getStartDate() + " - " + _trip.getEndDate();
            risk = _trip.getRisk() == 1 ? getString(R.string.label_yes) : getString(R.string.label_no);
            description = _trip.getDescription();
        }

        fmTripDetailName.setText(name);
        fmTripDetailDestination.setText(destination);
        fmTripDetailDate.setText(date);
        fmTripDetailRisk.setText(risk);
        fmTripDetailDescription.setText(description);
    }

    protected void showExpenseList() {
        if (getArguments() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ExpenseListFragment.ARG_PARAM_TRIP_ID, _trip.getId());

            // Send arguments (trip id) to ExpenseListFragment.
            getChildFragmentManager().getFragments().get(0).setArguments(bundle);
        }
    }

    protected boolean menuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tripUpdateFragment:
                showUpdateFragment();
                return true;

            case R.id.tripDeleteFragment:
                showDeleteConfirmFragment();
                return true;
        }

        return true;
    }

    protected void showUpdateFragment() {
        Bundle bundle = null;

        if (_trip != null) {
            bundle = new Bundle();
            bundle.putSerializable(TripUpdateFragment.ARG_PARAM_TRIP, _trip);
        }

        Navigation.findNavController(getView()).navigate(R.id.tripUpdateFragment, bundle);
    }

    protected void showDeleteConfirmFragment() {
        new DeleteConfirmFragment(getString(R.string.notification_delete_confirm)).show(getChildFragmentManager(), null);
    }

    protected void showAddExpenseFragment() {
        new ExpenseAddFragment().show(getChildFragmentManager(), null);
    }

    @Override
    public void sendFromDeleteConfirmFragment(int status) {
        if (status == 1 && _trip != null) {
            long numOfDeletedRows = _db.deleteTrip(_trip.getId());

            if (numOfDeletedRows > 0) {
                Toast.makeText(getContext(), R.string.notification_delete_success, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigateUp();

                return;
            }
        }

        Toast.makeText(getContext(), R.string.notification_delete_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendFromExpenseAddFragment(Expense expense) {
        if (expense != null) {
            expense.setTripId(_trip.getId());

            long id = _db.insertExpense(expense);

            Toast.makeText(getContext(), id == -1 ? R.string.notification_create_fail : R.string.notification_create_success, Toast.LENGTH_SHORT).show();

            reloadExpenseList();
        }
    }

    protected void reloadExpenseList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExpenseListFragment.ARG_PARAM_TRIP_ID, _trip.getId());

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fmTripDetailExpenseList, ExpenseListFragment.class, bundle)
                .commit();
    }
}