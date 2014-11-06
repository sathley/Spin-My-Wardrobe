package com.athley.spinmywardrobe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.core.AppacitiveFile;
import com.appacitive.core.AppacitiveGraphSearch;
import com.appacitive.core.AppacitiveObject;
import com.appacitive.core.model.AppacitiveGraphNode;
import com.appacitive.core.model.Callback;
import com.athley.spinmywardrobe.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley on 11/6/2014.
 */
public class ItemAdapter extends BaseAdapter {

    private Context mContext;

    private long mUserId;

    private Category mCategory;

    private List<Item> mItems = new ArrayList<Item>();

    public ItemAdapter(Context context, long userId, Category category)
    {
        this.mContext = context;
        this.mUserId = userId;
        this.mCategory = category;
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
        View itemView = inflater.inflate(R.layout.category_item, null, false);
        final ImageView photo = (ImageView) itemView.findViewById(R.id.category_item_image);
        TextView timesWorn = (TextView) itemView.findViewById(R.id.category_item_times_worn);
        TextView lastWorn = (TextView) itemView.findViewById(R.id.category_item_last_worn);

        Item item = getItem(i);
        if(item.getLastWorn() == null)
            lastWorn.setText("Never worn before");
        else
            lastWorn.setText("Last worn on " + item.getLastWorn());

        timesWorn.setText(item.getTimesWorn() + " times worn");
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

    private void getItems()
    {
        String queryName = mCategory.toString().toLowerCase() + "_category";
        List<Long> ids = new ArrayList<Long>(){{add(mUserId);}};
        AppacitiveGraphSearch.projectQueryInBackground(queryName, ids, null, new Callback<List<AppacitiveGraphNode>>() {
            @Override
            public void success(List<AppacitiveGraphNode> result) {
                List<AppacitiveGraphNode> items = result.get(0).getChildren(mCategory.toString().toLowerCase());
                for(AppacitiveGraphNode item: items)
                {
                    Item myItem = new Item();
                    myItem.setCategory(mCategory);
                    myItem.setImageId(item.object.getPropertyAsString("picture"));
                    myItem.setItemId(item.object.getId());
                    myItem.setName(item.object.getPropertyAsString("name"));
                    myItem.setTimesWorn(item.getChildren("wore").size());
                    if(item.getChildren("worn").size() > 0)
                    {
                        myItem.setLastWorn(item.getChildren("worn").get(0).object.getUtcDateCreated());
                    }
                    else myItem.setLastWorn(null);

                    mItems.add(myItem);
                }
                notifyDataSetChanged();
            }

            @Override
            public void failure(List<AppacitiveGraphNode> result, Exception e) {
                super.failure(result, e);
            }
        });
    }
}
