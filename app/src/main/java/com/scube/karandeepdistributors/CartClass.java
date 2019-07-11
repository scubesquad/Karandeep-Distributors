package com.scube.karandeepdistributors;

import com.scube.karandeepdistributors.model.Order;

import java.util.ArrayList;

/**
 * Adding and Removing Products from Cart
 */
public class CartClass  {
  public static ArrayList<Order> cartArrayList = new ArrayList<>();

  /**
   * arraylist of products is added to cart
   * @param order
   */
  public void addToCart(Order order) {
    cartArrayList.add(order);
  }
  /**
   * remove particular product from cart according to its position
   * @param position
   */
  public void removeItem(int position) {
    this.cartArrayList.remove(position);
  }
  /**
   * get Total number of items count
   * @return
   */
  public int getTotalNumberOfItems() {
    return cartArrayList.size();
  }
  /**
   * get list of cart items
   * @return
   */
  public ArrayList<Order> getCartArrayList() {
    return cartArrayList;
  }

}
