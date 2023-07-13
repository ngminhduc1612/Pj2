package com.example.pj2.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pj2.R;
import com.example.pj2.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    EditText input;
    TextView result;
    Spinner from;
    Spinner to;
    Button cv;
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        GalleryViewModel galleryViewModel =
//                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        input = root.findViewById(R.id.etFrom);
        from = root.findViewById(R.id.spFromCurrency);
        to = root.findViewById(R.id.spToCurrency);
        result = root.findViewById(R.id.tvResult);
        cv = root.findViewById(R.id.btnConvert);

        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        update();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void update() {
        double fromRate = 1;
        double toRate = 1;
        double in = Double.parseDouble(input.getText().toString());
        if (!input.getText().toString().equals("") && !from.getSelectedItem().toString().equals("")) {
            switch (from.getSelectedItem().toString()) {
                case "KM":
                    fromRate = 1000000;
                    break;
                case "HM":
                    fromRate = 100000;
                    break;
                case "DAM":
                    fromRate = 10000;
                    break;
                case "M":
                    fromRate = 1000;
                    break;
                case "DM":
                    fromRate = 100;
                    break;
                case "CM":
                    fromRate = 10;
                    break;
                case "MM":
                    fromRate = 1;
                    break;
            }
        }

        if (!input.getText().toString().equals("") && !to.getSelectedItem().toString().equals("")) {
            switch (to.getSelectedItem().toString()) {
                case "KM":
                    toRate = 1000000;
                    break;
                case "HM":
                    toRate = 100000;
                    break;
                case "DAM":
                    toRate = 10000;
                    break;
                case "M":
                    toRate = 1000;
                    break;
                case "DM":
                    toRate = 100;
                    break;
                case "CM":
                    toRate = 10;
                    break;
                case "MM":
                    toRate = 1;
                    break;
            }
        }

        double rs = in*fromRate/toRate;
        setResult(rs);
    }

    private void setResult(double num){
        result.setText(String.valueOf(num));
    }
}