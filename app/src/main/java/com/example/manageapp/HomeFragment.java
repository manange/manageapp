package com.example.manageapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private VideoView videoView;
    ListView listView;
    ArrayList<String> videoList;
    ArrayAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // list video view
//        videoView=findViewById(R.id.videoview);
        videoView = view.findViewById(R.id.videoview);
        ListView listView = (ListView) view.findViewById(R.id.lvideo) ;
        videoList= new ArrayList<>();
        videoList.add("video");
        videoList.add("vedeo2");
        videoList.add("video3");
        videoList.add("video4");
        videoList.add("video5");
        videoList.add("videom");

//        adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1,videoList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1,videoList
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        videoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video));

                        break;
                    case 1:
                        videoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.vedeo2));
                        break;
                    case 2:
                        videoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video3));
                        break;
                    case 3:
                        videoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video4));
                        break;
                    case 4:
                        videoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.video5));
                        break;
                    case 5:
                        videoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.videom));
                        break;
                    default:
                        break;
                }
                videoView.setMediaController(new MediaController(getContext()));
                videoView.requestFocus();
                videoView.start();
            }
        });


    }
}
