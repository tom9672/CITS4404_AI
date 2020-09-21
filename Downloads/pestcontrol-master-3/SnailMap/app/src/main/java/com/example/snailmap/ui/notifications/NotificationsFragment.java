package com.example.snailmap.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.snailmap.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private List<Object> myList = new ArrayList<>();

    private ListView listView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        listView = (ListView) root.findViewById(R.id.list_item);


        db.collection("snail-detection").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                myList.clear();

                for(DocumentSnapshot snapshot : value){

                    if(snapshot.getBoolean("HaveSnail")){
                        List<Object> myList_each = new ArrayList<>();
                        myList_each.add("DateTime: "+snapshot.getDate("DateTime"));
                        myList_each.add("\nNumberofSnails: "+snapshot.get("NumberofSnails"));
                        myList_each.add("\nCoordinate: "+snapshot.getDouble("longitude"));
                        myList_each.add(snapshot.getDouble("latitude"));
                        System.out.println(myList_each);
                        myList.add(myList_each);

                    }



                }
                if(getContext() != null){
                    listView.setAdapter(new ArrayAdapter<Object>(getContext(),
                            android.R.layout.simple_list_item_1 , removeNull(myList)));

                }

            }

        });


        return root;
    }

    public static <T> List<T> removeNull(List<? extends T> oldList) {
        List<T> listTemp = new ArrayList();
        for (int i = 0;i < oldList.size(); i++) {
            if (oldList.get(i) != null) {
                listTemp.add(oldList.get(i));
            }
        }
        return listTemp;
    }
}