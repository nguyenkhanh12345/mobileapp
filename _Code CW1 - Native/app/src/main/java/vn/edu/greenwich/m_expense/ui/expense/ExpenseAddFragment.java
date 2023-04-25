package vn.edu.greenwich.m_expense.ui.expense;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import vn.edu.greenwich.m_expense.R;
import vn.edu.greenwich.m_expense.models.Expense;
import vn.edu.greenwich.m_expense.ui.dialog.DatePickerFragment;
import vn.edu.greenwich.m_expense.ui.dialog.TimePickerFragment;

public class ExpenseAddFragment extends DialogFragment
        implements DatePickerFragment.FragmentListener, TimePickerFragment.FragmentListener {
    protected EditText fmExpenseAddDate, fmExpenseAddTime, fmExpenseAddComment, fmExpenseAddAmount;
    protected Button fmExpenseAddButtonCancel, fmExpenseAddButtonAdd;
    protected Spinner fmExpenseAddType;

    public ExpenseAddFragment() {}

    @Override
    public void sendFromDatePickerFragment(String date, String key) {
        fmExpenseAddDate.setText(date);
    }

    @Override
    public void sendFromTimePickerFragment(String time) {
        fmExpenseAddTime.setText(time);
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
        View view = inflater.inflate(R.layout.fragment_expense_add, container, false);

        fmExpenseAddDate = view.findViewById(R.id.fmExpenseAddDate);
        fmExpenseAddTime = view.findViewById(R.id.fmExpenseAddTime);
        fmExpenseAddComment = view.findViewById(R.id.fmExpenseAddComment);
        fmExpenseAddAmount = view.findViewById(R.id.fmExpenseAddAmount);
        fmExpenseAddButtonCancel = view.findViewById(R.id.fmExpenseAddButtonCancel);
        fmExpenseAddButtonAdd = view.findViewById(R.id.fmExpenseAddButtonAdd);
        fmExpenseAddType = view.findViewById(R.id.fmExpenseAddType);

        fmExpenseAddButtonCancel.setOnClickListener(v -> dismiss());
        fmExpenseAddButtonAdd.setOnClickListener(v -> addRequest());

        fmExpenseAddDate.setOnTouchListener((v, motionEvent) -> showDateDialog(motionEvent));
        fmExpenseAddTime.setOnTouchListener((v, motionEvent) -> showTimeDialog(motionEvent));

        setTypeSpinner();

        return view;
    }

    protected void setTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.request_type,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fmExpenseAddType.setAdapter(adapter);
    }

    protected boolean showDateDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new DatePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    protected boolean showTimeDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new TimePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }

        return false;
    }

    protected void addRequest() {
        Expense expense = new Expense();

        expense.setType(fmExpenseAddType.getSelectedItem().toString());
        expense.setTime(fmExpenseAddTime.getText().toString());
        expense.setDate(fmExpenseAddDate.getText().toString());
        expense.setComment(fmExpenseAddComment.getText().toString());
        expense.setAmount(Double.valueOf(fmExpenseAddAmount.getText().toString()));

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromExpenseAddFragment(expense);

        dismiss();
    }

    public interface FragmentListener {
        void sendFromExpenseAddFragment(Expense expense);
    }
}