package com.gongloroth.justjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    private int priceOfOneCup = 150;
    private int quantity =0;
    private int price = 0;
    private String message = "Thank you!";
    private String name = "gongloroth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("QUANTITY", quantity);
        outState.putInt("PRICE", price);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        price = savedInstanceState.getInt("PRICE");
        quantity = savedInstanceState.getInt("QUANTITY");

        displayQuantity(quantity);
        displayMessage(createOrderSummary(price));
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //displayMessage("Ordered coffee amount: " + quantity + "\n" + "Total: " + formatCurrency(calculatePrice(quantity,priceOfOneCup)) + "\n" + message);
        //int price = calculatePrice(quantity,priceOfOneCup);
        //displayPrice(price);
        price = calculatePrice(quantity,priceOfOneCup);
        displayMessage(createOrderSummary(price));
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given price on the screen.
     */
    private void displayPrice(int number) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        //priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        formatter.format("%s\n%s",NumberFormat.getCurrencyInstance().format(number),message);
        //priceTextView.setText(NumberFormat.getCurrencyInstance(Locale.US).format(number) + "\nThank you!");
        orderSummaryTextView.setText(sb.toString());
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.order_summary_text_view);
        priceTextView.setText(message);
    }

    public void increment(View view){
        quantity++;
        displayQuantity(quantity);
    }

    public void decrement(View view){
        quantity--;
        displayQuantity(quantity);
    }

    private int calculatePrice(int quantity, int priceOfOneCup){
        return quantity*priceOfOneCup;
    }

    private String formatCurrency(int number){
        return NumberFormat.getCurrencyInstance().format(number);
    }

    private String createOrderSummary(int price){
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);
        fmt.format("Name: %1$s\nQuantity: %2$d\nTotal: %3$s\n%4$s",name,quantity,formatCurrency(price),message);
        return sb.toString();
    }
}
