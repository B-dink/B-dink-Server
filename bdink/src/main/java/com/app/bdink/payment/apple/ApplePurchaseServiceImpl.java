package com.app.bdink.payment.apple;

import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplePurchaseServiceImpl implements ApplePurchaseService {

    private final AppleProductRepository appleProductRepository;

    @Override
    public AppleProduct findProduct(String productId) {
        return appleProductRepository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND, Error.NOT_FOUND.getMessage()));
    }
}
