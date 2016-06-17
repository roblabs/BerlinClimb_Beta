package de.example.navdrawemap_2.maptest.ListViewSpots;

import android.app.Activity;
import android.view.LayoutInflater;

import java.util.List;

public class CustomListFilter  {

    private String[] heads, krouten, brouten, adress, webadress, type, price, inout, material;
    private double[] lat, longC;
    private int[] imageid;
    private Activity context;

    private List<String> originalData = null;
    private List<String> filteredData = null;
    private LayoutInflater mInflater;
    // private ItemFilter mFilter = new ItemFilter();

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------
/*   extends BaseAdapter implements Filterable
    public CustomListFilter (Context context, List<String> data, String[] heads, int[] imageid, String[] type, String[] inout,
                             String[] krouten, String[] brouten, String[] material, String[] price,
                             String[] adress, double[] lat, double[] longC, String[] webadress) {
        this.filteredData = data ;
        this.originalData = data ;
        this.heads = heads;
        this.imageid = imageid;
        this.type = type;
        this.inout = inout;
        this.krouten = krouten;
        this.brouten = brouten;
        this.material = material;
        this.price = price;
        this.adress = adress;
        this.lat = lat;
        this.longC = longC;
        this.webadress = webadress;
        mInflater = LayoutInflater.from(context);
        super(context, R.layout.content_overwiev, heads);
    }

    public int getCount() {
        return filteredData.size();
    }

    public Object getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.content_overwiev, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.listView);

            // Bind the data efficiently with the holder.
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.content_overwiev, null, true);
            TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewHeads);
            ImageView imageView = (ImageView) listViewItem.findViewById(R.id.imageView);
            TextView textViewType = (TextView) listViewItem.findViewById(R.id.textViewType);
            TextView textViewInOut = (TextView) listViewItem.findViewById(R.id.textViewInOUT);
            TextView textViewKRouten = (TextView) listViewItem.findViewById(R.id.textViewKRouten);
            TextView textViewBRouten = (TextView) listViewItem.findViewById(R.id.textViewBRouten);
            TextView textViewMaterial = (TextView) listViewItem.findViewById(R.id.textViewMaterial);
            TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.textViewPrice);
            TextView textViewAdress = (TextView) listViewItem.findViewById(R.id.textViewAdress);
            TextView textViewLat = (TextView) listViewItem.findViewById(R.id.textViewLat);
            TextView textViewLong = (TextView) listViewItem.findViewById(R.id.textViewLong);
            TextView textViewWebadress = (TextView) listViewItem.findViewById(R.id.textViewWebadress);

            NumberFormat nm = NumberFormat.getNumberInstance();

            // Sets the image or a default image
            if (imageid[position] != 0) {
                imageView.setImageResource(imageid[position]);
            } else {
                imageView.setImageResource(R.drawable.ic_map_24dp);
            }
            // set the values in the text
            textViewName.setText(heads[position]);

            textViewType.setText(type[position]);
            textViewInOut.setText(inout[position]);
            textViewKRouten.setText(krouten[position]);
            textViewBRouten.setText(brouten[position]);
            textViewMaterial.setText(material[position]);
            textViewPrice.setText(price[position]);
            textViewAdress.setText(adress[position]);
            textViewLat.setText(nm.format(lat[position]));
            textViewLong.setText(nm.format(longC[position]));
            textViewWebadress.setText(webadress[position]);

          //   return listViewItem;
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // If weren't re-ordering this you could rely on what you set last time
        holder.text.setText(filteredData.get(position));

        return convertView;
    }

    static class ViewHolder {
        TextView text;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<String> list = originalData;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }

    }

    // returns the listview Item by position
    public View getViewByPosition(int pos, ListView listView) {

        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);

        }
    } */
}
