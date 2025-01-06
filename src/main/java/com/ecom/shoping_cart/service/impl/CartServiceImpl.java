package com.ecom.shoping_cart.service.impl;

import com.ecom.shoping_cart.model.Cart;
import com.ecom.shoping_cart.model.Product;
import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.repository.CartRepository;
import com.ecom.shoping_cart.repository.ProductRepository;
import com.ecom.shoping_cart.repository.UserRepository;
import com.ecom.shoping_cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart saveCart(Integer userId, Integer productId) {
        UserDtls user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return null;
        }

       Cart cartStatus = cartRepository.findByUserIdAndProductId(userId, productId);

        if(cartStatus != null){
            cartStatus.setQuantity(cartStatus.getQuantity() + 1);
            cartStatus.setTotalPrice(cartStatus.getQuantity() * product.getDiscountPrice());
            return cartRepository.save(cartStatus);
        }

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(1);
        cart.setTotalPrice(1 * product.getDiscountPrice());

        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getCartByUser(Integer userId) {

        List<Cart> carts= cartRepository.findByUserId(userId);

        List<Cart> updateCarts = new ArrayList<>();

        Double totalOrderPrice = 0.0;

        for(Cart cart : carts){
            Double totalPrice = (cart.getQuantity() * cart.getProduct().getDiscountPrice());
            totalPrice = Double.valueOf(String.format("%.2f", totalPrice));
            cart.setTotalPrice(totalPrice);


            totalOrderPrice += totalPrice;
            totalOrderPrice = Double.valueOf(String.format("%.2f", totalOrderPrice));
            cart.setTotalOrderPrice(totalOrderPrice);

            updateCarts.add(cart);
        }

        return updateCarts;
    }

    @Override
    public Integer getCartCount(Integer userId) {
        return  cartRepository.countByUserId(userId);
    }

    @Override
    public void updateQuantity(Integer cartId, String type) {

        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            return;
        }

        switch (type) {
            case "inc":
                cart.setQuantity(cart.getQuantity() + 1);
                break;

            case "dec":
                if (cart.getQuantity() > 1) {
                    cart.setQuantity(cart.getQuantity() - 1);
                } else {
                    cartRepository.delete(cart);
                    return;
                }
                break;

            default:
                break;
        }
        cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());

        cartRepository.save(cart);

    }

}
