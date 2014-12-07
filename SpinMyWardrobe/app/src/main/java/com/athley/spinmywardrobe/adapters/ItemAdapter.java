package com.athley.spinmywardrobe.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appacitive.core.AppacitiveBatchCall;
import com.appacitive.core.AppacitiveConnection;
import com.appacitive.core.AppacitiveFile;
import com.appacitive.core.AppacitiveGraphSearch;
import com.appacitive.core.AppacitiveObject;
import com.appacitive.core.model.AppacitiveGraphNode;
import com.appacitive.core.model.BatchCallRequest;
import com.appacitive.core.model.BatchCallResponse;
import com.appacitive.core.model.Callback;
import com.athley.spinmywardrobe.Category;
import com.athley.spinmywardrobe.R;
import com.athley.spinmywardrobe.model.Item;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sathley on 11/6/2014.
 */
public class ItemAdapter extends BaseAdapter {

    private Context mContext;

    private Category mCategory;

    private List<Item> mItems = new ArrayList<Item>();

    public ItemAdapter(Context context, Category category)
    {
        this.mContext = context;
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
        TextView name = (TextView) itemView.findViewById(R.id.category_item_name);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        final Item item = getItem(i);
        name.setText(item.getName());
        if(item.getLastWorn() == null)
            timesWorn.setText("Never worn before");
        else
        {
            if(item.getTimesWorn() == 1)
                timesWorn.setText("Worn 1 time");
            else
                timesWorn.setText("Worn " + item.getTimesWorn() + " times");
            lastWorn.setText("Last worn on " + dateFormat.format(item.getLastWorn()));
        }

        ImageButton add = (ImageButton) itemView.findViewById(R.id.category_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        addItemToCalendar(item, (new DateTime(y, m + 1, d, 0, 0, 0)).toDate());
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        ImageButton delete = (ImageButton) itemView.findViewById(R.id.category_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        AppacitiveFile.getDownloadUrlInBackground(item.getImageId(), -1, new Callback<String>() {
            @Override
            public void success(String result) {
                Picasso.with(mContext)
                        .load(result).centerCrop().fit().placeholder(R.drawable.logo)
                        .into(photo);
            }
        });

        return itemView;
    }

    private void addItemToCalendar(Item item, final Date date)
    {
        BatchCallRequest request = new BatchCallRequest();
        AppacitiveObject wore = new AppacitiveObject("wore_" + mCategory.toString());
        wore.setDateTimeProperty("wore_date", date);
        request.addNode(wore, "wore");

        AppacitiveConnection woreItem = new AppacitiveConnection("wore_" + mCategory.toString() + "_item");
        woreItem.endpointB.label = mCategory.toString();
        woreItem.endpointB.objectId = item.getItemId();
        AppacitiveConnection userWore = new AppacitiveConnection("user_wore_" + mCategory.toString());
        userWore.endpointA.label = "user";
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.shared_preference_file), Context.MODE_PRIVATE);
        userWore.endpointA.objectId = preferences.getLong("user_id", 0);
        request.addEdge(woreItem, null, "wore_" + mCategory.toString(), "wore", null, null);
        request.addEdge(userWore, null, null, null, "wore_" + mCategory.toString(), "wore");
        AppacitiveBatchCall.Fire(request, new Callback<BatchCallResponse>() {
            @Override
            public void success(BatchCallResponse result) {
                Toast.makeText(mContext, "Item added successfully!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(BatchCallResponse result, Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getItems()
    {
        String queryName = mCategory.toString().toLowerCase() + "_category";
        final SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.shared_preference_file), Context.MODE_PRIVATE);
        List<Long> ids = new ArrayList<Long>(){{add(preferences.getLong("user_id", 0));}};
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
                    if(item.getChildren("wore").size() > 0)
                    {
                        myItem.setLastWorn(item.getChildren("wore").get(0).object.getPropertyAsDateTime("wore_date"));
                    }
                    else myItem.setLastWorn(null);

                    mItems.add(myItem);
                }
                notifyDataSetChanged();
            }

            @Override
            public void failure(List<AppacitiveGraphNode> result, Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
