package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService
{
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    /**
     * order
     */
    @Transactional
    public Long order(Long memberId,Long itemId, int orderCount)
    {

        //entity inquiry
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //create delivery information
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());// actual would be different customer need to write where they want to send

        //create orderitem
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), orderCount);

        //create order
        Order order = Order.createOrder(member, delivery, orderItem);

        //save order
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * cancel order
     */
    @Transactional
    public void cancelOrder(Long orderId)
    {
        // order entity inquiry
        Order order = orderRepository.findOne(orderId);

        //cancel order
        order.cancel();

    }
}
