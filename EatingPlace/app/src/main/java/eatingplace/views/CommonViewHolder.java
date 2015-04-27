package eatingplace.views;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Uuser on 2015/4/27.
 */
public class CommonViewHolder extends SparseArray<View> {

    /**
     *
     * @param view = the View from the Adapter which the ListView used
     * @return return a ViewHolder to the Adapter
     */
    public static CommonViewHolder get(View view){
        CommonViewHolder viewHolder= (CommonViewHolder) view.getTag();
        if(viewHolder==null){
            viewHolder=new CommonViewHolder();
            view.setTag(viewHolder);
        }
        return viewHolder;
    }

    public View get(CommonViewHolder commonViewHolder,View currentView,int key) {
        // TODO Auto-generated method stub
        View view=get(key);
        if(view==null){
            view=currentView.findViewById(key);
            //using SparseArray to replace HashMap<Integer, Object>
            commonViewHolder.put(key, view);
        }
        return view;
    }
}


