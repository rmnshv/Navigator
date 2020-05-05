package icst.spbstu.ru.navigatoricst.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.listeners.ListItemClickListener;

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.ViewHolder> {

    private Context mContext;
    private Activity mActivity;

    private ArrayList<String> mItemList;
    private ArrayList<String> mItemCodes;
    private ListItemClickListener mItemClickListener;

    public DirectionsAdapter(Context mContext, Activity mActivity, ArrayList<String> mItemList, ArrayList<String> mItemCodes) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mItemList = mItemList;
        this.mItemCodes = mItemCodes;
    }

    public void setItemClickListener(ListItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dir, parent, false);
        return new ViewHolder(view, viewType, mItemClickListener);
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvDirName;
        private TextView tvDirCode;
        private ListItemClickListener itemClickListener;

        public ViewHolder(View itemView, int viewType, ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;

            tvDirName = (TextView)itemView.findViewById(R.id.tvDirName);
            tvDirCode = (TextView)itemView.findViewById(R.id.tvDirCode);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), v);
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
        final String dirCode = mItemCodes.get(position);
        // setting data over views
        mainHolder.tvDirName.setText(dirName);
        mainHolder.tvDirCode.setText(dirCode);
    }

}
