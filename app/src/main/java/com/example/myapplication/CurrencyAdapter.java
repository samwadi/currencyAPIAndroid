package com.example.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {
    private List<Currency> currencies;

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        if (currencies != null && position < currencies.size()) {
            Currency currency = currencies.get(position);
            holder.bind(currency);
        }
    }

    @Override
    public int getItemCount() {
        return currencies != null ? currencies.size() : 0;
    }

    static class CurrencyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewCurrencyName;
        private TextView textViewExchangeRate;

        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCurrencyName = itemView.findViewById(R.id.textViewCurrencyName);
            textViewExchangeRate = itemView.findViewById(R.id.textViewExchangeRate);
        }

        public void bind(Currency currency) {
            textViewCurrencyName.setText(currency.getName());
            textViewExchangeRate.setText("Exchange Rate: " + currency.getExchangeRate());
        }
    }
}
