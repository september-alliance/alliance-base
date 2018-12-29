package org.september.smartdao.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Order {

    private Direction direction;
    private String property;

    /**
     * 构造函数
     * @param property
     * @param orderExpr
     */
    public Order(String property, Direction direction) {
        this.direction = direction;
        this.property = property;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getProperty() {
        return property;
    }



    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setProperty(String property) {
        this.property = property;
    }


    private static String INJECTION_REGEX = "[A-Za-z0-9\\_\\-\\+\\.]+";
    public static boolean isSQLInjection(String str){
        return !Pattern.matches(INJECTION_REGEX, str);
    }

    @Override
    public String toString() {
        if(isSQLInjection(property)){
            throw new IllegalArgumentException("SQLInjection property: "+property);
        }

        return property + (direction == null ? "" : " " + direction.name());
    }



    public static List<Order> formString(String orderSegment){
        return formString(orderSegment, null);
    }

    /**
     * @param orderSegment  ex: "id.asc,code.desc" or "code.desc"
     */
    public static List<Order> formString(String orderSegment,String orderExpr){
        if(orderSegment == null || orderSegment.trim().equals("")) {
            return new ArrayList<Order>(0);
        }

        List<Order> results = new ArrayList<Order>();
        String[] orderSegments = orderSegment.trim().split(",");
        for(int i = 0; i < orderSegments.length; i++) {
            String sortSegment = orderSegments[i];
            Order order = _formString(sortSegment, orderExpr);
            if(order != null){
                results.add(order);
            }
        }
        return results;
    }


    private static Order _formString(String orderSegment, String orderExpr){

        if(orderSegment == null || orderSegment.trim().equals("") ||
            orderSegment.startsWith("null.") ||  orderSegment.startsWith(".")){
            return null;
        }

        String[] array = orderSegment.trim().split("\\.");
        if(array.length != 1 && array.length != 2){
            throw new IllegalArgumentException("orderSegment pattern must be {property}.{direction}, input is: "+orderSegment);
        }

        return create(array[0], array.length == 2 ? array[1] : "asc", orderExpr);
    }

    public static Order create(String property, String direction){
        return create(property, direction,null);
    }

    /**
     *
     * @param property
     * @param direction
     * @param orderExpr  placeholder is "?", in oracle like: "nlssort( ? ,'NLS_SORT=SCHINESE_PINYIN_M')".
     *                   Warning: you must prevent orderExpr SQL injection.
     * @return
     */
    public static Order create(String property, String direction, String orderExpr){
        return new Order(property, Order.Direction.fromString(direction));
    }


    /**
     * PropertyPath implements the pairing of an {@link Direction} and a property. It is used to provide input for
     *
     * @author Oliver Gierke
     */
    public static enum Direction {
        ASC, DESC;
        public static Direction fromString(String value) {
            try {
                return Direction.valueOf(value.toUpperCase(Locale.US));
            } catch (Exception e) {
                return ASC;
            }
        }
    }

}
