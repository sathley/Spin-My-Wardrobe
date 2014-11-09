package com.athley.spinmywardrobe.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appacitive.core.AppacitiveFile;
import com.appacitive.core.AppacitiveGraphSearch;
import com.appacitive.core.model.AppacitiveGraphNode;
import com.appacitive.core.model.AppacitiveObjectBase;
import com.appacitive.core.model.Callback;
import com.athley.spinmywardrobe.Category;
import com.athley.spinmywardrobe.R;
import com.athley.spinmywardrobe.model.Item;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sathley on 11/7/2014.
 */
public class CalendarDayItemsAdapter extends BaseAdapter {

    private Context mContext;

    private DateTime mDate;

    private List<Item> mItems = new ArrayList<Item>();

    public CalendarDayItemsAdapter(Context context, DateTime date)
    {
        this.mContext = context;
        this.mDate = date;
        getItems();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).getItemId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.calendar_item, null, false);
        final ImageView photo = (ImageView)itemView.findViewById(R.id.calendar_item_image);
        TextView name = (TextView) itemView.findViewById(R.id.calendar_name);
        Item item = getItem(i);
        name.setText(item.getName());
        AppacitiveFile.getDownloadUrlInBackground(item.getImageId(), -1, new Callback<String>() {
            @Override
            public void success(String result) {
                Picasso.with(mContext)
                        .load(result)
                        .into(photo);
            }
        });
        return itemView;
    }

    private void getItems() {
        mItems.clear();
        final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

        Map<String, String> placeholders = new HashMap<String, String>(){{
            put("date1", fmt.print(mDate));
            put("date2", fmt.print(mDate.plusDays(1)));
        }};
        final SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.shared_preference_file), Context.MODE_PRIVATE);
        List<Long> userIds = new ArrayList<Long>(){{
            add(preferences.getLong("user_id", 0));
        }};
        AppacitiveGraphSearch.projectQueryInBackground("calendar", userIds, placeholders, new Callback<List<AppacitiveGraphNode>>() {
            @Override
            public void success(List<AppacitiveGraphNode> result) {

                Map<Category, List<AppacitiveGraphNode>> items = new HashMap<Category, List<AppacitiveGraphNode>>();
                for (Category c : Category.values())
                {
                    if(result.get(0) != null && result.get(0).getChildren("wore_" + c.toString().toLowerCase()) != null)
                        items.put(c, result.get(0).getChildren("wore_" + c.toString().toLowerCase()));
                }
                for(Category category : items.keySet())
                {
                    List<AppacitiveGraphNode> values = items.get(category);
                    for (AppacitiveGraphNode value : values)
                    {
                        AppacitiveObjectBase obj = value.getChildren(category.toString().toLowerCase()).get(0).object;
                        Item item = new Item();
                        item.setImageId(obj.getPropertyAsString("picture"));
                        item.setName(obj.getPropertyAsString("name"));
                        item.setItemId(obj.getId());
//                        item.setCategory(Category.valueOf(value.object.getType()));
                        mItems.add(item);
                    }
                }
                if(mItems.size() == 0)
                    Toast.makeText(mContext, "No items for this day!", Toast.LENGTH_LONG).show();
                notifyDataSetChanged();
            }

            @Override
            public void failure(List<AppacitiveGraphNode> result, Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
