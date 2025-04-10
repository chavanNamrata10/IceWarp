package com.example.icewarpassesttest.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icewarpassesttest.R;
import com.example.icewarpassesttest.databinding.ItemChannelNameBinding;
import com.example.icewarpassesttest.databinding.ItemGroupHeaderBinding;
import com.example.icewarpassesttest.modelResponse.ChannelList;

import java.util.ArrayList;
import java.util.List;

public class ChannelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> itemName;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CHANNEL_NAME = 1;

    public ChannelListAdapter(List<ChannelList> list) {
        setChannelList(list);
    }

    @Override
    public int getItemViewType(int position) {
        return itemName.get(position) instanceof String ? TYPE_HEADER : TYPE_CHANNEL_NAME;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            ItemGroupHeaderBinding binding = ItemGroupHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new GroupHeaderViewHolder(binding);
        } else {
            ItemChannelNameBinding binding = ItemChannelNameBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ChannelNameViewHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof GroupHeaderViewHolder) {
            ((GroupHeaderViewHolder)holder).binding.tvGroupHeader.setText(itemName.get(position).toString());
        }else {
            ChannelList channelResponse = (ChannelList) itemName.get(position);
            ((ChannelNameViewHolder)holder).binding.tvChannelName.setText(((ChannelList) itemName.get(position)).getName());
        }
    }

    @Override
    public int getItemCount() {
        return itemName.size();
    }

    public void updateChannelList(List<ChannelList> channelResponseList) {

        setChannelList(channelResponseList);
        Log.d("Log10", "Display Channel Response: Adapter2" + channelResponseList.get(0).getName());

    }

    private void setChannelList(List<ChannelList> channelResponseList) {
        itemName = new ArrayList<>();
        String lastGroupHolderName = "";

        if (channelResponseList != null && channelResponseList.size() > 0) {
            for (int i = 0; i < channelResponseList.size(); i++) {
                if (!channelResponseList.get(i).getGroupHolderName().equals(lastGroupHolderName)) {
                    itemName.add(channelResponseList.get(i).getGroupHolderName());
                    lastGroupHolderName = channelResponseList.get(i).getGroupHolderName();
                }
                itemName.add(channelResponseList.get(i));
            }
            Log.d("Log10", "Display Channel Response: Adapter3" + channelResponseList.get(0).getName());

        }
        notifyDataSetChanged();

    }

    private class GroupHeaderViewHolder extends RecyclerView.ViewHolder {
        ItemGroupHeaderBinding binding;
        public GroupHeaderViewHolder(ItemGroupHeaderBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;


        }
    }

    private class ChannelNameViewHolder extends RecyclerView.ViewHolder {
        ItemChannelNameBinding binding;
        public ChannelNameViewHolder(ItemChannelNameBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }
}
