package icst.spbstu.ru.navigatoricst.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.listeners.ListItemClickListener;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private Context mContext;
    private Activity mActivity;

    private ArrayList<String> mItemList;
    private final int[] mColors;
    private ListItemClickListener mItemClickListener;

    public ResultAdapter(Context mContext, Activity mActivity, ArrayList<String> mItemList, int[] mColors) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mItemList = mItemList;
        this.mColors = mColors;
    }

    public void setItemClickListener(ListItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_res, parent, false);
        return new ViewHolder(view, viewType, mItemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvDirName;
        ListItemClickListener itemClickListener;

        public ViewHolder(View itemView, int viewType, ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            tvDirName = (TextView)itemView.findViewById(R.id.tvDirName);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != mItemList ? mItemList.size() : 0);

    }

    @Override
    public void onBindViewHolder(ViewHolder mainHolder, int position) {
        final String dirName = mItemList.get(position);
        // setting data over views
        mainHolder.tvDirName.setText(dirName);
        mainHolder.tvDirName.setTextColor(mColors[position]);
    }
}
