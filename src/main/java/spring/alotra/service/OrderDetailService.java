package spring.alotra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.alotra.entity.OrderDetail;
import spring.alotra.repository.OrderDetailRepository;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    public void save(List<OrderDetail> orderDetail) {
        orderDetailRepository.saveAll(orderDetail);
    }



}
