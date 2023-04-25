package vn.edu.greenwich.m_expense.ui.expense.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import vn.edu.greenwich.m_expense.R;
import vn.edu.greenwich.m_expense.models.Expense;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> implements Filterable {
    protected String currentDate = "";
    protected ArrayList<Expense> _originalList;
    protected ArrayList<Expense> _filteredList;
    protected ExpenseAdapter.ItemFilter _itemFilter = new ExpenseAdapter.ItemFilter();

    public ExpenseAdapter(ArrayList<Expense> list) {
        _originalList = list;
        _filteredList = list;
    }

    public void updateList(ArrayList<Expense> list) {
        _originalList = list;
        _filteredList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_expense, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = _filteredList.get(position);

        if (!currentDate.equals(expense.getDate())) {
            currentDate = expense.getDate();
            holder.listItemExpenseDate.setVisibility(View.VISIBLE);
            holder.listItemExpenseDate.setText(currentDate);
        } else {
            holder.listItemExpenseDate.setVisibility(View.GONE);
        }

        holder.listItemExpenseTime.setText(expense.getTime());
        holder.listItemExpenseType.setText(expense.getType());
        holder.listItemExpenseComment.setText(expense.getComment());
        holder.listItemExpenseAmount.setText(String.valueOf(expense.getAmount()));
    }

    @Override
    public int getItemCount() {
        return _filteredList == null ? 0 : _filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return _itemFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView listItemExpenseDate, listItemExpenseTime, listItemExpenseType, listItemExpenseAmount, listItemExpenseComment;

        public ViewHolder(View itemView) {
            super(itemView);

            listItemExpenseDate = itemView.findViewById(R.id.listItemExpenseDate);
            listItemExpenseTime = itemView.findViewById(R.id.listItemExpenseTime);
            listItemExpenseType = itemView.findViewById(R.id.listItemExpenseType);
            listItemExpenseAmount = itemView.findViewById(R.id.listItemExpenseAmount);
            listItemExpenseComment = itemView.findViewById(R.id.listItemExpenseComment);
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<Expense> list = _originalList;
            final ArrayList<Expense> nlist = new ArrayList<>(list.size());

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            for (Expense expense : list) {
                String filterableString = expense.toString();

                if (filterableString.toLowerCase().contains(filterString))
                    nlist.add(expense);
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _filteredList = (ArrayList<Expense>) results.values;
            notifyDataSetChanged();
        }
    }
}