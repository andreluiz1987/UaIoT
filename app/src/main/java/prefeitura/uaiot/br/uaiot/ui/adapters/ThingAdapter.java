package prefeitura.uaiot.br.uaiot.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import prefeitura.uaiot.br.uaiot.R;
import prefeitura.uaiot.br.uaiot.models.Thing;

/**
 * Created by AndreCoelho on 24/04/2018.
 */

public class ThingAdapter extends RecyclerView.Adapter<ThingAdapter.ThingViewHolder> {

    private Context context;
    private List<Thing> thingList;

    public ThingAdapter(Context context, List<Thing> thingList) {
        this.context = context;
        this.thingList = thingList;
    }

    @Override
    public ThingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thing_row, parent, false);

        return new ThingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThingViewHolder holder, int position) {

        Thing thing = thingList.get(position);

        holder.txtCarDescription.setText(thing.getDescription());
        holder.txtCarPlate.setText(thing.getName());
    }

    @Override
    public int getItemCount() {
        return thingList.size();
    }

    public void updateList(List<Thing> thingList) {
        this.thingList = thingList;
        notifyDataSetChanged();
    }

    public List<Thing> getList() {
        return thingList;
    }

    class ThingViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCarDescription;
        public TextView txtCarPlate;

        public ThingViewHolder(View itemView) {
            super(itemView);

            txtCarDescription = itemView.findViewById(R.id.txtCarName);
            txtCarPlate = itemView.findViewById(R.id.txtCarPlate);
        }
    }
}
