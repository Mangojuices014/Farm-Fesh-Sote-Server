package com.kira.farm_fresh_store.service.address;

import com.kira.farm_fresh_store.entity.user.Address;
import com.kira.farm_fresh_store.request.user.AddressRequest;

public interface IAddressService {
    Address createAddress(AddressRequest request);
    Address getAddressByUser();
    Address updateAddress();
    Address deleteAddress();
}
