package com.example.pj2.ui.slideshow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pj2.MainActivity;
import com.example.pj2.R;
import com.example.pj2.databinding.FragmentSlideshowBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    TextView convert_from_dropdown_menu, convert_to_dropdown_menu, conversion_rate;
    EditText edit_amount_to_convert_value;
    ArrayList<String> arraylist;
    Dialog from_dialog, to_dialog;
    Button conversion, exit;
    String convert_from_value, convert_to_value, conversion_value;
    String[] currency = {"AUD",
            "BGN", "BRL",
            "CAD", "CHF", "CNY", "CZK",
            "DKK",
            "EUR",
            "GBP",
            "HKD", "HRK", "HUF",
            "IDR", "ILS", "INR", "ISK",
            "JPY",
            "KRW",
            "MXN", "MYR",
            "NOK", "NZD",
            "PHP", "PLN",
            "RON", "RUB",
            "SEK", "SGD",
            "THB", "TRY",
            "USD",
            "ZAR"};
    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        convert_from_dropdown_menu = root.findViewById(R.id.convert_from_dropdown_menu);
        convert_to_dropdown_menu = root.findViewById(R.id.convert_to_dropdown_menu);
        conversion_rate = root.findViewById(R.id.conversion_rate);
        conversion = root.findViewById(R.id.conversion);

        edit_amount_to_convert_value = root.findViewById(R.id.edit_amount_to_convert_value);

        arraylist = new ArrayList<>();
        for(String i : currency) {
            arraylist.add(i);
        }

        convert_from_dropdown_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from_dialog = new Dialog(getContext());
                from_dialog.setContentView(R.layout.from_spinner);
                from_dialog.getWindow().setLayout(650, 800);
                from_dialog.show();

                EditText edittext = from_dialog.findViewById(R.id.edit_text);
                ListView listview = from_dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arraylist);
                listview.setAdapter(adapter);

                edittext.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        convert_from_dropdown_menu.setText(adapter.getItem(position));
                        from_dialog.dismiss();
                        convert_from_value = adapter.getItem(position);
                    }
                });
            }
        });

        convert_to_dropdown_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to_dialog = new Dialog(getContext());
                to_dialog.setContentView(R.layout.to_spinner);
                to_dialog.getWindow().setLayout(650, 800);
                to_dialog.show();

                EditText edittext = to_dialog.findViewById(R.id.edit_text);
                ListView listview = to_dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arraylist);
                listview.setAdapter(adapter);

                edittext.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        convert_to_dropdown_menu.setText(adapter.getItem(position));
                        to_dialog.dismiss();
                        convert_to_value = adapter.getItem(position);
                    }
                });
            }
        });

        conversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Double edit_amount_to_convert_value = Double.valueOf(SlideshowFragment.this.edit_amount_to_convert_value.getText().toString());
                    getConversionRate(convert_from_value, convert_to_value, edit_amount_to_convert_value);
                }
                catch(Exception e) {

                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String getConversionRate(String convert_from_value, String convert_to_value, Double edit_amount_to_convert_value) {
        RequestQueue requestqueue = Volley.newRequestQueue(getActivity());
        String url = "https://api.freecurrencyapi.com/v1/latest?apikey=UtMwt26qY1Hvr8YXBc6dI4W7j7MpEbWMmDbf8QCL&currencies=" + convert_to_value + "&base_currency=" + convert_from_value;

        StringRequest stringrequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonobject = null;
                try {
                    jsonobject = new JSONObject(response);
                    String result = jsonobject.getJSONObject("data").getString(convert_to_value);
                    Double conversion_rate_value = Double.parseDouble(result);
                    conversion_value = "" + round((conversion_rate_value * edit_amount_to_convert_value), 2);
                    conversion_rate.setText(conversion_value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestqueue.add(stringrequest);
        return null;
    }

    public static double round(double value, int currency) {
        if(currency < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(currency, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}