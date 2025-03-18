package com.kira.farm_fresh_store.service.address;

import com.kira.farm_fresh_store.entity.user.Address;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.AddressRepository;
import com.kira.farm_fresh_store.repository.UserRepository;
import com.kira.farm_fresh_store.request.user.AddressRequest;
import com.kira.farm_fresh_store.utils.AuthenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    @Override
    public Address createAddress(AddressRequest request) {
        Long userId = AuthenUtil.getProfileId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tim thấy user"));
        Address address = new Address();
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        addressRepository.save(address);
        user.setAddress(address);
        userRepository.save(user);
        return address;
    }

    @Override
    public Address getAddressByUser() {
        return null;
    }

    @Override
    public Address updateAddress() {
        return null;
    }

    @Override
    public Address deleteAddress() {
        return null;
    }
}
