package com.gongloroth.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Formatter;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    private final int priceOfOneCup = 150;
    private final int WHIPPED_CREAM_PRICE = 30;
    private final int CHOCOLATE_PRICE = 25;
    private int quantity = 1;
    private int price = 0;
    private String message = "Thank you!";
    private String name = "";
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
        /*
        setWhippedCreamCheckBox(R.id.whipped_cream_checkbox);
        getWhippedCreamCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    hasWhippedCream = whippedCreamCheckBox.isChecked();
            }
        });

        setChocolateCheckBox(R.id.chocolate_checkbox);
        getWhippedCreamCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    hasChocolate = chocolateCheckBox.isChecked();
            }
        }); */

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("QUANTITY", quantity);
        outState.putInt("PRICE", price);
        outState.putBoolean("WHIPPED_KEY", hasWhippedCream);
        outState.putBoolean("CHOCOLATE_KEY", hasChocolate);
        outState.putString("PERSON_NAME", name);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        price = savedInstanceState.getInt("PRICE");
        quantity = savedInstanceState.getInt("QUANTITY");
        hasWhippedCream = savedInstanceState.getBoolean("WHIPPED_KEY");
        hasChocolate = savedInstanceState.getBoolean("CHOCOLATE_KEY");
        name = savedInstanceState.getString("PERSON_NAME");

        getWhippedCreamCheckBox().setChecked(hasWhippedCream);
        getChocolateCheckBox().setChecked(hasChocolate);

        displayQuantity(quantity);
        //displayMessage(createOrderSummary(name, price, hasWhippedCream, hasChocolate));

    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //displayMessage("Ordered coffee amount: " + quantity + "\n" + "Total: " + formatCurrency(calculatePrice(quantity,priceOfOneCup)) + "\n" + message);
        //int price = calculatePrice(quantity,priceOfOneCup);
        //displayPrice(price);

        EditText personNameText = (EditText) findViewById(R.id.person_name);
        name = personNameText.getText().toString();
        Log.v("MainActivity", "Person's name: " + name);

        price = calculatePrice(quantity, priceOfOneCup);

        //displayMessage(createOrderSummary(name, price, hasWhippedCream, hasChocolate));
        String subject = "JustJava order summary for " + name;
        composeEmail(subject, createOrderSummary(name,price,hasWhippedCream,hasChocolate));
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("geo:47.6,-122.3"));
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }

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
        if (quantity == 100) {
            Context context = getApplicationContext();
            CharSequence text = "You cannot have more than 99 coffees";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            return;
        }

        quantity++;
        displayQuantity(quantity);
    }

    public void decrement(View view) {

        if (quantity == 1) {
            Context context = getApplicationContext();
            CharSequence text = "You cannot have less than 1 coffee";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            return;
        }

        quantity--;
        displayQuantity(quantity);
    }

    private int calculatePrice(int quantity, int priceOfOneCup) {

        int price_temp = priceOfOneCup;

        if (hasWhippedCream) {
            price_temp += WHIPPED_CREAM_PRICE;
        }

        if (hasChocolate) {
            price_temp += CHOCOLATE_PRICE;
        }

        return price_temp * quantity;
    }

    private String formatCurrency(int number) {
        return NumberFormat.getCurrencyInstance().format(number);
    }

    private String createOrderSummary(String name, int price, boolean creamIsChecked, boolean chocolateIsChecked) {
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);

        fmt.format("Name: %1$s\nQuantity: %2$d\nTotal: %3$s\n%4$s", name, quantity, formatCurrency(price), message);
        Log.v("MainActivity", "Add whipped cream? " + creamIsChecked);
        Log.v("MainActivity", "Add chocolate? " + chocolateIsChecked);
        if (creamIsChecked) {
            sb.insert(sb.indexOf("\n") + 1, getString(R.string.order_summary_whipped_cream, formatCurrency(WHIPPED_CREAM_PRICE * quantity)) + ".\n");
        }
        if (chocolateIsChecked) {
            sb.insert(sb.indexOf("\n") + 1, getString(R.string.order_summary_chocolate, formatCurrency(CHOCOLATE_PRICE * quantity)) + ".\n");
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
            case R.id.whipped_cream_checkbox:
                hasWhippedCream = checked;
                break;
            case R.id.chocolate_checkbox:
                hasChocolate = checked;
                break;
        }
    }

    public void composeEmail(String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}
