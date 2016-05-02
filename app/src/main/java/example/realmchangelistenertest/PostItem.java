package example.realmchangelistenertest;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.realm.RealmChangeListener;

/**
 * Created on 13/03/2016
 *
 * @author Matteo Zaccagnino
 */
public class PostItem extends AbstractItem<PostItem, PostItem.ViewHolder> {

    private static final ViewHolderFactory<ViewHolder> FACTORY = new ItemFactory();

    private Post mPost;
    private RealmChangeListener mRealmChangeListener;
    private PostItemListener mPostItemListener;

    public PostItem withPost(Post post) {
        mPost = post;
        return this;
    }

    public PostItem withListener(PostItemListener listener) {
        mPostItemListener = listener;
        mRealmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                Log.d("PostItem", "onChange() triggered");
                mPostItemListener.onTitleChanged(PostItem.this);
            }
        };
        mPost.addChangeListener(mRealmChangeListener);
        return this;
    }

    @Override
    public int getType() {
        return R.id.fast_adapter_post_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.support_simple_spinner_dropdown_item;
    }

    @Override
    public void bindView(ViewHolder holder) {
        super.bindView(holder);
        holder.textView.setText(mPost.getTitle());
    }

    @Override
    public ViewHolderFactory<? extends ViewHolder> getFactory() {
        return FACTORY;
    }

    public interface PostItemListener {
        void onTitleChanged(PostItem item);
    }

    protected static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
