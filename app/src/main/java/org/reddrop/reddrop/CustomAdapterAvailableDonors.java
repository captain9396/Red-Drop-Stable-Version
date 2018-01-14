package org.reddrop.reddrop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dipto on 1/1/18.
 */

public class CustomAdapterAvailableDonors extends ArrayAdapter<UserInfo> {


    private ImageView donorImageView;
    private TextView donorName;
    private TextView donorContactNumber;
    private TextView donorLocation;
    private ImageButton callDonorImageButton;
    private Bitmap my_image;
    private Context context;


    public CustomAdapterAvailableDonors(@NonNull Context context, @LayoutRes int resource,
        List<UserInfo> items) {
        super(context, resource, items);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_donor_list_row, parent, false);

        final UserInfo singleRowItems = getItem(position);


        donorImageView = (ImageView)customView.findViewById(R.id.donor_picture_custom_row);
        donorName = (TextView)customView.findViewById(R.id.name_custom_row);
        donorContactNumber = (TextView) customView.findViewById(R.id.contact_number_custom_row);
        donorLocation =  (TextView)customView.findViewById(R.id.location_custom_row);
        callDonorImageButton = (ImageButton)customView.findViewById(R.id.call_button_custom_row);



//        callDonorImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                System.out.println("YOU CLICKED ON = " + singleRowItems.getUsername());
//            }
//        });

        setFieldsOfCustomRow(singleRowItems);


        return customView;
    }

    public CustomAdapterAvailableDonors(@NonNull Context context, ArrayList donors) {
        super(context, R.layout.custom_donor_list_row, donors);
    }

    private void setFieldsOfCustomRow(UserInfo singleRowItems){
        donorName.setText(singleRowItems.getName());
        donorLocation.setText(singleRowItems.getLocation());
        donorContactNumber.setText("+88"+singleRowItems.getContact());
    }



//    private void downloadProfilePhoto() throws Exception{
//
//        StorageReference imageReference = mStorageRef.child("profile_photo_red_drop/"+  currentUserId + ".jpg");
//        final File localFile = File.createTempFile("images", "jpg");
//
//        imageReference.getFile(localFile)
//                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                        profilePictureImageView.setImageBitmap(my_image);
//                        loadingProfileProgressDialog.dismiss();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                loadingProfileProgressDialog.dismiss();
//            }
//        });
//
//    }


}