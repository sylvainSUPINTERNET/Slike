package fr.supinternet.slike;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by guillaume on 26/09/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;

    private ArrayList<Message> messages = new ArrayList<>();

    public Adapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.tvName.setText(message.getUser());
        holder.tvMessage.setText(message.getMessage());
    }

    public void addMessage(Message message){
        messages.add(0, message);
        notifyDataSetChanged();
        System.out.println("messages" + messages);
    }

    public void removeMessage(Message message){
        messages.remove(message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvMessage;

        public ViewHolder(View itemView){
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
        }
    }
}
