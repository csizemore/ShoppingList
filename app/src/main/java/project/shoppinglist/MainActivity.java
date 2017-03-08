package project.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_NEW_PRODUCT = 101;
    public static final int REQUEST_EDIT_PRODUCT = 102;
    public static final String KEY_EDIT = "KEY_EDIT";
    private ProductsAdapter productsAdapter;
    private CoordinatorLayout layoutContent;
    private DrawerLayout drawerLayout;
    private Product productToEditHolder;
    private int productToEditPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Product> productsList = Product.listAll(Product.class);

        productsAdapter = new ProductsAdapter(productsList, this);
        RecyclerView recyclerViewProducts = (RecyclerView) findViewById(
                R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProducts.setAdapter(productsAdapter);

        ProductsListTouchHelperCallback touchHelperCallback = new ProductsListTouchHelperCallback(
                productsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(
                touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerViewProducts);

        layoutContent = (CoordinatorLayout) findViewById(
                R.id.layoutContent);

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateProductActivity();
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.action_add:
                                showCreateProductActivity();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_deleteall:
                                deleteProductActivity();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_total:
                                totalProductActivity();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_about:
                                showSnackBarMessage(getString(R.string.txt_about));
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_help:
                                showSnackBarMessage(getString(R.string.txt_help));
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                        }

                        return false;
                    }
                });

        setUpToolBar();
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void showCreateProductActivity() {
        Intent intentStart = new Intent(MainActivity.this,
                CreateProductActivity.class);
        startActivityForResult(intentStart, REQUEST_NEW_PRODUCT);
    }

    private void deleteProductActivity() {
        productsAdapter.removeAll();
    }

    private void totalProductActivity() {
        int sum = productsAdapter.totalSum();
        showSnackBarMessage(getString(R.string.total)+sum);
    }

    public void showEditProductActivity(Product productToEdit, int position) {
        Intent intentStart = new Intent(MainActivity.this,
                CreateProductActivity.class);
        productToEditHolder = productToEdit;
        productToEditPosition = position;

        intentStart.putExtra(KEY_EDIT, productToEdit);
        startActivityForResult(intentStart, REQUEST_EDIT_PRODUCT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_NEW_PRODUCT) {
                    Product product = (Product) data.getSerializableExtra(
                            CreateProductActivity.KEY_PRODUCT);

                    productsAdapter.addProduct(product);
                    showSnackBarMessage(getString(R.string.txt_product_added));
                } else if (requestCode == REQUEST_EDIT_PRODUCT) {
                    Product productTemp = (Product) data.getSerializableExtra(
                            CreateProductActivity.KEY_PRODUCT);

                    productToEditHolder.setProductName(productTemp.getProductName());
                    productToEditHolder.setProductDescription(productTemp.getProductDescription());
                    productToEditHolder.setProductType(productTemp.getProductType());
                    productToEditHolder.setEstimatedPrice(productTemp.getEstimatedPrice());
                    productToEditHolder.setChecked(productTemp.getChecked());

                    if (productToEditPosition != -1) {
                        productsAdapter.updateProduct(productToEditPosition, productToEditHolder);
                        productToEditPosition = -1;
                    }else {
                        productsAdapter.notifyDataSetChanged();
                    }
                    showSnackBarMessage(getString(R.string.txt_product_edited));
                }
                break;
            case RESULT_CANCELED:
                showSnackBarMessage(getString(R.string.txt_add_cancel));
                break;
        }
    }


    private void showSnackBarMessage(String message) {
        Snackbar.make(layoutContent,
                message,
                Snackbar.LENGTH_LONG
        ).setAction(R.string.action_hide, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showCreateProductActivity();
                return true;
            case R.id.action_deleteall:
                deleteProductActivity();
                return true;
            default:
                showCreateProductActivity();
                return true;
        }
    }

}