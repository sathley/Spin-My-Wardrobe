package com.athley.spinmywardrobe;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private Category mCategory;

    private long mUserId;

    @InjectView(R.id.category_listview)
    ListView mListView;

    ItemAdapter mAdapter;

    public static CategoryFragment newInstance(Category category, long userId) {
        CategoryFragment fragment = new CategoryFragment();
        fragment.mCategory = category;
        fragment.mUserId = userId;
        return fragment;
    }

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.inject(this, rootView);
        getActivity().getActionBar().setTitle(mCategory.toString());

        mAdapter = new ItemAdapter(getActivity(), mUserId, mCategory);
        mListView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }


}
