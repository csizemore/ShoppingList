package project.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class CreateProductActivity extends AppCompatActivity {
    public static final String KEY_PRODUCT = "KEY_PRODUCT";
    private Spinner spinnerProductType;
    private EditText etProduct;
    private EditText etDesc;
    private EditText etEstimate;
    private Product productToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        if (getIntent().getSerializableExtra(MainActivity.KEY_EDIT) != null) {
            productToEdit = (Product) getIntent().getSerializableExtra(MainActivity.KEY_EDIT);
        }

        spinnerProductType = (Spinner) findViewById(R.id.spinnerProductType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.producttypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductType.setAdapter(adapter);

        etProduct = (EditText) findViewById(R.id.etProductName);
        etDesc = (EditText) findViewById(R.id.etDesc);
        etEstimate = (EditText) findViewById(R.id.etEstimate);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProduct();
            }
        });

        if (productToEdit != null) {
            etProduct.setText(productToEdit.getProductName());
            etDesc.setText(productToEdit.getProductDescription());
            etEstimate.setText(productToEdit.getEstimatedPrice());
            spinnerProductType.setSelection(productToEdit.getProductType().getValue());
        }
    }

    private void saveProduct() {
        Intent intentResult = new Intent();
        Product productResult = null;
        if (productToEdit != null) {
            productResult = productToEdit;
        } else {
            productResult = new Product();
        }

        productResult.setProductName(etProduct.getText().toString());
        productResult.setProductDescription(etDesc.getText().toString());
        productResult.setEstimatedPrice(etEstimate.getText().toString());

        productResult.setProductType(
                Product.ProductType.fromInt(spinnerProductType.getSelectedItemPosition()));

        intentResult.putExtra(KEY_PRODUCT, productResult);
        setResult(RESULT_OK, intentResult);
        finish();
    }
}