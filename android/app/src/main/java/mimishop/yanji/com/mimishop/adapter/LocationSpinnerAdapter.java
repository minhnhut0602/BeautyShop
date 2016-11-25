package mimishop.yanji.com.mimishop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.modal.Location;

/**
 * Created by KCJ on 4/6/2015.
 */
public class LocationSpinnerAdapter extends ArrayAdapter<Location>
{
    private Context context;
    // android.R.layout.simple_list_item_
    int rowResourceId;
    public LocationSpinnerAdapter(Context context, int textViewResourceId, ArrayList<Location> items)
    {
        super( context, textViewResourceId, items);
        this.context = context;
        rowResourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        if(v == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate( rowResourceId, null);
        }

        Location rm = getItem( position);
        String boardTitle = rm.getLocationName();
        TextView cpText = (TextView) v.findViewById( android.R.id.text1);
        cpText.setTextSize( 14);
        cpText.setText( boardTitle);
        return v;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // notifyDataSetChanged();
        View v = convertView;
        if(v == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate( android.R.layout.simple_spinner_dropdown_item, null);
        }

        Location rm = getItem( position);
        String boardTitle = rm.getLocationName();

        TextView cpText = (TextView) v.findViewById( android.R.id.text1);

        cpText.setText( boardTitle);
        return v;
    }
}