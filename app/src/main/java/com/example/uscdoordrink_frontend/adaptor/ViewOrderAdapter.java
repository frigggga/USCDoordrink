package com.example.uscdoordrink_frontend.adaptor;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscdoordrink_frontend.Constants.Constants;
import com.example.uscdoordrink_frontend.R;
import com.example.uscdoordrink_frontend.ViewOrderActivity;
import com.example.uscdoordrink_frontend.entity.Request;
import java.util.List;



public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.OrderViewHolder>{

    private List<Request> requests;
    private ViewOrderActivity context;

    public ViewOrderAdapter(ViewOrderActivity context, List<Request> r) {
        this.requests = r;
        this.context = context;
    }





    @NonNull
    @Override
    public ViewOrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new ViewOrderAdapter.OrderViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.textViewItemID.setText(requests.get(position).getStart().substring(0, 10));
        holder.textViewItemStatus.setText(Constants.getOrderStatus(requests.get(position).getStatus()));
        holder.textViewItemCI.setText(requests.get(position).getContactInformation());
        holder.textViewItemAddress.setText(requests.get(position).getAddress());

    }


    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewItemID, textViewItemStatus, textViewItemCI, textViewItemAddress;
        ConstraintLayout constraintLayout;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemID = itemView.findViewById(R.id.order_id);
            textViewItemStatus = itemView.findViewById(R.id.order_status);
            textViewItemCI = itemView.findViewById(R.id.order_CI);
            textViewItemAddress = itemView.findViewById(R.id.order_address);
            constraintLayout = itemView.findViewById(R.id.expandedLayout);
        }

    }
}