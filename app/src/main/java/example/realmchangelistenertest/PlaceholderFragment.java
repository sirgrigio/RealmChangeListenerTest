package example.realmchangelistenertest;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements PostItem.PostItemListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private FastItemAdapter<PostItem> mFastItemAdapter;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        mFastItemAdapter = new FastItemAdapter<>();

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.section_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mFastItemAdapter);

        final DomainController domainController = DomainController.getInstance(getContext());
        mFastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<PostItem>() {
            @Override
            public boolean onClick(View v, IAdapter<PostItem> adapter, PostItem item, int position) {
                Post modifiedPost = new Post(1, "Post1 " + new Random().nextInt());
                domainController.save(modifiedPost); // this should trigger the RealmChangeListener
                return true;
            }
        });

        Post realmPost = domainController.save(new Post(1, "Post1"));
        PostItem postItem = new PostItem().withPost(realmPost).withListener(this);
        mFastItemAdapter.add(postItem);

        return rootView;
    }

    @Override
    public void onTitleChanged(PostItem item) {
        int position = mFastItemAdapter.getAdapterPosition(item);
        if (position >= 0) {
            mFastItemAdapter.notifyItemChanged(position);
        }
    }
}
