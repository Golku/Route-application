package com.example.jason.route_application.features.addressDetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.jason.route_application.R;
import com.example.jason.route_application.data.pojos.database.AddressInformation;
import com.example.jason.route_application.data.pojos.CommentInformation;

/**
 * Created by Jason on 07-Feb-18.
 */

public class AddressDetailsAdapter extends RecyclerView.Adapter <AddressDetailsAdapter.CustomViewHolder>{

    private AddressInformation addressInformation;
    private CommentListFunctions commentListFunctions;
    private CommentInformation commentInformation;

    AddressDetailsAdapter(AddressInformation addressInformation, CommentListFunctions commentListFunctions) {
        this.addressInformation = addressInformation;
        this.commentListFunctions = commentListFunctions;
        this.commentInformation = new CommentInformation();
    }

    public interface CommentListFunctions{
        void onListItemClick(CommentInformation commentInformation);
    }

    @Override
    public AddressDetailsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.employedName.setText(addressInformation.getEmployeeId().get(position));
        holder.date.setText(addressInformation.getDates().get(position));
        holder.comment.setText(addressInformation.getComments().get(position));
    }

    @Override
    public int getItemCount() {
        return addressInformation.getCommentsCount();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView employedName;
        private TextView date;
        private TextView comment;
        private ViewGroup container;

        CustomViewHolder(View itemView) {
            super(itemView);
            this.employedName = itemView.findViewById(R.id.username_tv);
            this.date = itemView.findViewById(R.id.date_tv);
            this.comment = itemView.findViewById(R.id.comment_tv);
            this.container = itemView.findViewById(R.id.root_layout);
            this.container.setOnClickListener(this);
        }

        public void onClick(View v) {
            commentInformation.setEmployeeId(addressInformation.getEmployeeId().get(this.getAdapterPosition()));
            commentInformation.setDate(addressInformation.getDates().get(this.getAdapterPosition()));
            commentInformation.setComment(addressInformation.getComments().get(this.getAdapterPosition()));
            commentListFunctions.onListItemClick(commentInformation);
        }
    }

}
