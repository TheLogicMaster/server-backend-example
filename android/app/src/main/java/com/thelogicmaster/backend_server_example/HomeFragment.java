package com.thelogicmaster.backend_server_example;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    private TextView helloText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helloText = view.findViewById(R.id.hello);

        ((Button) view.findViewById(R.id.chatButton)).setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_messagesFragment, null));
        ((Button) view.findViewById(R.id.logoutButton)).setOnClickListener(v -> {
            SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
            editor.putString("username", null);
            editor.apply();
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        String username = getActivity().getPreferences(Context.MODE_PRIVATE).getString("username", null);
        if (username == null)
            NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_loginFragment);
        else
            helloText.setText("Welcome, " + username);
    }
}