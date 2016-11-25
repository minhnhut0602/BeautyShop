package mimishop.yanji.com.mimishop.adapter;

import mimishop.yanji.com.mimishop.modal.Product;

/**
 * Created by KCJ on 4/9/2015.
 */
public class ProductSectionizer implements Sectionizer<Product> {

    @Override
    public String getSectionTitleForItem(Product price) {
        return price.getName();
    }
}