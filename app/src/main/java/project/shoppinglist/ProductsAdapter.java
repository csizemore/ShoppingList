package project.shoppinglist;

/**
 * Created by Crystal on 11/14/2016.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Peter on 2015.07.01..
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon;
        public TextView tvProduct;
        public Button btnDelete;
        public Button btnEdit;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvProduct = (TextView) itemView.findViewById(R.id.tvProduct);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }

    private List<Product> productsList;
    private Context context;
    private int lastPosition = -1;

    public ProductsAdapter(List<Product> productsList, Context context) {
        this.productsList = productsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_product, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.tvProduct.setText(productsList.get(position).getProductName());
        viewHolder.ivIcon.setImageResource(
                productsList.get(position).getProductType().getIconId());

        viewHolder.checkBox.setChecked(productsList.get(position).getChecked());

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsList.get(viewHolder.getAdapterPosition()).
                        setChecked(viewHolder.checkBox.isChecked());
                productsList.get(viewHolder.getAdapterPosition()).save();
            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProduct(position);
            }
        });
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showEditProductActivity(productsList.get(position), position);
            }
        });

        setAnimation(viewHolder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public int totalSum() {
        int sum = 0;
        for (int i = 0; i < productsList.size(); i++){
            if (productsList.get(i).getEstimatedPrice().isEmpty()){
                sum += 0;
            } else {
                sum += Integer.parseInt(productsList.get(i).getEstimatedPrice());
            }
        }
        return sum;

    }

    public void addProduct(Product product) {
        product.save();
        productsList.add(product);
        notifyDataSetChanged();
    }

    public void updateProduct(int index, Product product) {
        productsList.set(index, product);
        product.save();
        notifyItemChanged(index);
    }

    public void removeProduct(int index) {
        // remove it from the DB
        productsList.get(index).delete();
        // remove it from the list
        productsList.remove(index);
        notifyDataSetChanged();
    }

    public void removeAll() {
        productsList.clear();
        notifyDataSetChanged();
    }

    public void swapProducts(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(productsList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(productsList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    public Product getProduct(int i) {
        return productsList.get(i);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
