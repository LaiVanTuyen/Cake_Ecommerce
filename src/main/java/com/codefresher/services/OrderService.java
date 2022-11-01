package com.codefresher.services;

import com.codefresher.dto.CheckOutDTO;
import com.codefresher.dto.OrderDTO;
import com.codefresher.dto.OrderItemDTO;
import com.codefresher.entities.Order;
import com.codefresher.entities.OrderItem;
import com.codefresher.repositories.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private ProductQuantityService productQuantityService;

    public void saveOrder(List<OrderItemDTO> orderItemDTOList, Order order){
        List<OrderItem> orderItems = orderItemService.listDTOtoEntity(orderItemDTOList);
        if(userService.getUserByLogged().getUserId() != null){
            order.setUserOrder(userService.getUserByLogged());
        }
        order.setPayTotal(countPayTotal(orderItems));
        order.setBuyingDate(getCurrentDate());
        order.setStatus(0);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderItem(order);
        }
        order.setOrderItems(orderItems);
        repository.saveAndFlush(order);
    }

    public List<OrderDTO> getListOrderPending(){
        List<Order> orderList = repository.findAllByStatusEquals(0);
        List<OrderDTO> orderDTOList = new ArrayList<>();
        orderList.forEach(data -> {
            OrderDTO orderDTO = toDTO(data);
            orderDTO.setPayTotalCurrency(calculateService.numberToCurrency(data.getPayTotal()));
            orderDTOList.add(orderDTO);
        });
        return orderDTOList;
    }

    public OrderDTO getOrderDTOById(Long id){
        Order order = repository.findOrderByOrderId(id);
        OrderDTO orderDTO = toDTO((order));
        orderDTO.setPayTotalCurrency(calculateService.numberToCurrency(order.getPayTotal()));
        return orderDTO;
    }

    public List<OrderDTO> getListOrderByUserId(Long userId){
        List<Order> orders = repository.findAllByUserOrderUserId(userId);
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(Order order: orders){
            OrderDTO orderDTO = toDTO(order);
            orderDTO.setPayTotalCurrency(calculateService.numberToCurrency(orderDTO.getPayTotal()));
            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }
    public void updateOrderStatus(Long orderId, int action){
        Order order = repository.findOrderByOrderId(orderId);

        // update quantity of product
        if (action == 2){
            List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
            order.getOrderItems().forEach(orderItem -> {
                OrderItemDTO orderItemDTO = orderItemToDTO(orderItem);
                orderItemDTOList.add(orderItemDTO);
            });
            productQuantityService.updateQuantity(orderItemDTOList, "add");
        }

        order.setStatus(action);
        repository.save(order);
    }
    public Order getOrderById(Long orderId){
        return repository.findOrderByOrderId(orderId);
    }

    public List<OrderItemDTO> getListOrderItemByOrderId(Long orderId){
        List<OrderItem> orderItems = repository.findOrderByOrderId(orderId).getOrderItems();
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        for(OrderItem data : orderItems){
            OrderItemDTO orderItemDTO = orderItemToDTO(data);
            orderItemDTO.setImageName(data.getQuantityItem().getProductQuantity().getProductImages().get(0).getName());
            orderItemDTO.setProductName(data.getQuantityItem().getProductQuantity().getName() + " "
                    + data.getQuantityItem().getProductQuantity().getCode());
            orderItemDTO.setTotalCurrency(calculateService.numberToCurrency(data.getPrice()*data.getQuantity()));
            orderItemDTOList.add(orderItemDTO);
        }
        return orderItemDTOList;
    }

    public List<CheckOutDTO> orderItemToCheckOut(List<OrderItemDTO> orderItemDTOList){
        List<CheckOutDTO> checkOutDTOList = new ArrayList<>();
        orderItemDTOList.forEach(data -> {
            CheckOutDTO checkOutDTO = new CheckOutDTO();
            checkOutDTO.setQuantity(data.getQuantity());
            checkOutDTO.setQuantityId(data.getQuantityId());
            checkOutDTOList.add(checkOutDTO);
        });
        return checkOutDTOList;
    }

    private int countPayTotal(List<OrderItem> orderItems){
        int total = 0;
        for(OrderItem orderItem: orderItems){
            total += (orderItem.getPrice()*orderItem.getQuantity());
        }
        return total;
    }
    private String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }
    //mapper
    public OrderDTO toDTO(Order order){
        return mapper.map(order, OrderDTO.class);
    }
    public OrderItemDTO orderItemToDTO(OrderItem orderItem){
        return mapper.map(orderItem, OrderItemDTO.class);
    }
}
