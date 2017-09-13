package example.umbrella.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import example.umbrella.DataManager.ImageItem;
import example.umbrella.R;

/**
 * Created by manojbudumuru on 9/12/17.
 */

public class TemperatureViewAdapter extends ArrayAdapter {

    private Context context;
    private int recourceID;
    private ArrayList dataArray = new ArrayList();

    public TemperatureViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        this.context = context;
        this.recourceID = resource;
        this.dataArray = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(this.recourceID, parent, false);
            holder = new ViewHolder();
            holder.itemTemperatureTime = (TextView) row.findViewById(R.id.itemTime);
            holder.itemTemperatureValue = (TextView) row.findViewById(R.id.itemTemp);
            holder.image = (ImageView) row.findViewById(R.id.itemImage);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ImageItem item = (ImageItem) dataArray.get(position);
        holder.itemTemperatureTime.setText(item.getTime());
        char tmpcValue = 0x00B0;
        String tempValue = item.getValue();
        String tempValueWithDegree = tempValue+tmpcValue;
        holder.itemTemperatureValue.setText(tempValueWithDegree);
        holder.image.setImageBitmap(item.getImage());
        return row;
    }

    static class ViewHolder {
        TextView itemTemperatureValue;
        TextView itemTemperatureTime;
        ImageView image;
    }
}
