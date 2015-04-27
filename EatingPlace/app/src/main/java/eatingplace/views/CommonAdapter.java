package eatingplace.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Uuser on 2015/4/27.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private ArrayList<T> mList;
    private Context mContext;
    private int resource;

    private LayoutInflater mLayoutInflater;

    public CommonAdapter(Context context, ArrayList<T> list, int res) {
        super();
        this.mContext = context;
        this.mList = list;
        this.resource = res;
        mLayoutInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub

        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    //@param item: type in the ArrayList
    public abstract void setViewData(CommonViewHolder commonViewHolder, View currentView, T item);

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        if(view==null){
            view=mLayoutInflater.inflate(resource, null);
        }

        CommonViewHolder cvh=CommonViewHolder.get(view);
        setViewData(cvh,view,getItem(position));
        return view;
    }
}

