package com.gongloroth.justjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Formatter;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    private final int priceOfOneCup = 150;
    private final int WHIPPED_CREAM_PRICE = 30;
    private final int CHOCOLATE_PRICE = 25;
    private int quantity = 0;
    private int price = 0;
    private String message = "Thank you!";
    private String name = "gongloroth";
    private boolean hasWhippedCream = false;
    private boolean hasChocolate = false;
    private CheckBox whippedCreamCheckBox;
    private CheckBox chocolateCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setChocolateCheckBox(R.id.chocolate_checkbox);
        setWhippedCreamCheckBox(R.id.whipped_cream_checkbox);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("QUANTITY", quantity);
        outState.putInt("PRICE", price);
        outState.putBoolean("WHIPPED_KEY", hasWhippedCream);
        outState.putBoolean("CHOCOLATE_KEY", hasChocolate);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        price = savedInstanceState.getInt("PRICE");
        quantity = savedInstanceState.getInt("QUANTITY");
        hasWhippedCream = savedInstanceState.getBoolean("WHIPPED_KEY");
        hasChocolate = savedInstanceState.getBoolean("CHOCOLATE_KEY");

        getWhippedCreamCheckBox().setChecked(hasWhippedCream);
        getChocolateCheckBox().setChecked(hasChocolate);

        displayQuantity(quantity);
        if (price > 0) {
            displayMessage(createOrderSummary(price, hasWhippedCream, hasChocolate));
        }
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //displayMessage("Ordered coffee amount: " + quantity + "\n" + "Total: " + formatCurrency(calculatePrice(quantity,priceOfOneCup)) + "\n" + message);
        //int price = calculatePrice(quantity,priceOfOneCup);
        //displayPrice(price);
        if (hasWhippedCream && hasChocolate) {
            price = calculatePrice(quantity, priceOfOneCup) + WHIPPED_CREAM_PRICE * quantity + CHOCOLATE_PRICE * quantity;

        } else if (hasWhippedCream && !hasChocolate) {
            price = calculatePrice(quantity, priceOfOneCup) + WHIPPED_CREAM_PRICE * quantity;

        } else if (hasChocolate && !hasWhippedCream) {
            price = calculatePrice(quantity, priceOfOneCup) + CHOCOLATE_PRICE * quantity;

        } else {
            price = calculatePrice(quantity, priceOfOneCup);

        }
        displayMessage(createOrderSummary(price, hasWhippedCream, hasChocolate));
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
        formatter.format("%s\n%s", NumberFormat.getCurrencyInstance().format(number), message);
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

    public void increment(View view) {
        quantity++;
        displayQuantity(quantity);
    }

    public void decrement(View view) {
        quantity--;
        displayQuantity(quantity);
    }

    private int calculatePrice(int quantity, int priceOfOneCup) {
        return quantity * priceOfOneCup;
    }

    private String formatCurrency(int number) {
        return NumberFormat.getCurrencyInstance().format(number);
    }

    private String createOrderSummary(int price, boolean creamIsChecked, boolean chocolateIsChecked) {
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);
        fmt.format("Name: %1$s\nQuantity: %2$d\nTotal: %3$s\n%4$s", name, quantity, formatCurrency(price), message);
        Log.v("MainActivity", "Add whipped cream? " + creamIsChecked);
        Log.v("MainActivity", "Add chocolate? " + chocolateIsChecked);
        if (creamIsChecked) {
            sb.insert(sb.indexOf("\n") + 1, "Whipped cream added for " + formatCurrency(WHIPPED_CREAM_PRICE * quantity) + ".\n");
        }
        if (chocolateIsChecked) {
            sb.insert(sb.indexOf("\n") + 1, "Chocolate added for " + formatCurrency(CHOCOLATE_PRICE * quantity) + ".\n");
        }

        return sb.toString();
    }

    private String createOrderSummary(int price) {
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);
        fmt.format("Name: %1$s\nQuantity: %2$d\nTotal: %3$s\n%4$s", name, quantity, formatCurrency(price), message);

        return sb.toString();
    }


    public CheckBox getWhippedCreamCheckBox() {
        return whippedCreamCheckBox;
    }

    public void setWhippedCreamCheckBox(int id) {
        this.whippedCreamCheckBox = (CheckBox) findViewById(id);
    }

    public CheckBox getChocolateCheckBox() {
        return chocolateCheckBox;
    }

    public void setChocolateCheckBox(int id) {
        this.chocolateCheckBox = (CheckBox) findViewById(id);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.chocolate_checkbox:
                if (checked) {
                    hasChocolate = true;
                } else {
                    hasChocolate = false;
                }

                break;
            case R.id.whipped_cream_checkbox:
                if (checked) {
                    hasWhippedCream = true;
                } else {
                    hasWhippedCream = false;
                }

                break;

        }
    }

}
