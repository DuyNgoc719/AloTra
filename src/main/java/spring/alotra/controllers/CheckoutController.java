package spring.alotra.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import spring.alotra.entity.*;
import spring.alotra.service.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart/")
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private VnpayService vnpayService;

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        List<Cart> cartList = cartService.getCartItems(user.getId());

        List<CartDTO> cartDTOList = cartList.stream().map(cart -> {
            CartDTO dto = new CartDTO();
            dto.setId(cart.getId());
            dto.setProduct(cart.getProduct());
            dto.setQuantity(cart.getQuantity());
            return dto;
        }).collect(Collectors.toList());

        double totalAmount = cartDTOList.stream()
                .mapToDouble(CartDTO::getTotalPrice)
                .sum();

        model.addAttribute("cartItems", cartDTOList);
        model.addAttribute("totalAmount", totalAmount);

        return "form/BillPay";
    }

    @PostMapping("/submit-order")
    public ModelAndView submitOrder(Model model, HttpSession session,
                                    @RequestParam("paymentMethod") String paymentMethod,
                                    @RequestParam("address") String address,
                                    @RequestParam("note") String note,
                                    @RequestParam("total") Double total) {
        User user = (User) session.getAttribute("user");
        ModelAndView mav = new ModelAndView("redirect:/cart");
        if (user == null) {
            return mav;
        }

        Order order = new Order();
        order.setCustomerId(user.getId());
        order.setShippingAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setNote(note);
        order.setTotalAmount(total);

        orderService.save(order);

        session.setAttribute("order", order);

        List<Cart> cartItems = cartService.getCartItems(user.getId());

        if (cartItems != null) {
            List<OrderDetail> orderDetails = cartItems.stream().map(cart -> {
                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProductId(cart.getProduct().getId());
                detail.setQuantity(cart.getQuantity());
                detail.setUnitPrice(cart.getProduct().getPrices());
                return detail;
            }).collect(Collectors.toList());
            Cart find_cart = cartService.findByIdCustomer(user.getId());
            cartService.deleteCart(find_cart);
            orderDetailService.save(orderDetails);
        }

        session.removeAttribute("cart");

        model.addAttribute("message", "Đơn hàng đã được đặt thành công!");
        if (paymentMethod.equals("BankTransfer")) {
            mav.setViewName("redirect:/cart/create-payment");
            session.setAttribute("total", order.getTotalAmount());
        }
        return mav;

    }
    @GetMapping("/create-payment")
    public RedirectView generatePaymentUrl(HttpSession session) {

        Double amount = (Double) session.getAttribute("total");

        Order order = (Order) session.getAttribute("order");

        String paymentUrl =  vnpayService.generateVnPayUrl(amount, order.getOrderId().toString());
        System.out.println(paymentUrl);
        return new RedirectView(paymentUrl);
    }
    private Map<String, String> getParametersFromRequest(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));
        return params;
    }

    @PostMapping("/notification")
    public String handleNotification(@RequestBody VnpayNotification notification) {
        String vnp_ResponseCode = notification.getVnp_ResponseCode();
        if (vnp_ResponseCode.equals("00")) {
            return "redirect:/cart/checkout/success";
        } else {
            return "redirect:/cart/checkout/failure";
        }
    }

    @GetMapping("/notification")
    public String handleNotificationGet(HttpServletRequest request) {
        Map<String, String> vnpParams = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            vnpParams.put(paramName, request.getParameter(paramName));
        }
        String vnp_ResponseCode = vnpParams.get("vnp_ResponseCode");

        if (vnp_ResponseCode.equals("00")) {
            return "redirect:/cart/checkout/success";
        } else {
            return "redirect:/cart/checkout/failure";
        }
    }


    @GetMapping("/checkout/success")
    public String success() {
        return "form/success";
    }

    @GetMapping("/checkout/failure")
    public String failure() {
        return "form/failure";
    }


}
