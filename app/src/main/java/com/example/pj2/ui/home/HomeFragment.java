package com.example.pj2.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pj2.R;
import com.example.pj2.databinding.FragmentHomeBinding;

import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class HomeFragment extends Fragment {

    TextView workingTV;
    TextView resultTV;

    Button zero, one, two, three, four, five, six, seven, eight, nine, div, mul, add, sub, dot, clr, mol, pow, paren, equal;

    String texts = "";
    String formula = "";
    String tempFormula = "";
    Double right = null;

    boolean left_p = true;   //bien ghi nho co dau ngoac ben trai

    boolean checkRight = false;  // check ngoac ben phai cua mu
    Double left = null;
    boolean checkLeft = false;   // check ngoac ben trai cua mu
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        zero = root.findViewById(R.id.btn_0);
        one = root.findViewById(R.id.btn_1);
        two = root.findViewById(R.id.btn_2);
        three = root.findViewById(R.id.btn_3);
        four = root.findViewById(R.id.btn_4);
        five = root.findViewById(R.id.btn_5);
        six = root.findViewById(R.id.btn_6);
        seven = root.findViewById(R.id.btn_7);
        eight = root.findViewById(R.id.btn_8);
        nine = root.findViewById(R.id.btn_9);
        div = root.findViewById(R.id.btn_div);
        mul = root.findViewById(R.id.btn_mul);
        add = root.findViewById(R.id.add);
        sub = root.findViewById(R.id.btn_sub);
        clr = root.findViewById(R.id.btn_clear);
        dot = root.findViewById(R.id.btn_dot);
        mol = root.findViewById(R.id.btn_module);
        pow = root.findViewById(R.id.btn_power);
        paren = root.findViewById(R.id.btn_paren);
        equal = root.findViewById(R.id.btn_equal);

        workingTV = root.findViewById(R.id.workingTV);
        resultTV = root.findViewById((R.id.resultTV));

        // Tuong tac voi cac nut
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("0");
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("1");
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("2");
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("3");
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("4");
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("5");
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("6");
            }
        });
//
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("7");
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("8");
            }
        });
//
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("9");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("+");
            }
        });
//
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("-");
            }
        });

        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("*");
            }
        });

        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("/");
            }
        });

        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings(".");
            }
        });

        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearClick(view);
            }
        });

        mol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("%");
            }
        });

        pow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWorkings("^");
            }
        });

        paren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parenthesisClick(view);
            }
        });

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equalClick(view);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setWorkings(String value){
        texts = texts + value;
        workingTV.setText(texts);
    }

    public void clearClick(View view) {
        workingTV.setText("");
        texts = "";
        resultTV.setText("");
        left_p = true;
        checkRight = false;
    }

    public void parenthesisClick(View view) {
        if(left_p){
            setWorkings("(");
            left_p = false;
        }else{
            setWorkings(")");
            left_p = true;
        }
    }

    public void equalClick(View view) {
        Double result = null;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
//        checkSin();
        checkPowerOf();
        try {
            result = (double)engine.eval(formula);
        } catch (ScriptException e) {
            Toast.makeText(getContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
        }

        if (result != null)
            resultTV.setText(String.valueOf(result.doubleValue()));
    }

    private void checkPowerOf() {
        ArrayList<Integer> indexOfPowers = new ArrayList<>();
        for(int i = 0; i < texts.length(); i++)
        {
            if (texts.charAt(i) == '^')
                indexOfPowers.add(i);
        }

        formula = texts;
        tempFormula = texts;
        for(Integer index: indexOfPowers)
        {
            changeFormula(index);
        }
        formula = tempFormula;
    }

    private void changeFormula(Integer index) {
        String numberLeft = "";
        String numberRight = "";

        for(int i = index + 1; i< texts.length(); i++)
        {
            if(isNumeric(texts.charAt(i)))
                numberRight = numberRight + texts.charAt(i);
            else
                break;
        }

        if (texts.charAt(index+1) == '('){
            for(int i = index + 2; i < texts.length(); i++){
                if (texts.charAt(i) == ')')
                    break;
                numberRight = numberRight + texts.charAt(i);
            }
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
            try {
                right = (double)engine.eval(numberRight);
            } catch (ScriptException e) {
                Toast.makeText(getContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
            }
            numberRight = "("+numberRight+")";
            checkRight = true;
        }

        for(int i = index - 1; i >= 0; i--)
        {
            if(isNumeric(texts.charAt(i)))
                numberLeft = texts.charAt(i) + numberLeft;
            else
                break;
        }

        if (texts.charAt(index-1) == ')'){
            for(int i = index - 2; i >= 0; i--){
                if (texts.charAt(i) == '(')
                    break;
                numberLeft = texts.charAt(i) + numberLeft;
            }
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
            try {
                left = (double)engine.eval(numberLeft);
            } catch (ScriptException e) {
                Toast.makeText(getContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
            }
            numberLeft = "("+numberLeft+")";
            checkLeft = true;
        }
        String original = numberLeft + "^" + numberRight;
        String changed;
        if(checkRight){
            changed = "Math.pow("+numberLeft+","+right+")";
        }
        else if (checkLeft) {
            changed = "Math.pow("+left+","+numberRight+")";
        }
        else if (checkRight&&checkLeft){
            changed = "Math.pow("+left+","+right+")";
        }
        else {
            changed = "Math.pow(" + numberLeft + "," + numberRight + ")";
        }
        tempFormula = tempFormula.replace(original,changed);
    }

    public boolean isNumeric(char c){
        if((c <= '9' && c >= '0') || c == '.')
            return true;

        return false;
    }

}