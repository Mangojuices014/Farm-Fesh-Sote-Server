package com.kira.farm_fresh_store.service.cart;

import com.kira.farm_fresh_store.dto.order.CartDto;
import com.kira.farm_fresh_store.dto.product.ProductDto;
import com.kira.farm_fresh_store.entity.order.Cart;
import com.kira.farm_fresh_store.entity.product.Product;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.CartRepository;
import com.kira.farm_fresh_store.repository.ProductRepository;
import com.kira.farm_fresh_store.repository.UserRepository;
import com.kira.farm_fresh_store.request.order.CreateCartRequest;
import com.kira.farm_fresh_store.utils.AuthenUtil;
import com.kira.farm_fresh_store.utils.Util;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;

    private final Util util;

    @Override
    public CartDto addCart(CreateCartRequest request, Long userId) {
        Cart checkExisted = cartRepository.findByProductAndUser(request.getProductId(), userId);
        if (checkExisted != null) {
            checkExisted.setQuantity(checkExisted.getQuantity() + request.getQuantity());
            checkExisted.setQuantity(Math.max(checkExisted.getQuantity(), 0)); // Đảm bảo không âm
            cartRepository.save(checkExisted);
            return modelMapper.map(checkExisted, CartDto.class);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
        Cart cart = new Cart();
        Cart lastCart = cartRepository.findFirstByOrderByIdDesc();
        if (lastCart == null) {
            cart.setId(util.createNewID("CART"));
        } else {
            cart.setId(util.createIDFromLastID("CART", 4, lastCart.getId()));
        }
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(request.getQuantity());
        cart.setSelected(0);
        cartRepository.save(cart);
        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public List<CartDto> getCartByUser() {
        long userId = AuthenUtil.getProfileId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng."));

        // Lấy danh sách giỏ hàng của user
        List<Cart> carts = cartRepository.findByUser(user);
        if (carts.isEmpty()) {
            throw new ResourceNotFoundException("Giỏ hàng của bạn đang trống.");
        }

        // Chuyển đổi danh sách Cart sang CartDto
        return carts.stream().map(cart -> {
            CartDto cartDto = new CartDto();
            cartDto.setId(cart.getId());
            cartDto.setProductId(cart.getProduct() != null ? cart.getProduct().getId() : null);
            cartDto.setName(cart.getProduct() != null ? cart.getProduct().getName() : "Sản phẩm không tồn tại");
            cartDto.setPrice(cart.getProduct() != null ? cart.getProduct().getPrice() : 0.0);
            cartDto.setImage(cart.getProduct() != null ? cart.getProduct().getImage() : null);
            cartDto.setQuantity(cart.getQuantity());
            cartDto.setSelected(cart.getSelected());

            return cartDto;
        }).collect(Collectors.toList());
    }


    @Override
    public String deleteCartById(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));
        cartRepository.delete(cart);
        return "Xóa thanh công";
    }

    @Override
    public String deleteAllCart() {
        long userId = AuthenUtil.getProfileId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng."));

        List<Cart> carts = cartRepository.findByUser(user);

        if (carts.isEmpty()) {
            return "Giỏ hàng của bạn đã trống.";
        }

        cartRepository.deleteAll(carts);
        return "Xóa toàn bộ giỏ hàng thành công.";
    }

    @Override
    public String updateSelectCartById(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotFoundException("Không tim thấy giỏ hàng"));

        if (cart.getSelected()==1){
            cart.setSelected(0);
        }else {
            cart.setSelected(1);
        }
        cartRepository.save(cart);
        return "Cập nhập thành công";
    }

    @Override
    public String updateQualityPlusCartById(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotFoundException("Không tim thấy giỏ hàng"));
        cart.setQuantity(cart.getQuantity()+1);
        cartRepository.save(cart);
        return "Cập nhập thành công";
    }

    @Override
    public String updateQualityMinusCartById(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotFoundException("Không tim thấy giỏ hàng"));
        cart.setQuantity(cart.getQuantity()-1);
        cartRepository.save(cart);
        return "Cập nhập thành công";     }

}
