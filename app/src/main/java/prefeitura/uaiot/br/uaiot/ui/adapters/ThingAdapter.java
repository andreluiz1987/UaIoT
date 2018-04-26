package prefeitura.uaiot.br.uaiot.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import prefeitura.uaiot.br.uaiot.R;
import prefeitura.uaiot.br.uaiot.models.Thing;

/**
 * Created by AndreCoelho on 24/04/2018.
 */

public class ThingAdapter extends RecyclerView.Adapter<ThingAdapter.ThingViewHolder> {

    private String[] arrColors = {"#2D7777", "#425EFF", "#5B7F00", "#7F3300", "#57007F", "#808080", "#8559FF", "#404040"};
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

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(ThingViewHolder holder, int position) {

        Thing thing = thingList.get(position);

        holder.txtCarDescription.setText(thing.getDescription());
        holder.txtCarPlate.setText(thing.getName());
        holder.txtItemRow.setText(String.valueOf(thing.getName().charAt(0)).toUpperCase());

        GradientDrawable gd = (GradientDrawable) holder.txtItemRow.getBackground();
        //To shange the solid color
        gd.setColor(Color.parseColor(getRandom(arrColors)));
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

    private String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    class ThingViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCarDescription;
        public TextView txtCarPlate;
        public TextView txtItemRow;

        public ThingViewHolder(View itemView) {
            super(itemView);

            txtCarDescription = itemView.findViewById(R.id.txtCarName);
            txtCarPlate = itemView.findViewById(R.id.txtCarPlate);
            txtItemRow = itemView.findViewById(R.id.txt_item_row_thing);
        }
    }
}
