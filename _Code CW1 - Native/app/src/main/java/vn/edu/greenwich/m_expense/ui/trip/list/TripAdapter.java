package vn.edu.greenwich.m_expense.ui.trip.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import vn.edu.greenwich.m_expense.R;
import vn.edu.greenwich.m_expense.models.Trip;
import vn.edu.greenwich.m_expense.ui.trip.TripDetailFragment;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> implements Filterable {
    protected ArrayList<Trip> _originalList;
    protected ArrayList<Trip> _filteredList;
    protected TripAdapter.ItemFilter _itemFilter = new TripAdapter.ItemFilter();

    public TripAdapter(ArrayList<Trip> list) {
        _originalList = list;
        _filteredList = list;
    }

    public void updateList(ArrayList<Trip> list) {
        _originalList = list;
        _filteredList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_trip, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = _filteredList.get(position);

        holder.listItemTripName.setText(trip.getName());
        holder.listItemTripDestination.setText(trip.getDestination());
        holder.listItemTripDate.setText(trip.getStartDate() + " - " + trip.getEndDate());
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
        protected LinearLayout listItemTrip;
        protected TextView listItemTripName, listItemTripDate, listItemTripDestination;

        public ViewHolder(View itemView) {
            super(itemView);

            listItemTripName = itemView.findViewById(R.id.listItemTripName);
            listItemTripDate = itemView.findViewById(R.id.listItemTripDate);
            listItemTripDestination = itemView.findViewById(R.id.listItemTripDestination);

            listItemTrip = itemView.findViewById(R.id.listItemTrip);
            listItemTrip.setOnClickListener(v -> showDetail(v));
        }

        protected void showDetail(View view) {
            Trip trip = _filteredList.get(getAdapterPosition());

            Bundle bundle = new Bundle();
            bundle.putSerializable(TripDetailFragment.ARG_PARAM_TRIP, trip);

            Navigation.findNavController(view).navigate(R.id.tripDetailFragment, bundle);
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<Trip> list = _originalList;
            final ArrayList<Trip> nlist = new ArrayList<>(list.size());

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            for (Trip trip : list) {
                String filterableString = trip.toString();
                if (filterableString.toLowerCase().contains(filterString)) nlist.add(trip);
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _filteredList = (ArrayList<Trip>) results.values;
            notifyDataSetChanged();
        }
    }
}