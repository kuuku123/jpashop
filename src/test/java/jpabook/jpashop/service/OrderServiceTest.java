package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest
{
    @Autowired
    EntityManager em;
    @Autowired OrderService orderService;
    @Autowired
    OrderRepository orderRepository;



    @Test
    public void 상품주문() throws Exception
    {
        //given
        Member member = getMember();

        Book book = getBook("시골 JPA", 10000, 10);

        int orderCount = 2;
        //when
        Long order = orderService.order(member.getId(), book.getId(), orderCount);
        //then
        Order getOrder = orderRepository.findOne(order);
        assertEquals( OrderStatus.ORDER,getOrder.getOrderStatus(),"상품 주문시 상태가 ORDER");
        assertEquals(1,getOrder.getOrderItems().size(),"주문한 상품 종류 수가 정확해야한다.");
        assertEquals(10000*orderCount,getOrder.getTotalPrice(),"주문가격은 가격 * 수량이다.");
        assertEquals(8,book.getStockQuantity(),"주문수량 만큼 재고가 줄어야한다.");
    }

    @Test
    public void 주문취소() throws Exception
    {
        Member member = getMember();
        Book book = getBook("임도현", 10000, 10);
        int orderCount = 3;
        Long getOrder = orderService.order(member.getId(), book.getId(), orderCount);

        orderService.cancelOrder(getOrder);
        assertEquals(10,book.getStockQuantity(),"수량이 돌와와야한다.");
        assertEquals(OrderStatus.CANCEL,orderRepository.findOne(getOrder).getOrderStatus(),"Cancel 상태가 되어야한다.");


    }
    
    @Test
    public void 상품주문_재고수량초과() throws Exception
    {
        //given
        Member member = getMember();
        Book book = getBook("시골 JPA", 10000, 10);
        int orderCount = 11;
        //when
        assertThrows(NotEnoughStockException.class,() ->
                orderService.order(member.getId(),book.getId(),orderCount),"재고수량 부족");
        //then
    }

    private Book getBook(String name, int price, int stockQuantity)
    {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member getMember()
    {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);
        return member;
    }


}