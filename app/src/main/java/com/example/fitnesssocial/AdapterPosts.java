package com.example.fitnesssocial;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AdapterPosts extends RecyclerView.Adapter<com.example.fitnesssocial.AdapterPosts.MyHolder> {
    Context context;
    String myuid;
    private DatabaseReference likeref, postref, userRef;
    String namee, uImg;
    boolean mprocesslike = false;

    public AdapterPosts(Context context, List<ModelPost> modelPosts) {
        this.context = context;
        this.modelPosts = modelPosts;
        myuid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        likeref = FirebaseDatabase.getInstance().getReference().child("Like");
        postref = FirebaseDatabase.getInstance().getReference().child("Post");
        userRef = FirebaseDatabase.getInstance().getReference().child("User");
    }

    List<ModelPost> modelPosts;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_rows, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        final String uid = modelPosts.get(position).getUid();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    ModelUser modelUser = dataSnapshot1.getValue(ModelUser.class);
                    if(modelUser.getUid()!=null && modelUser.getUid().equals(uid)){
                        namee = modelUser.getName();
                        uImg = modelUser.getImage();
                        holder.name.setText(namee);
                        if (uImg.equals("noImage")) {
                            holder.picture.setVisibility(View.GONE);
                        } else {
                            try {
                                Glide.with(context).load(uImg).into(holder.picture);
                            } catch (Exception e) {

                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final String descc = modelPosts.get(position).getDescription();
        final String ptime = modelPosts.get(position).getPtime();
        String plike = modelPosts.get(position).getPlike();
        final String image = modelPosts.get(position).getMapImg();
        String comm = modelPosts.get(position).getPcomments();
        final String pid = modelPosts.get(position).getPtime();
        final String type = modelPosts.get(position).getEtype();
        final String cals = String.format("%.2f kcals",modelPosts.get(position).getTotal_cals());
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(ptime));
        String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        holder.type.setText(type);
        holder.cals.setText(cals);
        holder.description.setText(descc);
        holder.time.setText(timedate);
        holder.like.setText(plike + " Likes");
        holder.comments.setText(comm + " Comments");
        setLikes(holder, ptime);
        holder.image.setVisibility(View.VISIBLE);
        try {
            Glide.with(context).load(image).into(holder.image);
        } catch (Exception e) {

        }
        holder.like.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), PostLikedByActivity.class);
            intent.putExtra("pid", pid);
            holder.itemView.getContext().startActivity(intent);
        });
        holder.likebtn.setOnClickListener(v -> {
            final int plike1 = Integer.parseInt(modelPosts.get(position).getPlike());
            mprocesslike = true;
            final String postid = modelPosts.get(position).getPtime();
            likeref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (mprocesslike) {
                        if (dataSnapshot.child(postid).hasChild(myuid)) {
                            postref.child(postid).child("plike").setValue("" + (plike1 - 1));
                            likeref.child(postid).child(myuid).removeValue();
                        } else {
                            postref.child(postid).child("plike").setValue("" + (plike1 + 1));
                            likeref.child(postid).child(myuid).setValue("Liked");
                        }
                        mprocesslike = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
        holder.comment.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailsActivity.class);
            intent.putExtra("pid", ptime);
            context.startActivity(intent);
        });
        holder.more.setOnClickListener(v -> showMoreOptions(holder.more, uid, myuid, ptime, image));
        holder.comment.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailsActivity.class);
            intent.putExtra("pid", ptime);
            context.startActivity(intent);
        });
    }

    private void showMoreOptions(ImageButton more, String uid, String myuid, final String pid, final String image) {
        PopupMenu popupMenu = new PopupMenu(context, more, Gravity.END);
        if (uid.equals(myuid)) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "DELETE");
        }
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 0) {
                deletewithImage(pid, image);
            }
            return false;
        });
        popupMenu.show();
    }

    private void deletewithImage(final String pid, String image) {
        StorageReference picref = FirebaseStorage.getInstance().getReferenceFromUrl(image);
        picref.delete().addOnSuccessListener(aVoid -> {
            Query query = FirebaseDatabase.getInstance().getReference("Post").orderByChild("ptime").equalTo(pid);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        dataSnapshot1.getRef().removeValue();
                    }
                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }).addOnFailureListener(e -> {

        });
    }

    private void setLikes(final MyHolder holder, final String pid) {
        likeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(pid).hasChild(myuid)) {
                    holder.likebtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked_smaller, 0, 0, 0);
                } else {
                    holder.likebtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_smaller, 0, 0, 0);
                    holder.likebtn.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blue_dark_cornflower)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelPosts.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView picture, image;
        TextView name, time, description, like, comments, type, cals;
        ImageButton more;
        Button likebtn, comment;
        LinearLayout profile;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.profilePic);
            image = itemView.findViewById(R.id.pimagetv);
            name = itemView.findViewById(R.id.unametv);
            time = itemView.findViewById(R.id.utimetv);
            more = itemView.findViewById(R.id.morebtn);
            description = itemView.findViewById(R.id.descript);
            like = itemView.findViewById(R.id.plikeb);
            comments = itemView.findViewById(R.id.pcommentco);
            likebtn = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            profile = itemView.findViewById(R.id.profileLayout);
            type = itemView.findViewById(R.id.textType);
            cals = itemView.findViewById(R.id.textCalories);
        }
    }
}
