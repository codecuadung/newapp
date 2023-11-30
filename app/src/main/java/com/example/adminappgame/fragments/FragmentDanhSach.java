package com.example.adminappgame.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminappgame.R;

public class FragmentDanhSach extends Fragment {

    public FragmentDanhSach() {

    }
    public static FragmentDanhSach newInstance() {
        FragmentDanhSach fragment = new FragmentDanhSach();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_danh_sach, container, false);
    }
}